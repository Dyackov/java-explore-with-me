package ru.practicum.event.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.enums.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    Optional<Event> findByIdAndInitiatorId(long userId, long eventId);

    List<Event> findAllByInitiatorId(long userId, Pageable pageable);

    List<Event> findAllByCategoryId(Long categoryId);

    @Query("""
            SELECT e
            FROM Event e
            WHERE (:users IS NULL OR e.initiator.id IN :users)
            AND (:states IS NULL OR e.state IN :states)
            AND (:categories IS NULL OR e.category.id IN :categories)
            AND (:rangeStart IS NULL OR e.eventDate >= :rangeStart)
            AND (:rangeEnd IS NULL OR e.eventDate <= :rangeEnd)
            """)
    List<Event> findEvents(@Param("users") List<Long> users,
                           @Param("states") List<State> states,
                           @Param("categories") List<Long> categories,
                           @Param("rangeStart") LocalDateTime rangeStart,
                           @Param("rangeEnd") LocalDateTime rangeEnd,
                           Pageable pageable);

//    @Query("""
//            SELECT e
//            FROM Event e
//            WHERE (e.state = :state)
//            AND (:text IS NULL OR e.annotation ILIKE  %:text% OR e.description ILIKE  %:text%)
//            AND (:categories IS NULL OR e.category.id IN :categories)
//            AND (:paid = e.paid)
//            AND (:rangeStart IS NULL AND e.eventDate > CURRENT_TIMESTAMP OR e.eventDate >= COALESCE(:rangeStart, CURRENT_TIMESTAMP))
//            AND (:rangeEnd IS NULL OR e.eventDate <= :rangeEnd)
//
//            """)
//    List<Event> getAllEventsPublic(State state,String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
//                                   LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, int from, int size);
}
