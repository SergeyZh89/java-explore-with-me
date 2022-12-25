package ru.practicum.event.service.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.event.controller.client.EventClient;
import ru.practicum.event.exception.EventNotFoundException;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.dto.EndPointHitDto;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.service.EventServicePublic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServicePublicImpl implements EventServicePublic {
    private final EventRepository eventRepository;
    private final EventClient eventClient;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-dd-MM HH:mm:ss");

    public EventServicePublicImpl(EventRepository eventRepository, EventClient eventClient) {
        this.eventRepository = eventRepository;
        this.eventClient = eventClient;
    }

    @Override
    public List<Event> getEventsByFilter(String text,
                                         List<Long> categories,
                                         boolean paid,
                                         String rangeStart,
                                         String rangeEnd,
                                         boolean onlyAvailable,
                                         Pageable pageable,
                                         String clientIp,
                                         String endPointPath) {
        List<Event> eventList;
        if (onlyAvailable) {
            eventList = eventRepository.findAll(pageable).stream()
                    .filter(event -> categories.contains(event.getCategory().getId())
                            && event.getAnnotation().equalsIgnoreCase(text)
                            && event.isPaid() == paid
                            && event.getConfirmedRequests() < event.getParticipantLimit()
                            && rangeStart != null ? event.getEventDate().isAfter(LocalDateTime.parse(rangeStart, FORMATTER)) : event.getEventDate().isAfter(LocalDateTime.now())
                            && rangeEnd != null ? event.getEventDate().isBefore(LocalDateTime.parse(rangeEnd, FORMATTER)) : event.getEventDate().isBefore(LocalDateTime.MAX))
                    .collect(Collectors.toList());
        } else {
            eventList = eventRepository.findAll(pageable).stream()
                    .filter(event -> categories.contains(event.getCategory().getId())
                            && event.getAnnotation().equalsIgnoreCase(text)
                            && event.isPaid() == paid
                            && rangeStart != null ? event.getEventDate().isAfter(LocalDateTime.parse(rangeStart, FORMATTER)) : event.getEventDate().isAfter(LocalDateTime.now())
                            && rangeEnd != null ? event.getEventDate().isBefore(LocalDateTime.parse(rangeEnd, FORMATTER)) : event.getEventDate().isBefore(LocalDateTime.MAX))
                    .collect(Collectors.toList());
        }
        EndPointHitDto endPointHitDto = new EndPointHitDto().toBuilder()
                .ip(clientIp)
                .uri(endPointPath)
                .app("events")
                .build();
        eventClient.addHit(endPointHitDto);
        return eventList;
    }

    @Override
    public Event getEvent(long eventId, String clientIp, String endPointPath) {
        EndPointHitDto endPointHitDto = new EndPointHitDto().toBuilder()
                .ip(clientIp)
                .uri(endPointPath)
                .app("events")
                .build();
        eventClient.addHit(endPointHitDto);
        return eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
    }
}