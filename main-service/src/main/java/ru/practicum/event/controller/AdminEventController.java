package ru.practicum.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.model.dto.EventFullDto;
import ru.practicum.event.model.dto.UpdateEventAdminRequest;
import ru.practicum.event.model.enums.State;
import ru.practicum.event.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Slf4j
public class AdminEventController {

    private final EventService eventServiceImpl;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getAllEventsAdmin(@RequestParam(required = false) List<Long> users,
                                                @RequestParam(required = false) List<State> states,
                                                @RequestParam(required = false) List<Long> categories,
                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                @RequestParam(required = false) LocalDateTime rangeStart,
                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                @RequestParam(required = false) LocalDateTime rangeEnd,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size,
                                                HttpServletRequest request) {
        logRequestDetails(request);
        log.info("""
                Admin:Получен запрос на поиск событий:
                Список ID пользователей: {}\
                Список состояний: {}\
                Список ID категорий: {}\
                Дата и время не раньше которых должно произойти событие: {}\
                Дата и время не позже которых должно произойти событие: {}\
                from: {}, size: {}""", users, states, categories, rangeStart, rangeEnd, from, size);
        return eventServiceImpl.getAllEventsAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEventAdmin(@PathVariable long eventId,
                                         @RequestBody UpdateEventAdminRequest updateEventAdminRequest,
                                         HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Admin:Получен запрос на обновление события. ID события: {}.\n{}", eventId, updateEventAdminRequest);
        return eventServiceImpl.updateEvent(eventId, updateEventAdminRequest);
    }

    private void logRequestDetails(HttpServletRequest request) {
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        String params = queryString != null ? "?" + queryString : "";
        log.info("{} {}{}", method, url, params);
    }
}
