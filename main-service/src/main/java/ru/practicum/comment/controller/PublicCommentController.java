package ru.practicum.comment.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.model.dto.CommentFullDto;
import ru.practicum.comment.service.CommentService;

import java.util.List;

/**
 * Контроллер для работы с комментариями в публичной части системы.
 * Реализует методы для получения всех комментариев к событию.
 */
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Slf4j
public class PublicCommentController {

    private final CommentService commentServiceImpl;

    /**
     * Получает все комментарии по идентификатору события.
     *
     * @param eventId идентификатор события
     * @param from количество комментариев, которые нужно пропустить (сдвиг)
     * @param size количество комментариев в наборе
     * @param request объект запроса для логирования
     * @return список всех комментариев для события
     */
    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentFullDto> getAllCommentsByEventIdPublic(@PathVariable long eventId,
                                                              @RequestParam(defaultValue = "0") int from,
                                                              @RequestParam(defaultValue = "10") int size,
                                                              HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Public:Получен запрос на получение комментариев. ID События: {}.", eventId);
        return commentServiceImpl.getAllCommentsByEventIdPublic(eventId, from, size);
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
