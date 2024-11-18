package ru.practicum.event.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.category.model.mapper.CategoryMapper;
import ru.practicum.comment.model.mapper.CommentMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.dto.EventFullDto;
import ru.practicum.event.model.dto.EventShortDto;
import ru.practicum.event.model.dto.NewEventDto;
import ru.practicum.event.model.enums.StateAction;
import ru.practicum.user.model.mapper.UserMapper;

@Mapper(uses = {CategoryMapper.class, EventMapper.class, UserMapper.class, LocationMapper.class, StateAction.class, CommentMapper.class})
public interface EventMapper {
    @Mapping(source = "category", target = "category.id")
    Event toEvent(NewEventDto newEventDto);

    EventFullDto toEventFullDto(Event event);

    EventShortDto toEventShortDto(Event event);

}

