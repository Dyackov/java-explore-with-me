package ru.practicum.compilation.service;

import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.dto.CompilationDto;
import ru.practicum.compilation.model.dto.NewCompilationDto;
import ru.practicum.compilation.model.dto.UpdateCompilationRequest;
import ru.practicum.event.model.Event;

import java.util.List;

/**
 * Интерфейс для сервиса работы с подборками.
 * Осуществляет создание, удаление, обновление, получение подборок и проверку существования событий в подборке.
 */
public interface CompilationService {

    /**
     * Создает новую подборку.
     *
     * @param newCompilationDto DTO для создания подборки.
     * @return DTO созданной подборки.
     */
    CompilationDto createCompilationAdmin(NewCompilationDto newCompilationDto);

    /**
     * Удаляет подборку по ID.
     *
     * @param compId ID подборки для удаления.
     */
    void deleteCompilationAdmin(long compId);

    /**
     * Получает подборку по ID. Если подборка не найдена, выбрасывает исключение.
     *
     * @param compId ID подборки.
     * @return найденная подборка.
     */
    Compilation getCompilationByIdOrThrow(long compId);

    /**
     * Обновляет подборку по ID.
     *
     * @param compId ID подборки.
     * @param updateCompilationRequest данные для обновления.
     * @return DTO обновленной подборки.
     */
    CompilationDto updateCompilationAdmin(long compId, UpdateCompilationRequest updateCompilationRequest);

    /**
     * Получает подборку по ID для публичного доступа.
     *
     * @param compId ID подборки.
     * @return DTO найденной подборки.
     */
    CompilationDto getCompilationByIdPublic(long compId);

    /**
     * Получает список подборок с учетом параметров pinned, from и size для пагинации.
     *
     * @param pinned флаг закрепленности подборки.
     * @param from   начальный индекс для пагинации.
     * @param size   размер страницы.
     * @return список DTO подборок.
     */
    List<CompilationDto> getCompilationsPublic(Boolean pinned, int from, int size);

    /**
     * Проверяет наличие всех событий по их ID.
     * Если какие-то события не найдены, выбрасывает исключение NotFoundException.
     *
     * @param events    список событий.
     * @param actualIds список ID событий.
     */
    void checkEventExists(List<Event> events, List<Long> actualIds);
}
