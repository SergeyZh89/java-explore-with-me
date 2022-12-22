package ru.practicum.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventServicePublic;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/events")
public class EventControllerPublic {
    private final EventServicePublic eventService;

    @Autowired
    public EventControllerPublic(EventServicePublic eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<Event> getEvents(@RequestParam(name = "text") String text,
                                 @RequestParam(name = "categories") List<Long> categories,
                                 @RequestParam(name = "paid") boolean paid,
                                 @RequestParam(name = "rangeStart") String rangeStart,
                                 @RequestParam(name = "rangeEnf") String rangeEnd,
                                 @RequestParam(name = "onlyAvailable", defaultValue = "false") boolean onlyAvailable,
                                 @RequestParam(name = "sort") String sort,
                                 @RequestParam(name = "from", defaultValue = "0") int from,
                                 @RequestParam(name = "size", defaultValue = "10") int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        return eventService.getEvents(text, categories, paid, rangeStart, rangeEnd,  onlyAvailable, sort, pageable);
    }

    @GetMapping("/{id}")
    public Event getEventById(@PathVariable @Positive long id) {
        return eventService.getEvent(id);
    }
}
