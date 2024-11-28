package ru.practicum.compilation.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.dto.CompilationDto;
import ru.practicum.compilation.model.dto.NewCompilationDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.mapper.EventMapper;

import java.util.Collections;
import java.util.List;

/**
 * Маппер для преобразования объектов между слоями компиляции и DTO.
 * Использует маппер событий для преобразования идентификаторов событий в объекты Event.
 */
@Mapper(uses = {EventMapper.class})
public interface CompilationMapper {

    /**
     * Преобразует объект NewCompilationDto в объект Compilation.
     * Использует метод mapEventIdsToEvent для преобразования идентификаторов событий в объекты Event.
     *
     * @param newCompilationDto DTO для создания новой подборки.
     * @return объект Compilation.
     */
    @Mapping(target = "events", source = "events", qualifiedByName = "mapEventIdsToEvent")
    Compilation toCompilation(NewCompilationDto newCompilationDto);

    /**
     * Преобразует объект Compilation в объект CompilationDto.
     *
     * @param compilation объект компиляции.
     * @return объект CompilationDto.
     */
    CompilationDto toCompilationDto(Compilation compilation);

    /**
     * Преобразует список идентификаторов событий в список объектов Event.
     * Если список идентификаторов событий пуст, возвращает пустой список.
     *
     * @param eventIds список идентификаторов событий.
     * @return список объектов Event.
     */
    @Named("mapEventIdsToEvent")
    default List<Event> mapEventIdsToEvent(List<Long> eventIds) {
        return eventIds == null ? Collections.emptyList() : eventIds.stream().map(id -> {
            Event event = new Event();
            event.setId(id);
            return event;
        }).toList();
    }

}
