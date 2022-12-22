package ru.practicum.event.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.event.model.Event;

import java.util.List;

public interface EventServicePublic {
    List<Event> getEvents(String text,
                          List<Long> categories,
                          boolean paid,
                          String rangeStart,
                          String rangeEnd,
                          boolean onlyAvailable,
                          String sort,
                          Pageable pageable
    );

    Event getEvent(long eventId);
}
