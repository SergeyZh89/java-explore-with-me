package ru.practicum.event.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.event.model.dto.AdminUpdateEventRequest;
import ru.practicum.event.model.dto.EventFullDto;

import java.util.List;

public interface EventServiceAdmin {
    List<EventFullDto> getEventsByFilter(List<Long> usersId,
                                         List<String> states,
                                         List<Long> categories,
                                         String rangeStart,
                                         String rangeEnd,
                                         Pageable pageable);

    EventFullDto updateEvent(long eventId, AdminUpdateEventRequest adminUpdateEventRequest);

    EventFullDto publishEvent(long eventId);

    EventFullDto rejectEvent(long eventId);
}