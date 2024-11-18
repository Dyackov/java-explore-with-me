package ru.practicum.comment.model.mapper;

import org.mapstruct.Mapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.dto.CommentDto;
import ru.practicum.comment.model.dto.CommentFullDto;
import ru.practicum.comment.model.dto.NewCommentDto;
import ru.practicum.event.model.mapper.EventMapper;
import ru.practicum.user.model.mapper.UserMapper;

@Mapper(uses = {UserMapper.class, EventMapper.class})
public interface CommentMapper {
    Comment toComment(NewCommentDto newCommentDto);

    CommentFullDto toCommentFullDto(Comment comment);

    CommentDto toCommentDto(Comment comment);

}