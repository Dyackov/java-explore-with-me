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

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class PublicEventController {

    private final EventService eventServiceImpl;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventByIdPublic(@PathVariable int id,
                                           HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Public:Получение подробной информации об опубликованном событии. Событие ID: {}", id);
        return eventServiceImpl.getEventByIdPublic(id);
    }

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
                """, text, categories, paid, rangeStart, onlyAvailable, rangeEnd, sort, from, size);
        return eventServiceImpl.getAllEventsPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }


    private void logRequestDetails(HttpServletRequest request) {
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        String params = queryString != null ? "?" + queryString : "";
        log.info("{} {}{}", method, url, params);
    }
}
