package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.model.Event;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {

//    List<Event> findEventsByFilters(String text,
//                                    List<Long> categories,
//                                    boolean paid,
//                                    String rangeStart,
//                                    String rangeEnd,
//                                    boolean onlyAvailable,
//                                    String sort,
//                                    Pageable pageable);
}