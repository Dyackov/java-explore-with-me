package ru.practicum.comment.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.model.dto.CommentDto;
import ru.practicum.comment.model.dto.CommentFullDto;
import ru.practicum.comment.model.dto.NewCommentDto;
import ru.practicum.comment.model.dto.UpdateCommentDto;
import ru.practicum.comment.service.CommentService;

import java.util.List;

/**
 * Контроллер для работы с комментариями в пользовательской части системы.
 * Реализует методы для добавления, получения и обновления комментариев пользователем.
 */
@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
@Slf4j
public class PrivateCommentController {

    private final CommentService commentServiceImpl;

    /**
     * Добавляет новый комментарий для пользователя на событие.
     *
     * @param userId идентификатор пользователя
     * @param eventId идентификатор события
     * @param newCommentDto DTO для создания нового комментария
     * @param request объект запроса для логирования
     * @return полный комментарий после создания
     */
    @PostMapping("/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentFullDto createCommentPrivate(@PathVariable long userId,
                                               @PathVariable long eventId,
                                               @RequestBody @Valid NewCommentDto newCommentDto,
                                               HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Private:Получен запрос на добавление нового комментария. ID Пользователя: {}, ID События: {},\n{}",
                userId, eventId, newCommentDto);
        return commentServiceImpl.createCommentPrivate(userId, eventId, newCommentDto);
    }

    /**
     * Получает полную информацию о комментарии по его ID.
     *
     * @param userId идентификатор пользователя
     * @param commentId идентификатор комментария
     * @param request объект запроса для логирования
     * @return полный комментарий
     */
    @GetMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentFullDto getCommentByIdPrivate(@PathVariable long userId,
                                                @PathVariable long commentId,
                                                HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Private:Получен запрос на получение полной информации о комментарии. ID пользователя: {}, ID Комментария: {}",
                userId, commentId);
        return commentServiceImpl.getCommentByIdPrivate(userId, commentId);
    }

    /**
     * Получает список всех комментариев пользователя.
     *
     * @param userId идентификатор пользователя
     * @param from количество комментариев, которые нужно пропустить (сдвиг)
     * @param size количество комментариев в наборе
     * @param request объект запроса для логирования
     * @return список комментариев пользователя
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getCommentsByUserIdPrivate(@PathVariable long userId,
                                                       @RequestParam(defaultValue = "0") int from,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Private:Получен запрос на получение всех комментариев пользователя. ID пользователя:{}, from:{}, size:{}",
                userId, from, size);
        return commentServiceImpl.getCommentsByUserIdPrivate(userId, from, size);
    }

    /**
     * Обновляет комментарий пользователя.
     *
     * @param userId идентификатор пользователя
     * @param commentId идентификатор комментария
     * @param updateCommentDto DTO для обновления комментария
     * @param request объект запроса для логирования
     * @return обновленный комментарий
     */
    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentFullDto updateCommentPrivate(@PathVariable long userId,
                                               @PathVariable long commentId,
                                               @RequestBody @Valid UpdateCommentDto updateCommentDto,
                                               HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Private:Получен запрос на обновление комментария. ID Пользователя: {}, ID Комментария: {}.\n{}",
                userId, commentId, updateCommentDto);
        return commentServiceImpl.updateCommentPrivate(userId, commentId, updateCommentDto);
    }

    /**
     * Логирует детали запроса: метод, URL и параметры.
     *
     * @param request объект запроса
     */
    private void logRequestDetails(HttpServletRequest request) {
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        String params = queryString != null ? "?" + queryString : "";
        log.info("{} {}{}", method, url, params);
    }
}