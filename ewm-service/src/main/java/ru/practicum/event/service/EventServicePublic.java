package ru.practicum.event.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.event.model.Event;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventServicePublic {
    List<Event> getEventsByFilter(String text,
                                  List<Long> categories,
                                  boolean paid,
                                  String rangeStart,
                                  String rangeEnd,
                                  boolean onlyAvailable,
                                  Pageable pageable,
                                  HttpServletRequest request
    );

    Event getEvent(long eventId, HttpServletRequest request);
}