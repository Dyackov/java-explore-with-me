package ru.practicum.comment.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.model.dto.CommentFullDto;
import ru.practicum.comment.model.dto.UpdateStatusCommentAdmin;
import ru.practicum.comment.model.enums.StatusComment;
import ru.practicum.comment.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
@Slf4j
public class AdminCommentController {
    private final CommentService commentServiceImpl;

    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentFullDto updateStatusCommentAdmin(@PathVariable long commentId,
                                                   @RequestBody @Valid UpdateStatusCommentAdmin updateStatusCommentAdmin,
                                                   HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Admin:Получен запрос на обновление комментария.ID Комментария: {}.", commentId);
        return commentServiceImpl.updateStatusCommentAdmin(commentId, updateStatusCommentAdmin);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCommentAdmin(@PathVariable long commentId,
                            HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Private:Получен запрос на удаление комментария.  ID Комментария: {}.", commentId);
        commentServiceImpl.deleteCommentAdmin(commentId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentFullDto> getCommentBySearchAdmins(@RequestParam(required = false) List<Long> commentIds,
                                                         @RequestParam(required = false) String text,
                                                         @RequestParam(required = false) List<Long> commentatorIds,
                                                         @RequestParam(required = false) List<Long> eventIds,
                                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                         @RequestParam(required = false) LocalDateTime rangeStart,
                                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                         @RequestParam(required = false) LocalDateTime rangeEnd,
                                                         @RequestParam(required = false) StatusComment status,
                                                         @RequestParam(defaultValue = "0") int from,
                                                         @RequestParam(defaultValue = "10") int size,
                                                         HttpServletRequest request) {
        logRequestDetails(request);
        log.info("""
                Admin:Запрос на получение комментариев с фильтрацией.
                Список идентификаторов комментариев: {}
                Текст для поиска: {}
                Список идентификаторов комментаторов: {}
                Список идентификаторов событий: {}
                Дата и время не раньше которых создан комментарий: {}
                Дата и время не позже которых создан комментарий: {}
                Статус комментария: {},
                Количество событий, которые нужно пропустить: {}
                Количество событий в наборе: {}
                """, commentIds, text, commentatorIds, eventIds, rangeStart, rangeEnd, status, from, size);
        return commentServiceImpl.getCommentBySearchAdmins(commentIds, text, commentatorIds, eventIds, rangeStart,
                rangeEnd, status, from, size);
    }

    private void logRequestDetails(HttpServletRequest request) {
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        String params = queryString != null ? "?" + queryString : "";
        log.info("{} {}{}", method, url, params);
    }
}
