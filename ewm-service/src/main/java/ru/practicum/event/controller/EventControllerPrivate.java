package ru.practicum.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.dto.EventDto;
import ru.practicum.event.model.dto.NewEventDto;
import ru.practicum.event.service.EventServicePrivate;
import ru.practicum.request.model.Request;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/events")
@Validated
public class EventControllerPrivate {
    private final EventServicePrivate eventServicePrivate;

    public EventControllerPrivate(EventServicePrivate eventServicePrivate) {
        this.eventServicePrivate = eventServicePrivate;
    }

    @GetMapping
    public List<Event> getEventsCurrentUser(@PathVariable @Positive long userId,
                                            @RequestParam(name = "from", defaultValue = "0") int from,
                                            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Получен запрос на список событий от пользователя {}", userId);
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        return eventServicePrivate.getEventsByCurrentUser(userId, pageable);
    }

    @PatchMapping
    public Event updateEventByCurrentUser(@PathVariable @Positive long userId,
                                          @RequestBody @Valid EventDto eventDto) {
        log.info("Получен запрос на обновление события {} от пользователя {}", eventDto.getEventId(), userId);
        return eventServicePrivate.updateEventByCurrentUser(userId, eventDto);
    }

    @PostMapping
    public Event addNewEvent(@PathVariable @Positive long userId, @RequestBody @Valid NewEventDto newEventDto) {
        log.info("Получен запрос на добавление события от пользователя {}", userId);
        return eventServicePrivate.addNewEvent(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public Event getEventByCurrentUser(@PathVariable @Positive long userId, @PathVariable @Positive long eventId) {
        log.info("Получен запрос на событиe {} от пользователя {}", eventId, userId);
        return eventServicePrivate.getEventByCurrentUser(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public Event cancelEventByCurrentUser(@PathVariable @Positive long userId,
                                          @PathVariable @Positive long eventId) {
        log.info("Получен запрос на отмену события {} от пользователя {}", eventId, userId);
        return eventServicePrivate.cancelEventByCurrentUser(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<Request> getOtherRequestsByEventAndCurrentUser(@PathVariable @Positive long userId,
                                                               @PathVariable @Positive long eventId) {
        log.info("Получен запрос на информацию о запросах на участие в событии {} пользователя {}", eventId, userId);
        return eventServicePrivate.getOtherRequestsByEventAndCurrentUser(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public Request confirmOtherRequestByEventByCurrentUser(@PathVariable @Positive long userId,
                                                           @PathVariable @Positive long eventId,
                                                           @PathVariable @Positive long reqId) {
        log.info("Получен запрос на подтверждение чужой заявки на участие в событии {} текущего пользователя {}", eventId, userId);
        return eventServicePrivate.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public Request rejectOtherRequestByEventByCurrentUser(@PathVariable @Positive long userId,
                                                          @PathVariable @Positive long eventId,
                                                          @PathVariable @Positive long reqId) {
        log.info("Получен запрос на отклонение чужой заявки на участие в событии {} текущего пользователя {}", eventId, userId);
        return eventServicePrivate.rejectRequest(userId, eventId, reqId);
    }
}