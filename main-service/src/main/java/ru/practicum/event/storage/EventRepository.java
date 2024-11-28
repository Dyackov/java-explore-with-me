package ru.practicum.event.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.event.model.Event;

import java.util.List;

/**
 * Репозиторий для работы с сущностью события.
 * Использует JPA и QueryDSL для выполнения запросов.
 */
public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    /**
     * Найти все события по ID категории.
     *
     * @param categoryId Идентификатор категории.
     * @return Список событий, относящихся к данной категории.
     */
    List<Event> findAllByCategoryId(Long categoryId);

}
