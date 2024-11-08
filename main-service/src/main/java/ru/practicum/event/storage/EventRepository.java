package ru.practicum.event.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findByIdAndInitiatorId(long userId, long eventId);

    List<Event> findAllByInitiatorId(long userId, Pageable pageable);

    List<Event> findAllByCategoryId(Long categoryId);
}
