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

/**
 * Контроллер для обработки запросов, связанных с личными событиями пользователя.
 * Обрабатывает создание, получение и обновление событий, а также запросы на участие в них.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class PrivateEventController {

    private final EventService eventServiceImpl;

    /**
     * Создание нового события пользователем.
     *
     * @param userId      ID пользователя, создающего событие
     * @param newEventDto данные для создания события
     * @param request     HTTP запрос для логирования
     * @return созданное событие
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEventPrivate(@PathVariable long userId,
                                           @RequestBody @Valid NewEventDto newEventDto,
                                           HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Private: Получен запрос на добавление нового события:\n{}", newEventDto);
        return eventServiceImpl.createEventPrivate(userId, newEventDto);
    }

    /**
     * Получение полной информации о событии.
     *
     * @param userId  ID пользователя, который сделал запрос
     * @param eventId ID события, о котором нужно получить информацию
     * @param request HTTP запрос для логирования
     * @return полная информация о событии
     */
    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventByIdPrivate(@PathVariable long userId,
                                            @PathVariable long eventId,
                                            HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Private: Получен запрос на получение полной информации о событии. ID пользователя: {}, ID события: {}",
                userId, eventId);
        return eventServiceImpl.getEventByIdPrivate(userId, eventId);
    }

    /**
     * Получение всех событий пользователя.
     *
     * @param userId ID пользователя, чьи события нужно отобразить
     * @param from   с какого события начать вывод (для пагинации)
     * @param size   количество событий на странице (для пагинации)
     * @param request HTTP запрос для логирования
     * @return список событий пользователя
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEventsByUserIdPrivate(@PathVariable long userId,
                                                        @RequestParam(defaultValue = "0") int from,
                                                        @RequestParam(defaultValue = "10") int size,
                                                        HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Private: Получен запрос на получение всех событий пользователя. ID пользователя:{}, from:{}, size:{}",
                userId, from, size);
        return eventServiceImpl.getEventsByUserIdPrivate(userId, from, size);
    }

    /**
     * Обновление события пользователя.
     *
     * @param userId                 ID пользователя, обновляющего событие
     * @param eventId                ID события, которое нужно обновить
     * @param updateEventUserRequest объект с новыми данными для события
     * @param request                HTTP запрос для логирования
     * @return обновленное событие
     */
    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEventPrivate(@PathVariable long userId,
                                           @PathVariable long eventId,
                                           @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest,
                                           HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Private: Получен запрос на обновление события. ID пользователя: {}, ID события: {}.\n{}",
                userId, eventId, updateEventUserRequest);
        return eventServiceImpl.updateEvent(userId, eventId, updateEventUserRequest);
    }

    /**
     * Получение запросов на участие в событии.
     *
     * @param userId  ID пользователя, чьи запросы на участие нужно получить
     * @param eventId ID события, для которого получаем запросы
     * @param request HTTP запрос для логирования
     * @return список запросов на участие в событии
     */
    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getParticipationRequestsForUserEventsPrivate(@PathVariable long userId,
                                                                                      @PathVariable long eventId,
                                                                                      HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Private: Получен запрос на получение информации о запросах на участие в событии. ID пользователя: {}, ID события: {}",
                userId, eventId);
        return eventServiceImpl.getParticipationRequestsForUserEventsPrivate(userId, eventId);
    }

    /**
     * Обновление статуса заявок на участие в событии.
     *
     * @param userId                    ID пользователя, который обновляет статус заявок
     * @param eventId                   ID события, для которого обновляются заявки
     * @param eventRequestStatusUpdateRequest запрос на обновление статуса заявки
     * @param request                   HTTP запрос для логирования
     * @return результат обновления статуса заявки
     */
    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult updateRequestStatusPrivate(@PathVariable long userId,
                                                                     @PathVariable long eventId,
                                                                     @Valid @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
                                                                     HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Private: Получен запрос на изменение статуса (подтверждена, отменена) заявок на участие в событии. ID пользователя: {}, ID события: {}\n{}",
                userId, eventId, eventRequestStatusUpdateRequest);
        return eventServiceImpl.updateRequestStatusPrivate(userId, eventId, eventRequestStatusUpdateRequest);
    }

    /**
     * Логирует детали HTTP запроса.
     *
     * @param request HTTP запрос
     */
    private void logRequestDetails(HttpServletRequest request) {
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        String params = queryString != null ? "?" + queryString : "";
        log.info("{} {}{}", method, url, params);
    }
}