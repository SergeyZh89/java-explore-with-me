package ru.practicum.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.enums.EventSort;
import ru.practicum.event.model.dto.EventFullDto;
import ru.practicum.event.service.EventServicePublic;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;

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
    public List<EventFullDto> getEventsByFilter(@RequestParam(name = "text", required = false) String text,
                                                @RequestParam(name = "categories", required = false, defaultValue = "") List<Long> categories,
                                                @RequestParam(name = "paid", required = false) boolean paid,
                                                @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                                @RequestParam(name = "rangeEnf", required = false) String rangeEnd,
                                                @RequestParam(name = "onlyAvailable", required = false, defaultValue = "false") boolean onlyAvailable,
                                                @RequestParam(name = "sort", required = false) String sort,
                                                @RequestParam(name = "from", required = false, defaultValue = "0") int from,
                                                @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                                HttpServletRequest request) {
        log.info("Получен запрос на получение событий по фильтру");
        int page = from / size;
        Optional<EventSort> eventSort = EventSort.from(sort);
        Pageable pageable;
        if (eventSort.isPresent()) {
            pageable = PageRequest.of(page, size, Sort.by(eventSort.get().name()).descending());
        } else {
            pageable = PageRequest.of(page, size);
        }
        return eventService.getEventsByFilter(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, pageable, request);
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable @Positive long id, HttpServletRequest request) {
        log.info("Получен запрос на событие {}", id);
        return eventService.getEvent(id, request);
    }
}