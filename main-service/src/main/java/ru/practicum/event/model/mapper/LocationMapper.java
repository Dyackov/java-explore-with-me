package ru.practicum.event.model.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import ru.practicum.category.model.mapper.CategoryMapper;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.dto.LocationDto;
import ru.practicum.user.model.mapper.UserMapper;

/**
 * Маппер для преобразования объектов местоположения (Location) между сущностью и DTO.
 * Используется аннотация MapStruct для автоматической генерации методов преобразования.
 * Маппинг настроен для работы с категориями и пользователями.
 */
@Mapper(uses = {CategoryMapper.class, EventMapper.class, UserMapper.class})
public interface LocationMapper {

    /**
     * Преобразует DTO местоположения (LocationDto) в сущность местоположения (Location).
     *
     * @param locationDto DTO для местоположения
     * @return Сущность местоположения (Location)
     */
    Location toLocation(LocationDto locationDto);

    /**
     * Преобразует сущность местоположения (Location) в DTO местоположения (LocationDto).
     * Используется аннотация @InheritInverseConfiguration для автоматического генерации
     * обратного маппинга на основе метода "toLocation".
     *
     * @param location Сущность местоположения
     * @return DTO местоположения (LocationDto)
     */
    @InheritInverseConfiguration(name = "toLocation")
    LocationDto toDto(Location location);
}
