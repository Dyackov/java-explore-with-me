package ru.practicum.event.model.mapper;

import org.mapstruct.*;
import ru.practicum.category.model.Category;
import ru.practicum.category.model.mapper.CategoryMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.dto.EventFullDto;
import ru.practicum.event.model.dto.EventShortDto;
import ru.practicum.event.model.dto.NewEventDto;
import ru.practicum.event.model.dto.UpdateEventAdminRequest;
import ru.practicum.event.model.enums.StateAction;
import ru.practicum.user.model.mapper.UserMapper;

@Mapper(uses = {CategoryMapper.class, EventMapper.class, UserMapper.class, LocationMapper.class, StateAction.class})
public interface EventMapper {
    @Mapping(source = "category", target = "category.id")
    Event toEvent(NewEventDto newEventDto);

    EventFullDto toEventFullDto(Event event);

    Event toEntity(EventShortDto eventShortDto);

    EventShortDto toEventShortDto(Event event);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Event partialUpdate(EventShortDto eventShortDto, @MappingTarget Event event);


    @Mapping(source = "category", target = "category.id")
    Event toEntity(UpdateEventAdminRequest updateEventAdminRequest);

    @Mapping(source = "category.id", target = "category")
    UpdateEventAdminRequest toDto(Event event);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "category", target = "category")
    Event partialUpdate(UpdateEventAdminRequest updateEventAdminRequest, @MappingTarget Event event);

    default Category createCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }
}

