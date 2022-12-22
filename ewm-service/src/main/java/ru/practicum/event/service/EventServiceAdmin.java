package ru.practicum.event.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.event.model.Event;
import ru.practicum.request.AdminUpdateEventRequest;

import java.util.List;

public interface EventServiceAdmin {
    List<Event> getEventsByFilter(List<Long> usersId,
                                  List<String> states,
                                  List<Long> categories,
                                  String rangeStart,
                                  String rangeEnd,
                                  Pageable pageable);

    Event updateEvent(long eventId, AdminUpdateEventRequest adminUpdateEventRequest);

    Event publishEvent(long eventId);

    Event rejectEvent(long eventId);
}