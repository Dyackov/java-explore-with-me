package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.model.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.dto.CompilationDto;
import ru.practicum.compilation.model.dto.NewCompilationDto;
import ru.practicum.compilation.model.dto.UpdateCompilationRequest;
import ru.practicum.compilation.storage.CompilationRepository;
import ru.practicum.error.exception.NotFoundException;
import ru.practicum.event.model.Event;
import ru.practicum.event.storage.EventRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Сервис для работы с подборками.
 * Осуществляет создание, удаление, обновление, получение подборок, а также проверку существования событий в подборке.
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;

    /**
     * Создает новую подборку.
     *
     * @param newCompilationDto DTO для создания подборки.
     * @return DTO созданной подборки.
     */
    @Override
    public CompilationDto createCompilationAdmin(NewCompilationDto newCompilationDto) {
        log.debug("Создание новой подборки: {}", newCompilationDto);
        Compilation compilation = compilationMapper.toCompilation(newCompilationDto);
        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
            checkEventExists(events, newCompilationDto.getEvents());
            compilation.setEvents(events);
        }
        log.info("compilation: {}", compilation);
        Compilation savedCompilation = compilationRepository.save(compilation);
        log.info("Создана новая подборка: {}", savedCompilation);
        return compilationMapper.toCompilationDto(savedCompilation);
    }

    /**
     * Удаляет подборку по ID.
     *
     * @param compId ID подборки для удаления.
     */
    @Override
    public void deleteCompilationAdmin(long compId) {
        log.debug("Удаление подборки. ID подборки: {}", compId);
        getCompilationByIdOrThrow(compId);
        compilationRepository.deleteById(compId);
        log.info("Удалена подборка. ID подборки: {}", compId);
    }

    /**
     * Получает подборку по ID.
     * Если подборка не найдена, выбрасывает исключение NotFoundException.
     *
     * @param compId ID подборки.
     * @return найденная подборка.
     */
    @Override
    public Compilation getCompilationByIdOrThrow(long compId) {
        log.debug("Попытка получения подборки. ID подборки: {}", compId);
        return compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException(
                "Compilation with id = " + compId + " was not found"));
    }

    /**
     * Обновляет подборку по ID.
     *
     * @param compId ID подборки.
     * @param updateCompilationRequest данные для обновления.
     * @return DTO обновленной подборки.
     */
    @Override
    public CompilationDto updateCompilationAdmin(long compId, UpdateCompilationRequest updateCompilationRequest) {
        log.debug("Обновление подборки. ID подборки: {}", compId);
        Compilation compilation = getCompilationByIdOrThrow(compId);
        if (updateCompilationRequest.getEvents() != null && !updateCompilationRequest.getEvents().isEmpty()) {
            List<Event> events = eventRepository.findAllById(updateCompilationRequest.getEvents());
            checkEventExists(events, updateCompilationRequest.getEvents());
            compilation.setEvents(events);
        }
        Optional.ofNullable(updateCompilationRequest.getPinned()).ifPresent(compilation::setPinned);
        Optional.ofNullable(updateCompilationRequest.getTitle()).ifPresent(compilation::setTitle);
        Compilation savedCompilation = compilationRepository.save(compilation);
        log.info("Подборка обновлена\n{}", savedCompilation);
        return compilationMapper.toCompilationDto(savedCompilation);
    }

    /**
     * Получает подборку по ID для публичного доступа.
     *
     * @param compId ID подборки.
     * @return DTO найденной подборки.
     */
    @Override
    public CompilationDto getCompilationByIdPublic(long compId) {
        log.debug("Получение подборки. ID подборки: {}", compId);
        Compilation compilation = getCompilationByIdOrThrow(compId);
        log.info("Получена подборка\n{}", compilation);
        return compilationMapper.toCompilationDto(compilation);
    }

    /**
     * Получает список подборок с учетом параметров pinned, from и size для пагинации.
     *
     * @param pinned флаг закрепленности подборки.
     * @param from   начальный индекс для пагинации.
     * @param size   размер страницы.
     * @return список DTO подборок.
     */
    @Override
    public List<CompilationDto> getCompilationsPublic(Boolean pinned, int from, int size) {
        log.debug("Получение подборок. pinned: {}, from: {}, size: {}", pinned, from, size);
        Pageable pageable = createPageable(from, size, Sort.unsorted());
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pageable);
        log.info("Получен список подборок\n{}", compilations);
        return compilations.stream().map(compilationMapper::toCompilationDto).toList();
    }

    /**
     * Создает объект Pageable для пагинации.
     *
     * @param from   начальный индекс.
     * @param size   размер страницы.
     * @param sort   сортировка.
     * @return объект Pageable.
     */
    private Pageable createPageable(int from, int size, Sort sort) {
        log.debug("Create Pageable with offset from {}, size {}", from, size);
        return PageRequest.of(from / size, size, sort);
    }

    /**
     * Проверяет наличие всех событий по их ID.
     * Если какие-то события не найдены, выбрасывает исключение NotFoundException.
     *
     * @param events    список событий.
     * @param actualIds список ID событий.
     */
    @Override
    public void checkEventExists(List<Event> events, List<Long> actualIds) {
        if (events.size() != actualIds.size()) {
            Set<Long> foundIds = events.stream()
                    .map(Event::getId)
                    .collect(Collectors.toSet());
            List<Long> missingIds = actualIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();
            throw new NotFoundException("Отсутствующие ID Событий: " + missingIds);
        }
    }
}