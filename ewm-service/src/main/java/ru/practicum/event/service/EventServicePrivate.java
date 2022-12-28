package ru.practicum.event.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.event.model.dto.EventFullDto;
import ru.practicum.event.model.dto.NewEventDto;
import ru.practicum.event.model.dto.UpdateEventRequest;
import ru.practicum.request.model.dto.RequestDto;

import java.util.List;

public interface EventServicePrivate {
    List<EventFullDto> getEventsByCurrentUser(long userId, Pageable pageable);

    EventFullDto updateEventByCurrentUser(long userId, UpdateEventRequest updateEventRequest);

    EventFullDto addNewEvent(long userId, NewEventDto newEventDto);

    EventFullDto getEventByCurrentUser(long userId, long eventId);

    EventFullDto cancelEventByCurrentUser(long userId, long eventId);

    List<RequestDto> getOtherRequestsByEventAndCurrentUser(long userId, long eventId);

    RequestDto confirmRequest(long userId, long eventId, long reqId);

    RequestDto rejectRequest(long userId, long eventId, long reqId);
}