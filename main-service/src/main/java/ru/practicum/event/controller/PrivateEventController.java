package ru.practicum.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.model.dto.*;
import ru.practicum.event.service.EventService;
import ru.practicum.request.model.dto.ParticipationRequestDto;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class PrivateEventController {

    private final EventService eventServiceImpl;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable long userId,
                                    @RequestBody @Valid NewEventDto newEventDto,
                                    HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Private:Получен запрос на добавление нового события:\n{}", newEventDto);
        return eventServiceImpl.createEvent(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventById(@PathVariable long userId,
                                     @PathVariable long eventId,
                                     HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Private:Получен запрос на получение полной информации о событии. ID пользователя: {}, ID события: {}",
                userId, eventId);
        return eventServiceImpl.getEventById(userId, eventId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEventsByUserId(@PathVariable long userId,
                                                 @RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Private:Получен запрос на получение всех событий пользователя.\nID пользователя:{}, from:{}, size:{}",
                userId, from, size);
        return eventServiceImpl.getEventsByUserId(userId, from, size);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable long userId,
                                    @PathVariable long eventId,
                                    @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest,
                                    HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Private:Получен запрос на обновление события. ID пользователя: {}, ID события: {}.\n{}",
                userId, eventId, updateEventUserRequest);
        return eventServiceImpl.updateEvent(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getParticipationRequestsForUserEvents(@PathVariable long userId,
                                                                               @PathVariable long eventId,
                                                                               HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Private:Получен запрос на получение информации о запросах на участие в событии. " +
                "ID пользователя: {}, ID события :{}", userId, eventId);

        return eventServiceImpl.getParticipationRequestsForUserEvents(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult updateRequestStatus(@PathVariable long userId,
                                                              @PathVariable long eventId,
                                                              @Valid @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
                                                              HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Private:Получен запрос на изменение статуса (подтверждена, отменена) заявок на участие в событии. " +
                "ID пользователя: {}, ID события :{}\n{}", userId, eventId, eventRequestStatusUpdateRequest);
        return eventServiceImpl.updateRequestStatus(userId, eventId, eventRequestStatusUpdateRequest);
    }


    private void logRequestDetails(HttpServletRequest request) {
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        String params = queryString != null ? "?" + queryString : "";
        log.info("{} {}{}", method, url, params);
    }

}
