package ru.practicum.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.model.dto.EventFullDto;
import ru.practicum.event.model.dto.EventShortDto;
import ru.practicum.event.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Контроллер для обработки публичных запросов, связанных с событиями.
 * Обрабатывает запросы на получение всех событий и подробной информации о событии.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class PublicEventController {

    private final EventService eventServiceImpl;

    /**
     * Получение списка событий с возможностью фильтрации.
     *
     * @param text           Текст для поиска в описаниях событий.
     * @param categories     Список идентификаторов категорий событий.
     * @param paid           Параметр, указывающий на платность событий.
     * @param rangeStart     Минимальная дата и время для начала события.
     * @param rangeEnd       Максимальная дата и время для начала события.
     * @param onlyAvailable  Флаг, указывающий на доступность событий.
     * @param sort           Параметр сортировки.
     * @param from           Смещение для пагинации.
     * @param size           Количество элементов на странице.
     * @param request        Объект HttpServletRequest для логирования деталей запроса.
     * @return Список событий, удовлетворяющих фильтрам.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getAllEventsPublic(@RequestParam(required = false) String text,
                                                  @RequestParam(required = false) List<Long> categories,
                                                  @RequestParam(required = false) Boolean paid,
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                  @RequestParam(required = false) LocalDateTime rangeStart,
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                  @RequestParam(required = false) LocalDateTime rangeEnd,
                                                  @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                  @RequestParam(required = false) String sort,
                                                  @RequestParam(defaultValue = "0") int from,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  HttpServletRequest request) {
        logRequestDetails(request);
        log.info("""
                Public:Запрос на получение событий с фильтрацией.
                Текст для поиска: {}
                Список идентификаторов категорий: {}
                Платных/бесплатных событий: {}
                Дата и время не раньше которых должно произойти событие: {}
                Дата и время не позже которых должно произойти событие: {}
                События у которых не исчерпан лимит запросов: {}
                Вариант сортировки: {}
                Количество событий, которые нужно пропустить: {}
                Количество событий в наборе: {}
                """, text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return eventServiceImpl.getAllEventsPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                from, size, request);
    }

    /**
     * Получение полной информации о событии по его ID.
     *
     * @param id      Идентификатор события.
     * @param request Объект HttpServletRequest для логирования деталей запроса.
     * @return Полные данные о событии.
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventByIdPublic(@PathVariable int id,
                                           HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Public:Получение подробной информации об опубликованном событии. Событие ID: {}", id);
        return eventServiceImpl.getEventByIdPublic(id, request);
    }

    /**
     * Логирование деталей HTTP-запроса.
     *
     * @param request Объект HttpServletRequest для логирования.
     */
    private void logRequestDetails(HttpServletRequest request) {
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        String params = queryString != null ? "?" + queryString : "";
        log.info("{} {}{}", method, url, params);
    }
}