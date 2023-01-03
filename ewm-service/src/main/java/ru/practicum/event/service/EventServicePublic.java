package ru.practicum.event.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.event.model.dto.EventFullDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventServicePublic {
    List<EventFullDto> getEventsByFilter(String text,
                                         List<Long> categories,
                                         boolean paid,
                                         String rangeStart,
                                         String rangeEnd,
                                         boolean onlyAvailable,
                                         Pageable pageable,
                                         HttpServletRequest request
    );

    EventFullDto getEvent(long eventId, HttpServletRequest request);
}