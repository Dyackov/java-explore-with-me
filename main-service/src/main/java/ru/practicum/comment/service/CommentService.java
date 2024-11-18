package ru.practicum.comment.service;

import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.dto.*;
import ru.practicum.comment.model.enums.StatusComment;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {

    CommentFullDto createCommentPrivate(Long userId, Long eventId, NewCommentDto newCommentDto);

    CommentFullDto getCommentByIdPrivate(Long userId, Long commentId);

    List<CommentDto> getCommentsByUserIdPrivate(long userId, int from, int size);

    CommentFullDto updateCommentPrivate(long userId, long commentId, UpdateCommentDto updateCommentDto);

    List<CommentFullDto> getAllCommentsByEventIdPublic(long eventId, int from, int size);

    void deleteCommentAdmin(long commentId);

    CommentFullDto updateStatusCommentAdmin(long commentId, UpdateStatusCommentAdmin updateStatusCommentAdmin);

    List<CommentFullDto> getCommentBySearchAdmins(List<Long> commentIds, String text, List<Long> commentatorIds,
                                                  List<Long> eventIds, LocalDateTime rangeStart,
                                                  LocalDateTime rangeEnd, StatusComment status, int from, int size);

    Comment getCommentByIdOrThrow(long commentId);

    Comment getCommentAndCheckAuthorization(long userId, long commentId);
}
