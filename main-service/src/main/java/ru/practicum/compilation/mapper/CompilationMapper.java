package ru.practicum.compilation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.dto.CompilationDto;
import ru.practicum.compilation.model.dto.NewCompilationDto;
import ru.practicum.compilation.model.dto.UpdateCompilationRequest;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.dto.EventShortDto;
import ru.practicum.event.model.mapper.EventMapper;

import java.util.Collections;
import java.util.List;

@Mapper(uses = {EventMapper.class})
public interface CompilationMapper {

    @Mapping(target = "events", source = "events", qualifiedByName = "mapEventIdsToEvent")
    Compilation toCompilation(NewCompilationDto newCompilationDto);

    CompilationDto toCompilationDto(Compilation compilation);

    @Mapping(target = "events", source = "events", qualifiedByName = "mapEventIdsToEventShortDtos")
    CompilationDto toCompilationDto(UpdateCompilationRequest updateCompilationRequest);

    @Named("mapEventIdsToEventShortDtos")
    default List<EventShortDto> mapEventIdsToEventShortDtos(List<Long> eventIds) {
        return eventIds.stream().map(id -> EventShortDto.builder().id(id).build()).toList();
    }

    @Named("mapEventIdsToEvent")
    default List<Event> mapEventIdsToEvent(List<Long> eventIds) {
        return eventIds == null ? Collections.emptyList() : eventIds.stream().map(id -> {
            Event event = new Event();
            event.setId(id);
            return event;
        }).toList();
    }

}