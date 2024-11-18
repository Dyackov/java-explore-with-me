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

@Mapper(uses = {EventMapper.class})
public interface CompilationMapper {

    @Mapping(target = "events", source = "events", qualifiedByName = "mapEventIdsToEvent")
    Compilation toCompilation(NewCompilationDto newCompilationDto);

    CompilationDto toCompilationDto(Compilation compilation);

    @Named("mapEventIdsToEvent")
    default List<Event> mapEventIdsToEvent(List<Long> eventIds) {
        return eventIds == null ? Collections.emptyList() : eventIds.stream().map(id -> {
            Event event = new Event();
            event.setId(id);
            return event;
        }).toList();
    }

}