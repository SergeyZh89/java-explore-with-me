package ru.practicum.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.enums.EventSort;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventServicePublic;

import javax.servlet.http.HttpServletRequest;
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
    public List<Event> getEventsByFilter(@RequestParam(name = "text", required = false) String text,
                                         @RequestParam(name = "categories", required = false, defaultValue = "new List<Long>") List<Long> categories,
                                         @RequestParam(name = "paid", required = false) boolean paid,
                                         @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                         @RequestParam(name = "rangeEnf", required = false) String rangeEnd,
                                         @RequestParam(name = "onlyAvailable", required = false, defaultValue = "false") boolean onlyAvailable,
                                         @RequestParam(name = "sort", required = false) String sort,
                                         @RequestParam(name = "from", required = false, defaultValue = "0") int from,
                                         @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                         HttpServletRequest request) {
        log.info("Получен запрос на получение событий по фильтру");
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        String clientIp = request.getRemoteAddr();
        String endPointPath = request.getRequestURI();
        int page = from / size;
        Pageable pageable;
        if (sort != null) {
            String sortFound = EventSort.getSort(sort);
            pageable = PageRequest.of(page, size, Sort.by(sortFound).descending());
        } else {
            pageable = PageRequest.of(page, size);
        }
        return eventService.getEventsByFilter(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, pageable, clientIp, endPointPath);
    }

    @GetMapping("/{id}")
    public Event getEventById(@PathVariable @Positive long id, HttpServletRequest request) {
        log.info("Получен запрос на событие {}", id);
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        String clientIp = request.getRemoteAddr();
        String endPointPath = request.getRequestURI();
        return eventService.getEvent(id, clientIp, endPointPath);
    }
}
