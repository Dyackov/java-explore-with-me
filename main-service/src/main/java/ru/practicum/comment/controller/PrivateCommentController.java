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

@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
@Slf4j
public class PrivateCommentController {

    private final CommentService commentServiceImpl;

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

    private void logRequestDetails(HttpServletRequest request) {
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        String params = queryString != null ? "?" + queryString : "";
        log.info("{} {}{}", method, url, params);
    }
}