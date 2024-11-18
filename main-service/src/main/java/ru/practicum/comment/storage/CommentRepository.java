package ru.practicum.comment.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.enums.StatusComment;

import java.util.List;

/**
 * Репозиторий для работы с сущностью {@link Comment}.
 * Включает стандартные операции CRUD и методы для поиска комментариев с фильтрацией.
 */
public interface CommentRepository extends JpaRepository<Comment, Long>, QuerydslPredicateExecutor<Comment> {

    /**
     * Находит комментарии для конкретного события с фильтрацией по статусу.
     *
     * @param eventId  ID события
     * @param published Статус комментария (например, опубликован)
     * @param pageable Объект для пагинации
     * @return Список комментариев, соответствующих фильтрам
     */
    List<Comment> findByEventIdAndStatus(long eventId, StatusComment published, Pageable pageable);
}
