package ru.practicum.event.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.exception.CategoryNotFoundException;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.exception.EventNotFoundException;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.service.EventServiceAdmin;
import ru.practicum.mappers.DateTimeMapper;
import ru.practicum.request.AdminUpdateEventRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceAdminImpl implements EventServiceAdmin {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public EventServiceAdminImpl(EventRepository eventRepository,
                                 CategoryRepository categoryRepository) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Event> getEventsByFilter(List<Long> usersId, List<String> states, List<Long> categories, String rangeStart, String rangeEnd, Pageable pageable) {
        return eventRepository.findAll(pageable).stream()
                .filter(event -> usersId.contains(event.getInitiator().getId())
                        && states.contains(event.getState().name())
                        && categories.contains(event.getCategory().getId())
                        && rangeStart != null ? event.getEventDate().isAfter(DateTimeMapper.INSTANCE.toTime(rangeStart)) : event.getEventDate().isAfter(LocalDateTime.now())
                        && rangeEnd != null ? event.getEventDate().isBefore(DateTimeMapper.INSTANCE.toTime(rangeEnd)) : event.getEventDate().isBefore(LocalDateTime.MAX))
                .collect(Collectors.toList());
    }

    @Override
    public Event updateEvent(long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        event.setAnnotation(adminUpdateEventRequest.getAnnotation());
        Category category = categoryRepository.findById(adminUpdateEventRequest.getCategory())
                .orElseThrow(() -> new CategoryNotFoundException(adminUpdateEventRequest.getCategory()));
        event.setCategory(category);
        event.setDescription(adminUpdateEventRequest.getDescription());
        event.setEventDate(adminUpdateEventRequest.getEventDate());
        event.setLocation(adminUpdateEventRequest.getLocation());
        event.setPaid(adminUpdateEventRequest.isPaid());
        event.setParticipantLimit(adminUpdateEventRequest.getParticipantLimit());
        event.setRequestModeration(adminUpdateEventRequest.isRequestModeration());
        event.setTitle(adminUpdateEventRequest.getTitle());
        return eventRepository.save(event);
    }

    @Override
    public Event publishEvent(long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        event.setState(EventState.PUBLISHED);
        return eventRepository.save(event);
    }

    @Override
    public Event rejectEvent(long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        event.setState(EventState.CANCELED);
        return eventRepository.save(event);
    }
}