package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findRequestsByRequester(long userId);

    List<Request> findRequestsByEvent(long eventId);

    Optional<Request> findRequestByRequester(long userId);

    Optional<Request> findRequestByEventAndRequester(long eventId, long userId);
}
