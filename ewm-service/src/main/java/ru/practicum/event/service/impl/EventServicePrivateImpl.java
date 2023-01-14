package ru.practicum.event.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.exception.CategoryNotFoundException;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.enums.RequestState;
import ru.practicum.event.exception.EventNotFoundException;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.dto.EventFullDto;
import ru.practicum.event.model.dto.NewEventDto;
import ru.practicum.event.model.dto.UpdateEventRequest;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.service.EventServicePrivate;
import ru.practicum.exceptions.ValidatorException;
import ru.practicum.mappers.EventMapper;
import ru.practicum.mappers.RequestMapper;
import ru.practicum.request.exception.RequestNotFountException;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.dto.RequestDto;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.exception.UserNotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EventServicePrivateImpl implements EventServicePrivate {
    EventRepository eventRepository;
    CategoryRepository categoryRepository;
    UserRepository userRepository;
    RequestRepository requestRepository;

    @Override
    public List<EventFullDto> getEventsByCurrentUser(long userId, Pageable pageable) {
        return eventRepository.findEventsByInitiator_Id(userId, pageable).stream()
                .map(EventMapper.INSTANCE::toFullEvent)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEventByCurrentUser(long userId, UpdateEventRequest updateEventRequest) {
        Event event = getEventOrThrowEventNotFound(updateEventRequest.getEventId(), userId);
        Category category = categoryRepository.findById(updateEventRequest.getCategory())
                .orElseThrow(() -> new CategoryNotFoundException(updateEventRequest.getCategory()));
        Event eventUpdated = EventMapper.INSTANCE.partialUpdate(updateEventRequest, event);
        eventUpdated.setCategory(category);
        if (event.getState().equals(EventState.PENDING)) {
            return EventMapper.INSTANCE.toFullEvent(eventRepository.save(eventUpdated));
        } else if (event.getState().equals(EventState.CANCELED)) {
            event.setState(EventState.PENDING);
            return EventMapper.INSTANCE.toFullEvent(eventRepository.save(eventUpdated));
        } else {
            throw new ValidatorException("Only pending or canceled events can be changed");
        }
    }

    @Override
    public EventFullDto addNewEvent(long userId, NewEventDto newEventDto) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidatorException("Only pending or canceled events can be changed");
        }
        Event event = EventMapper.INSTANCE.toEvent(newEventDto);
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new CategoryNotFoundException(newEventDto.getCategory()));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        event.setCategory(category);
        event.setInitiator(user);
        return EventMapper.INSTANCE.toFullEvent(eventRepository.save(event));
    }

    @Override
    public EventFullDto getEventByCurrentUser(long userId, long eventId) {
        return EventMapper.INSTANCE.toFullEvent(eventRepository.findEventByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new EventNotFoundException(eventId)));
    }

    @Override
    public EventFullDto cancelEventByCurrentUser(long userId, long eventId) {
        Event event = getEventOrThrowEventNotFound(eventId, userId);
        event.setState(EventState.CANCELED);
        return EventMapper.INSTANCE.toFullEvent(eventRepository.save(event));
    }

    @Override
    public List<RequestDto> getOtherRequestsByEventAndCurrentUser(long userId, long eventId) {
        Event event = getEventOrThrowEventNotFound(eventId, userId);
        return requestRepository.findRequestsByEvent(eventId).stream()
                .map(RequestMapper.INSTANCE::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestDto confirmRequest(long userId, long eventId, long reqId) {
        Event event = getEventOrThrowEventNotFound(eventId, userId);
        if (event.getConfirmedRequests() == event.getParticipantLimit()) {
            List<Request> requests = requestRepository.findAll().stream()
                    .filter(request -> request.getStatus() == RequestState.PENDING && request.getEvent() == eventId)
                    .map(request -> {
                        request.setStatus(RequestState.REJECTED);
                        return request;
                    })
                    .collect(Collectors.toList());
            requestRepository.saveAll(requests);
            throw new ValidatorException("Достигнут лимит участия в событии");
        }
        long confirmed = event.getConfirmedRequests();
        event.setConfirmedRequests(++confirmed);
        eventRepository.save(event);
        Request request = requestRepository.findById(reqId)
                .orElseThrow(() -> new RequestNotFountException(reqId));
        request.setStatus(RequestState.CONFIRMED);
        return RequestMapper.INSTANCE.toRequestDto(requestRepository.save(request));
    }

    @Override
    public RequestDto rejectRequest(long userId, long eventId, long reqId) {
        Event event = getEventOrThrowEventNotFound(eventId, userId);
        Request request = requestRepository.findById(reqId)
                .orElseThrow(() -> new RequestNotFountException(reqId));
        request.setStatus(RequestState.REJECTED);
        return RequestMapper.INSTANCE.toRequestDto(requestRepository.save(request));
    }

    private Event getEventOrThrowEventNotFound(long eventId, long userId) {
        return eventRepository.findEventByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new EventNotFoundException("У данного пользователя нет событий"));
    }
}