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

/**
 * Маппер для преобразования объектов события (Event) между различными представлениями:
 * - DTO для создания нового события (NewEventDto)
 * - Полное представление события (EventFullDto)
 * - Краткое представление события (EventShortDto)
 * Используется аннотация MapStruct для автоматической генерации методов преобразования.
 * Маппинг настроен для работы с категориями, пользователями, локациями и комментариями.
 */
@Mapper(uses = {CategoryMapper.class, EventMapper.class, UserMapper.class, LocationMapper.class, StateAction.class, CommentMapper.class})
public interface EventMapper {

    /**
     * Преобразует DTO для создания нового события (NewEventDto) в сущность события (Event).
     *
     * @param newEventDto DTO для создания нового события
     * @return Сущность события (Event)
     */
    @Mapping(source = "category", target = "category.id")
    Event toEvent(NewEventDto newEventDto);

    /**
     * Преобразует сущность события (Event) в полное DTO события (EventFullDto).
     *
     * @param event Сущность события
     * @return Полное DTO события (EventFullDto)
     */
    EventFullDto toEventFullDto(Event event);

    /**
     * Преобразует сущность события (Event) в краткое DTO события (EventShortDto).
     *
     * @param event Сущность события
     * @return Краткое DTO события (EventShortDto)
     */
    EventShortDto toEventShortDto(Event event);
}
