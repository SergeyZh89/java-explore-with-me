package ru.practicum.event.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.event.model.Event;

import java.util.List;

public interface EventServicePublic {
    List<Event> getEventsByFilter(String text,
                                  List<Long> categories,
                                  boolean paid,
                                  String rangeStart,
                                  String rangeEnd,
                                  boolean onlyAvailable,
                                  Pageable pageable,
                                  String clientIp,
                                  String endPointPath
    );

    Event getEvent(long eventId, String clientIp, String endPointPath);
}
