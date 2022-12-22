package ru.practicum.event.service.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.service.EventServicePublic;

import java.util.List;

@Service
public class EventServicePublicImpl implements EventServicePublic {
    private final EventRepository eventRepository;

    public EventServicePublicImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Event> getEvents(String text,
                                 List<Long> categories,
                                 boolean paid,
                                 String rangeStart,
                                 String rangeEnd,
                                 boolean onlyAvailable,
                                 String sort,
                                 Pageable pageable) {
        return null;
    }

    @Override
    public Event getEvent(long eventId) {
        return eventRepository.findById(eventId).orElseThrow();
    }
}
