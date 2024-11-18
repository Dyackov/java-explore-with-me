package ru.practicum.request.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.model.dto.ParticipationRequestDto;
import ru.practicum.request.service.RequestService;

import java.util.List;

/**
 * Контроллер для работы с запросами на участие в событиях для пользователя.
 * Обрабатывает запросы на получение, создание и отмену заявок на участие в событиях.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class PrivateRequestForParticipationController {

    private final RequestService requestServiceImpl;

    /**
     * Получение списка заявок на участие в событиях пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @param request HTTP запрос для логирования.
     * @return Список заявок на участие в событиях.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getRequestsPrivate(@PathVariable Long userId,
                                                            HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Private:Получен запрос на получение заявок на участие в событиях. Пользователь ID: {}", userId);
        return requestServiceImpl.getRequestsPrivate(userId);
    }

    /**
     * Создание заявки на участие в событии.
     *
     * @param userId Идентификатор пользователя.
     * @param eventId Идентификатор события.
     * @param request HTTP запрос для логирования.
     * @return Созданная заявка на участие.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequestPrivate(@PathVariable long userId,
                                                        @RequestParam long eventId,
                                                        HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Private:Получен запрос на создание заявки на участие в событии. Пользователь ID: {}, Событие ID: {}",
                userId, eventId);
        return requestServiceImpl.createRequestPrivate(userId, eventId);
    }

    /**
     * Обновление (отмена) заявки пользователя на участие в событии.
     *
     * @param userId Идентификатор пользователя.
     * @param requestId Идентификатор заявки.
     * @param request HTTP запрос для логирования.
     * @return Обновленная заявка.
     */
    @PatchMapping("/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto updateRequestPrivate(@PathVariable long userId,
                                                        @PathVariable long requestId,
                                                        HttpServletRequest request) {
        logRequestDetails(request);
        log.info("Private:Получен запрос на обновление(отмену) собственной заявки на участие в событии. " +
                "Пользователь ID: {}, Запрос ID: {}", userId, requestId);
        return requestServiceImpl.updateRequestPrivate(userId, requestId);
    }

    /**
     * Логирование деталей HTTP запроса.
     *
     * @param request HTTP запрос.
     */
    private void logRequestDetails(HttpServletRequest request) {
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        String params = queryString != null ? "?" + queryString : "";
        log.info("{} {}{}", method, url, params);
    }
}
