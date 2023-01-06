package ru.practicum.event.service.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.comment.model.dto.CommentDto;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.event.controller.client.EventClient;
import ru.practicum.event.exception.EventNotFoundException;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.dto.EndPointHitDto;
import ru.practicum.event.model.dto.EventFullDto;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.service.EventServicePublic;
import ru.practicum.mappers.CommentMapper;
import ru.practicum.mappers.DateTimeMapper;
import ru.practicum.mappers.EventMapper;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServicePublicImpl implements EventServicePublic {
    private final EventRepository eventRepository;
    private final EventClient eventClient;
    private final CommentRepository commentRepository;

    public EventServicePublicImpl(EventRepository eventRepository, EventClient eventClient,
                                  CommentRepository commentRepository) {
        this.eventRepository = eventRepository;
        this.eventClient = eventClient;
        this.commentRepository = commentRepository;
    }

    @Override
    public List<EventFullDto> getEventsByFilter(String text,
                                                List<Long> categories,
                                                boolean paid,
                                                String rangeStart,
                                                String rangeEnd,
                                                boolean onlyAvailable,
                                                Pageable pageable,
                                                HttpServletRequest request) {

        List<Event> eventList;
        LocalDateTime startDate;
        LocalDateTime endDate;

        if (rangeStart == null) {
            startDate = LocalDateTime.now();
        } else {
            startDate = DateTimeMapper.INSTANCE.toTime(rangeStart);
        }
        if (rangeEnd == null) {
            endDate = LocalDateTime.MAX;
        } else {
            endDate = DateTimeMapper.INSTANCE.toTime(rangeEnd);
        }
        
        eventList = eventRepository.findAll().stream()
                .filter(event -> categories.contains(event.getCategory().getId())
                        && event.getAnnotation().equalsIgnoreCase(text)
                        && event.isPaid() == paid
                        && onlyAvailable ? event.getConfirmedRequests() < event.getParticipantLimit() : event.getEventDate().isAfter(startDate)
                        && event.getEventDate().isBefore(endDate))
                .collect(Collectors.toList());

        EndPointHitDto endPointHitDto = new EndPointHitDto().toBuilder()
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .app("ewm-main-service")
                .build();
        eventClient.addHit(endPointHitDto);
        return eventList.stream().map(EventMapper.INSTANCE::toFullEvent)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEvent(long eventId, HttpServletRequest request) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        EndPointHitDto endPointHitDto = new EndPointHitDto().toBuilder()
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .app("ewm-main-service")
                .build();
        eventClient.addHit(endPointHitDto);
        long views = event.getViews() + 1;
        event.setViews(views);
        List<CommentDto> commentList = commentRepository.findAll().stream()
                .map(CommentMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
        EventFullDto eventFullDto = EventMapper.INSTANCE.toFullEvent(event);
        eventFullDto.setComments(commentList);
        return eventFullDto;
    }
}