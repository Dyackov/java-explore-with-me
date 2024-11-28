package ru.practicum.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.request.model.Request;

import java.util.List;

/**
 * Репозиторий для управления запросами на участие.
 */
@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    /**
     * Проверяет существование запроса на участие по идентификатору пользователя и события.
     *
     * @param userId  идентификатор пользователя.
     * @param eventId идентификатор события.
     * @return true, если запрос существует, иначе false.
     */
    boolean existsByRequesterIdAndEventId(long userId, long eventId);

    /**
     * Находит запросы на участие по идентификатору пользователя.
     *
     * @param userId идентификатор пользователя.
     * @return список запросов на участие.
     */
    List<Request> findByRequesterId(long userId);

    /**
     * Находит запросы на участие по идентификатору события.
     *
     * @param eventId идентификатор события.
     * @return список запросов на участие.
     */
    List<Request> findByEventId(long eventId);
}