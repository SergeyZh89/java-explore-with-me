package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import ru.practicum.event.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepositoryCustom {
    List<Event> findEventsByInitiator_Id(long userId, Pageable pageable);

    Optional<Event> findEventByIdAndInitiator_Id(long eventId, long userId);
}
