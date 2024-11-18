package ru.practicum.comment.service;

import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.dto.*;
import ru.practicum.comment.model.enums.StatusComment;
import ru.practicum.error.exception.AuthorizationException;
import ru.practicum.error.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для работы с комментариями.
 * Предоставляет методы для создания, получения, обновления и удаления комментариев,
 * а также для получения списка комментариев с фильтрацией.
 */
public interface CommentService {

    /**
     * Создает новый комментарий для пользователя на событие.
     *
     * @param userId        ID пользователя, который создает комментарий
     * @param eventId       ID события, к которому добавляется комментарий
     * @param newCommentDto DTO объекта с данными нового комментария
     * @return DTO полного комментария
     */
    CommentFullDto createCommentPrivate(Long userId, Long eventId, NewCommentDto newCommentDto);

    /**
     * Получает комментарий по его ID для частного пользователя.
     *
     * @param userId    ID пользователя, запрашивающего комментарий
     * @param commentId ID комментария
     * @return DTO полного комментария
     */
    CommentFullDto getCommentByIdPrivate(Long userId, Long commentId);

    /**
     * Получает список комментариев пользователя с пагинацией.
     *
     * @param userId ID пользователя
     * @param from   Количество элементов, которые нужно пропустить
     * @param size   Количество элементов на странице
     * @return Список DTO комментариев
     */
    List<CommentDto> getCommentsByUserIdPrivate(long userId, int from, int size);

    /**
     * Обновляет комментарий пользователя.
     *
     * @param userId           ID пользователя, который обновляет комментарий
     * @param commentId        ID комментария
     * @param updateCommentDto DTO объекта с обновленными данными комментария
     * @return DTO полного обновленного комментария
     */
    CommentFullDto updateCommentPrivate(long userId, long commentId, UpdateCommentDto updateCommentDto);

    /**
     * Получает все комментарии для события с пагинацией (публичный доступ).
     *
     * @param eventId ID события
     * @param from    Количество элементов, которые нужно пропустить
     * @param size    Количество элементов на странице
     * @return Список DTO комментариев
     */
    List<CommentFullDto> getAllCommentsByEventIdPublic(long eventId, int from, int size);

    /**
     * Удаляет комментарий администратором по его ID.
     *
     * @param commentId ID комментария
     */
    void deleteCommentAdmin(long commentId);

    /**
     * Обновляет статус комментария администратором.
     *
     * @param commentId              ID комментария
     * @param updateStatusCommentAdmin DTO с обновленным статусом комментария
     * @return DTO полного комментария с обновленным статусом
     */
    CommentFullDto updateStatusCommentAdmin(long commentId, UpdateStatusCommentAdmin updateStatusCommentAdmin);

    /**
     * Получает комментарии с фильтрацией для администраторов.
     *
     * @param commentIds   Список ID комментариев для фильтрации
     * @param text         Текст для поиска в комментариях
     * @param commentatorIds Список ID комментаторов для фильтрации
     * @param eventIds     Список ID событий для фильтрации
     * @param rangeStart   Дата и время начала диапазона для фильтрации по времени
     * @param rangeEnd     Дата и время конца диапазона для фильтрации по времени
     * @param status       Статус комментария для фильтрации
     * @param from         Количество элементов, которые нужно пропустить
     * @param size         Количество элементов на странице
     * @return Список DTO полных комментариев
     */
    List<CommentFullDto> getCommentBySearchAdmins(List<Long> commentIds, String text, List<Long> commentatorIds,
                                                  List<Long> eventIds, LocalDateTime rangeStart,
                                                  LocalDateTime rangeEnd, StatusComment status, int from, int size);

    /**
     * Получает комментарий по его ID или выбрасывает исключение, если комментарий не найден.
     *
     * @param commentId ID комментария
     * @return Комментарий
     * @throws NotFoundException Если комментарий с данным ID не найден
     */
    Comment getCommentByIdOrThrow(long commentId);

    /**
     * Получает комментарий и проверяет авторизацию пользователя для доступа к этому комментарию.
     *
     * @param userId    ID пользователя, пытающегося получить комментарий
     * @param commentId ID комментария
     * @return Комментарий
     * @throws AuthorizationException Если пользователь не является владельцем комментария
     */
    Comment getCommentAndCheckAuthorization(long userId, long commentId);
}
