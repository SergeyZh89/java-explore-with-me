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
import ru.practicum.event.model.dto.AdminUpdateEventRequest;
import ru.practicum.event.model.dto.EventFullDto;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.service.EventServiceAdmin;
import ru.practicum.mappers.DateTimeMapper;
import ru.practicum.mappers.EventMapper;

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
    public List<EventFullDto> getEventsByFilter(List<Long> usersId, List<String> states, List<Long> categories, String rangeStart, String rangeEnd, Pageable pageable) {
        return eventRepository.findAll(pageable).stream()
                .filter(event -> usersId.contains(event.getInitiator().getId())
                        && states.contains(event.getState().name())
                        && categories.contains(event.getCategory().getId())
                        && rangeStart != null ? event.getEventDate().isAfter(DateTimeMapper.INSTANCE.toTime(rangeStart)) : event.getEventDate().isAfter(LocalDateTime.now())
                        && rangeEnd != null ? event.getEventDate().isBefore(DateTimeMapper.INSTANCE.toTime(rangeEnd)) : event.getEventDate().isBefore(LocalDateTime.MAX))
                .collect(Collectors.toList()).stream().map(EventMapper.INSTANCE::toFullEvent)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEvent(long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        event.setAnnotation(adminUpdateEventRequest.getAnnotation());
        Category category = categoryRepository.findById(adminUpdateEventRequest.getCategory())
                .orElseThrow(() -> new CategoryNotFoundException(adminUpdateEventRequest.getCategory()));
        event = EventMapper.INSTANCE.partialUpdate(adminUpdateEventRequest, event);

        event.setCategory(category);
        return EventMapper.INSTANCE.toFullEvent(eventRepository.save(event));
    }

    @Override
    public EventFullDto publishEvent(long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        event.setState(EventState.PUBLISHED);
        return EventMapper.INSTANCE.toFullEvent(eventRepository.save(event));
    }

    @Override
    public EventFullDto rejectEvent(long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        event.setState(EventState.CANCELED);
        return EventMapper.INSTANCE.toFullEvent(eventRepository.save(event));
    }
}