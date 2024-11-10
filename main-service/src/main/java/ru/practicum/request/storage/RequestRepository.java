package ru.practicum.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    boolean existsByRequesterIdAndEventId(long userId, long eventId);

    List<Request> findByRequesterId(long userId);

    List<Request> findByEventId(long eventId);

}