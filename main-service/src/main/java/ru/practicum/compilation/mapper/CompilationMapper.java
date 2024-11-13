package ru.practicum.compilation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.dto.CompilationDto;
import ru.practicum.compilation.model.dto.NewCompilationDto;
import ru.practicum.compilation.model.dto.UpdateCompilationRequest;
import ru.practicum.event.model.dto.EventShortDto;
import ru.practicum.event.model.mapper.EventMapper;

import java.util.List;

@Mapper(uses = {EventMapper.class})
public interface CompilationMapper {


    @Mapping(target = "events", ignore = true)
    Compilation toCompilation(NewCompilationDto newCompilationDto);

    CompilationDto toCompilationDto(Compilation compilation);

    @Mapping(target = "events", source = "eventIds", qualifiedByName = "mapEventIdsToEventShortDtos")
    CompilationDto toCompilationDto(UpdateCompilationRequest updateCompilationRequest);

    @Named("mapEventIdsToEventShortDtos")
    default List<EventShortDto> mapEventIdsToEventShortDtos(List<Long> eventIds) {
        return eventIds.stream().map(id -> EventShortDto.builder().id(id).build())
                .toList();
    }
}