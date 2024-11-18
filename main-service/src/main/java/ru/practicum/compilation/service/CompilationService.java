package ru.practicum.compilation.service;

import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.dto.CompilationDto;
import ru.practicum.compilation.model.dto.NewCompilationDto;
import ru.practicum.compilation.model.dto.UpdateCompilationRequest;
import ru.practicum.event.model.Event;

import java.util.List;

public interface CompilationService {
    CompilationDto createCompilationAdmin(NewCompilationDto newCompilationDto);

    void deleteCompilationAdmin(long compId);

    Compilation getCompilationByIdOrThrow(long compId);

    CompilationDto updateCompilationAdmin(long compId, UpdateCompilationRequest updateCompilationRequest);

    CompilationDto getCompilationByIdPublic(long compId);

    List<CompilationDto> getCompilationsPublic(Boolean pinned, int from, int size);

    void checkEventExists(List<Event> events, List<Long> actualIds);
}
