package ru.practicum.event.model.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import ru.practicum.category.model.mapper.CategoryMapper;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.dto.LocationDto;
import ru.practicum.user.model.mapper.UserMapper;

@Mapper(uses = {CategoryMapper.class, EventMapper.class, UserMapper.class})
public interface LocationMapper {
    Location toLocation(LocationDto locationDto);

    @InheritInverseConfiguration(name = "toLocation")
    LocationDto toDto(Location location);

}