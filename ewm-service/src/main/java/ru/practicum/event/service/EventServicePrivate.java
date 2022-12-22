package ru.practicum.event.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.dto.EventDto;
import ru.practicum.event.model.dto.NewEventDto;
import ru.practicum.request.model.Request;

import java.util.List;

public interface EventServicePrivate {
    List<Event> getEventsByCurrentUser(long userId, Pageable pageable);

    Event updateEventByCurrentUser(long userId, EventDto eventDto);

    Event addNewEvent(long userId, NewEventDto newEventDto);

    Event getEventByCurrentUser(long userId, long eventId);

    Event cancelEventByCurrentUser(long userId, long eventId);

    List<Request> getOtherRequestsByEventAndCurrentUser(long userId, long eventId);

    Request confirmRequest(long userId, long eventId, long reqId);

    Request rejectRequest(long userId, long eventId, long reqId);
}
