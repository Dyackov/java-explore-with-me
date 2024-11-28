package ru.practicum.comment.model.mapper;

import org.mapstruct.Mapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.dto.CommentDto;
import ru.practicum.comment.model.dto.CommentFullDto;
import ru.practicum.comment.model.dto.NewCommentDto;
import ru.practicum.event.model.mapper.EventMapper;
import ru.practicum.user.model.mapper.UserMapper;

/**
 * Интерфейс для маппинга между сущностями и DTO комментариев.
 * Использует MapStruct для автоматической генерации мапперов.
 */
@Mapper(uses = {UserMapper.class, EventMapper.class})
public interface CommentMapper {

    /**
     * Преобразует объект NewCommentDto в сущность Comment.
     *
     * @param newCommentDto DTO комментария, который нужно преобразовать в сущность.
     * @return Сущность Comment, соответствующая данным из NewCommentDto.
     */
    Comment toComment(NewCommentDto newCommentDto);

    /**
     * Преобразует сущность Comment в полный DTO комментария (CommentFullDto).
     *
     * @param comment Сущность комментария, которую нужно преобразовать.
     * @return Полный DTO комментария с дополнительной информацией о пользователе и событии.
     */
    CommentFullDto toCommentFullDto(Comment comment);

    /**
     * Преобразует сущность Comment в краткий DTO комментария (CommentDto).
     *
     * @param comment Сущность комментария, которую нужно преобразовать.
     * @return Краткий DTO комментария с текстом и статусом.
     */
    CommentDto toCommentDto(Comment comment);
}