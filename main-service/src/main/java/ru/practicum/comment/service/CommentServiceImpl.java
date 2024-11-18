package ru.practicum.comment.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.dto.*;
import ru.practicum.comment.model.enums.StatusComment;
import ru.practicum.comment.model.mapper.CommentMapper;
import ru.practicum.comment.storage.CommentRepository;
import ru.practicum.error.exception.AuthorizationException;
import ru.practicum.error.exception.DataTimeException;
import ru.practicum.error.exception.NotFoundException;
import ru.practicum.error.exception.StateValidateException;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.enums.State;
import ru.practicum.event.service.EventService;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static ru.practicum.comment.model.QComment.comment;

/**
 * Сервис для работы с комментариями.
 * Реализует методы для создания, получения, обновления и удаления комментариев как для пользователей, так и для администраторов.
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserService userServiceImpl;
    private final EventService eventServiceImpl;
    private final CommentMapper commentMapper;

    /**
     * Создает новый комментарий для события.
     *
     * @param userId ID пользователя, который оставляет комментарий
     * @param eventId ID события, к которому оставляется комментарий
     * @param newCommentDto Данные нового комментария
     * @return Полная информация о созданном комментарии
     * @throws StateValidateException Если событие не опубликовано
     */
    public CommentFullDto createCommentPrivate(Long userId, Long eventId, NewCommentDto newCommentDto) {
        log.debug("Private:Создание комментария: ID Пользователя: {}, ID События: {},\n{}", userId, eventId, newCommentDto);
        User commentator = userServiceImpl.getUserByIdOrThrow(userId);
        Event event = eventServiceImpl.getEventByIdOrThrow(eventId);
        if (event.getState() != State.PUBLISHED) {
            throw new StateValidateException("Комментарий можно оставить только на опубликованное событие");
        }
        Comment comment = commentMapper.toComment(newCommentDto);
        comment.setCommentator(commentator);
        comment.setEvent(event);
        comment.setCreated(LocalDateTime.now());
        comment.setStatus(StatusComment.PENDING);
        commentRepository.save(comment);
        log.info("Private:Создан комментарий:\n{}", comment);
        return commentMapper.toCommentFullDto(comment);
    }

    /**
     * Получает полный комментарий по его ID для пользователя.
     *
     * @param userId ID пользователя
     * @param commentId ID комментария
     * @return Полная информация о комментарии
     */
    @Override
    public CommentFullDto getCommentByIdPrivate(Long userId, Long commentId) {
        log.debug("Private:Получение полной информации о комментарии. ID пользователя: {}, ID Комментария: {}", userId, commentId);
        userServiceImpl.getUserByIdOrThrow(userId);
        Comment comment = getCommentAndCheckAuthorization(userId, commentId);
        log.info("Private:Получен комментарий:\n{}", comment);
        return commentMapper.toCommentFullDto(comment);
    }

    /**
     * Получает все комментарии пользователя.
     *
     * @param userId ID пользователя
     * @param from Индекс первого комментария в выборке
     * @param size Количество комментариев в выборке
     * @return Список комментариев
     */
    @Override
    public List<CommentDto> getCommentsByUserIdPrivate(long userId, int from, int size) {
        log.debug("Private:Получение всех комментариев. ID пользователя:{}, from:{}, size:{}", userId, from, size);
        userServiceImpl.getUserByIdOrThrow(userId);
        Pageable pageable = createPageable(from, size, Sort.by(Sort.Direction.ASC, "created"));
        List<Comment> comments = commentRepository.findAll(pageable).getContent();
        log.info("Private:Получен список всех комментариев пользователя. ID пользователя:{}\n{}", userId, comments);
        return comments.stream().map(commentMapper::toCommentDto).toList();
    }

    /**
     * Обновляет комментарий пользователя.
     *
     * @param userId ID пользователя
     * @param commentId ID комментария
     * @param updateCommentDto Данные для обновления комментария
     * @return Полная информация о обновленном комментарии
     * @throws StateValidateException Если комментарий опубликован
     */
    @Override
    public CommentFullDto updateCommentPrivate(long userId, long commentId, UpdateCommentDto updateCommentDto) {
        log.debug("Обновление комментария ID: {}\n{}", commentId, updateCommentDto);
        userServiceImpl.getUserByIdOrThrow(userId);
        Comment comment = getCommentAndCheckAuthorization(userId, commentId);
        if (comment.getStatus().equals(StatusComment.PUBLISHED)) {
            throw new StateValidateException("Можно изменить только ожидающие или отмененные комментарии");
        }
        Optional.ofNullable(updateCommentDto.getText()).ifPresent(comment::setText);
        log.info("Комментарий обновлён:\n{}", comment);
        return commentMapper.toCommentFullDto(comment);
    }

    /**
     * Получает все опубликованные комментарии для события.
     *
     * @param eventId ID события
     * @param from Индекс первого комментария в выборке
     * @param size Количество комментариев в выборке
     * @return Список комментариев
     */
    @Override
    public List<CommentFullDto> getAllCommentsByEventIdPublic(long eventId, int from, int size) {
        log.debug("Public:Получен запрос на получение комментариев. ID События: {}.", eventId);
        eventServiceImpl.getEventByIdOrThrow(eventId);
        Pageable pageable = createPageable(from, size, Sort.by(Sort.Direction.ASC, "created"));
        List<Comment> comments = commentRepository.findByEventIdAndStatus(eventId, StatusComment.PUBLISHED, pageable);
        log.info("Public:Получен список комментариев. ID События: {}\n{}", eventId, comments);
        return comments.stream().map(commentMapper::toCommentFullDto).toList();
    }

    /**
     * Удаляет комментарий по ID администратора.
     *
     * @param commentId ID комментария
     */
    @Override
    public void deleteCommentAdmin(long commentId) {
        log.info("Admin:Удаление комментария. ID Комментария: {}.", commentId);
        getCommentByIdOrThrow(commentId);
        commentRepository.deleteById(commentId);
        log.info("Admin:Комментарий удалён. ID Комментария: {}.", commentId);
    }

    /**
     * Обновляет статус комментария администратором.
     *
     * @param commentId ID комментария
     * @param updateStatusCommentAdmin Данные для обновления статуса
     * @return Полная информация о обновленном комментарии
     */
    @Override
    public CommentFullDto updateStatusCommentAdmin(long commentId, UpdateStatusCommentAdmin updateStatusCommentAdmin) {
        log.debug("Admin:Обновление статуса комментария.ID Комментария: {}.", commentId);
        Comment comment = getCommentByIdOrThrow(commentId);
        comment.setStatus(updateStatusCommentAdmin.getStatus());
        Comment updatedComment = commentRepository.save(comment);
        log.info("Admin:Обновлён статус комментария.ID Комментария: {}, Статус: {}", commentId, updatedComment.getStatus());
        return commentMapper.toCommentFullDto(comment);
    }

    /**
     * Получает список комментариев с фильтрацией для администраторов.
     *
     * @param commentIds Список ID комментариев
     * @param text Текст для поиска в комментариях
     * @param commentatorIds Список ID пользователей-комментаторов
     * @param eventIds Список ID событий
     * @param rangeStart Начало временного диапазона
     * @param rangeEnd Конец временного диапазона
     * @param status Статус комментария
     * @param from Индекс первого комментария в выборке
     * @param size Количество комментариев в выборке
     * @return Список комментариев с фильтрацией
     * @throws DataTimeException Если время начала позже времени окончания
     */

    @Override
    public List<CommentFullDto> getCommentBySearchAdmins(List<Long> commentIds, String text, List<Long> commentatorIds,
                                                         List<Long> eventIds, LocalDateTime rangeStart,
                                                         LocalDateTime rangeEnd, StatusComment status, int from, int size) {
        log.debug("""
                Admin:Получение комментариев с фильтрацией.
                Список идентификаторов комментариев: {}
                Текст для поиска: {}
                Список идентификаторов комментаторов: {}
                Список идентификаторов событий: {}
                Дата и время не раньше которых создан комментарий: {}
                Дата и время не позже которых создан комментарий: {}
                Статус комментария: {}status,
                Количество событий, которые нужно пропустить: {}
                Количество событий в наборе: {}
                """, commentIds, text, commentatorIds, eventIds, rangeStart, rangeEnd, status, from, size);
        Page<Comment> commetPage;
        Pageable pageable = createPageable(from, size, Sort.by(Sort.Direction.ASC, "id"));

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (commentIds != null && !commentIds.isEmpty()) {
            booleanBuilder.and(comment.id.in(commentIds));
        }
        if (text != null && !text.isEmpty()) {
            booleanBuilder.and(comment.text.contains(text));
        }
        if (commentatorIds != null && !commentatorIds.isEmpty()) {
            booleanBuilder.and(comment.commentator.id.in(commentatorIds));
        }
        if (eventIds != null && !eventIds.isEmpty()) {
            booleanBuilder.and(comment.event.id.in(eventIds));
        }
        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new DataTimeException("Время начала должно быть не позже времени окончания.");
            }
            booleanBuilder.and(comment.created.between(rangeStart, rangeEnd));
        } else if (rangeStart == null && rangeEnd != null) {
            booleanBuilder.and(comment.created.before(rangeEnd));
        } else if (rangeStart != null) {
            booleanBuilder.and(comment.created.after(rangeStart));
        }
        if (status != null) {
            booleanBuilder.and(comment.status.in(status));
        }

        if (booleanBuilder.getValue() != null) {
            commetPage = commentRepository.findAll(booleanBuilder, pageable);
        } else {
            commetPage = commentRepository.findAll(pageable);
        }
        List<Comment> comments = commetPage.getContent();
        log.info("Public:Получен список комментариев:\n{}", comments);
        return comments.stream().map(commentMapper::toCommentFullDto).toList();
    }

    /**
     * Получает комментарий по его идентификатору или выбрасывает исключение, если комментарий не найден.
     *
     * @param commentId Идентификатор комментария.
     * @return Объект комментария.
     * @throws NotFoundException Если комментарий с указанным идентификатором не найден.
     */

    @Override
    public Comment getCommentByIdOrThrow(long commentId) {
        log.info("Попытка получения Комментария по ID: {}", commentId);
        return commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException(
                "Comment with id = " + commentId + " was not found"));
    }

    /**
     * Проверяет, является ли пользователь владельцем комментария.
     *
     * @param userId Идентификатор пользователя, для которого проверяется авторизация.
     * @param commentId Идентификатор комментария, для которого проверяется авторизация.
     * @return Комментарий, если пользователь является его владельцем.
     * @throws AuthorizationException Если пользователь не является владельцем комментария.
     * @throws NotFoundException Если комментарий с указанным идентификатором не найден.
     */

    @Override
    public Comment getCommentAndCheckAuthorization(long userId, long commentId) {
        log.info("Проверка авторизации, Пользователь ID: {}, Комментарий ID: {}", userId, commentId);
        Comment comment = getCommentByIdOrThrow(commentId);
        if (comment.getCommentator().getId() != userId) {
            throw new AuthorizationException("Пользователь ID: " + userId +
                    " не является владельцем комментария ID: " + commentId);
        }
        return comment;
    }

    /**
     * Создает объект Pageable для пагинации.
     *
     * @param from Количество элементов, которые нужно пропустить (для пагинации).
     * @param size Количество элементов, которое нужно вернуть (для пагинации).
     * @param sort Параметры сортировки.
     * @return Объект Pageable для дальнейшего использования в запросах к базе данных.
     */

    private Pageable createPageable(int from, int size, Sort sort) {
        log.debug("Create Pageable with offset from {}, size {}", from, size);
        return PageRequest.of(from / size, size, sort);
    }
}
