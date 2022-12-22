package ru.practicum.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventServiceAdmin;
import ru.practicum.request.AdminUpdateEventRequest;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/events")
@Validated
public class EventControllerAdmin {
    private final EventServiceAdmin eventServiceAdmin;

    @Autowired
    public EventControllerAdmin(EventServiceAdmin eventServiceAdmin) {
        this.eventServiceAdmin = eventServiceAdmin;
    }

    @GetMapping
    public List<Event> getEvents(@RequestParam(name = "users") List<Long> usersId,
                                 @RequestParam(name = "states") List<String> states,
                                 @RequestParam(name = "categories") List<Long> categories,
                                 @RequestParam(name = "rangeStart") String rangeStart,
                                 @RequestParam(name = "rangeEnd") String rangeEnd,
                                 @RequestParam(name = "from", defaultValue = "0") int from,
                                 @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Получен админский запрос на получение событий");
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        return eventServiceAdmin.getEventsByFilter(usersId, states, categories, rangeStart, rangeEnd, pageable);
    }

    @PutMapping("/{eventId}")
    public Event updateEvent(@PathVariable @Positive long eventId,
                             @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        log.info("Получен админский запрос на редактирование события {}", eventId);
        return eventServiceAdmin.updateEvent(eventId, adminUpdateEventRequest);
    }

    @PatchMapping("/{eventId}/publish")
    public Event publishEvent(@PathVariable @Positive long eventId) {
        log.info("Получен админский запрос на публикацию события {}", eventId);
        return eventServiceAdmin.publishEvent(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public Event rejectEvent(@PathVariable @Positive long eventId) {
        log.info("Получен админский запрос на отклонение события {}", eventId);
        return eventServiceAdmin.rejectEvent(eventId);
    }
}