package ru.practicum.event.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.exception.CategoryNotFoundException;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.enums.RequestState;
import ru.practicum.event.exception.EventNotFoundException;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.dto.EventDto;
import ru.practicum.event.model.dto.NewEventDto;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.service.EventServicePrivate;
import ru.practicum.exceptions.ValidatorException;
import ru.practicum.mappers.EventMapper;
import ru.practicum.request.exception.RequestNotFountException;
import ru.practicum.request.model.Request;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.exception.UserNotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServicePrivateImpl implements EventServicePrivate {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    @Autowired
    public EventServicePrivateImpl(EventRepository eventRepository,
                                   CategoryRepository categoryRepository,
                                   UserRepository userRepository,
                                   RequestRepository requestRepository) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public List<Event> getEventsByCurrentUser(long userId, Pageable pageable) {
        return eventRepository.findEventsByInitiator_Id(userId, pageable);
    }

    @Override
    public Event updateEventByCurrentUser(long userId, EventDto eventDto) {
        Event event = eventRepository.findEventByIdAndInitiator_Id(eventDto.getEventId(), userId)
                .orElseThrow(() -> new EventNotFoundException(eventDto.getEventId()));
        Category category = categoryRepository.findById(eventDto.getCategory())
                .orElseThrow(() -> new CategoryNotFoundException(eventDto.getCategory()));
        Event eventUpdated = EventMapper.INSTANCE.partialUpdate(eventDto, event);
        eventUpdated.setCategory(category);
        if (event.getState().equals(EventState.PENDING)) {
            return eventRepository.save(eventUpdated);
        } else if (event.getState().equals(EventState.CANCELED)) {
            event.setState(EventState.PENDING);
            return eventRepository.save(eventUpdated);
        } else {
            throw new ValidatorException("Only pending or canceled events can be changed");
        }
    }

    @Override
    public Event addNewEvent(long userId, NewEventDto newEventDto) {
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
        return eventRepository.save(event);
    }

    @Override
    public Event getEventByCurrentUser(long userId, long eventId) {
        return eventRepository.findEventByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
    }

    @Override
    public Event cancelEventByCurrentUser(long userId, long eventId) {
        Event event = eventRepository.findEventByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        event.setState(EventState.CANCELED);
        return eventRepository.save(event);
    }

    @Override
    public List<Request> getOtherRequestsByEventAndCurrentUser(long userId, long eventId) {
        Event event = eventRepository.findEventByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new EventNotFoundException("У данного пользователя нет событий"));
        return requestRepository.findRequestsByEvent(eventId);
    }

    @Override
    public Request confirmRequest(long userId, long eventId, long reqId) {
        Event event = eventRepository.findEventByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new EventNotFoundException("У данного пользователя нет событий"));
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
        return requestRepository.save(request);
    }

    @Override
    public Request rejectRequest(long userId, long eventId, long reqId) {
        Event event = eventRepository.findEventByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new EventNotFoundException("У данного пользователя нет событий"));
        Request request = requestRepository.findById(reqId)
                .orElseThrow(() -> new RequestNotFountException(reqId));
        request.setStatus(RequestState.REJECTED);
        return requestRepository.save(request);
    }

    private static Event updateEvent(Event event, EventDto eventDto) {
        event.setAnnotation(eventDto.getAnnotation());
        event.setDescription(eventDto.getDescription());
        if (eventDto.getEventDate().isBefore(eventDto.getEventDate().plusHours(2))) {
            throw new ValidatorException("Only pending or canceled events can be changed");
        }
        event.setEventDate(eventDto.getEventDate());
        event.setPaid(eventDto.isPaid());
        event.setTitle(eventDto.getTitle());
        return event;
    }
}