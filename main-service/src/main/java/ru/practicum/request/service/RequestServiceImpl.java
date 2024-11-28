package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.error.exception.NotFoundException;
import ru.practicum.error.exception.ValidateRequestException;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.enums.State;
import ru.practicum.event.service.EventService;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.dto.ParticipationRequestDto;
import ru.practicum.request.model.enums.RequestState;
import ru.practicum.request.model.mapper.RequestMapper;
import ru.practicum.request.storage.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для управления запросами на участие.
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserService userServiceImpl;
    private final EventService eventServiceImpl;
    private final RequestMapper requestMapper;

    /**
     * Получение списка запросов на участие для пользователя.
     *
     * @param userId идентификатор пользователя.
     * @return список запросов на участие.
     */
    @Override
    public List<ParticipationRequestDto> getRequestsPrivate(Long userId) {
        log.debug("Private:Получение списка запросов на участие. Пользователь ID: {}", userId);
        userServiceImpl.getUserByIdOrThrow(userId);
        List<Request> requests = requestRepository.findByRequesterId(userId);
        log.info("Private:Получен список запросов на участие:\n{}", requests);
        return requests.stream().map(requestMapper::toParticipationRequestDto).toList();
    }

    /**
     * Создание нового запроса на участие.
     *
     * @param userId  идентификатор пользователя.
     * @param eventId идентификатор события.
     * @return созданный запрос на участие.
     */
    @Override
    public ParticipationRequestDto createRequestPrivate(long userId, long eventId) {
        log.debug("Private:Создание запроса на участие. Пользователь ID: {}, Событие ID: {}", userId, eventId);
        User user = userServiceImpl.getUserByIdOrThrow(userId);
        Event event = eventServiceImpl.getEventByIdOrThrow(eventId);
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ValidateRequestException("Данный запрос уже существует. Нельзя добавить повторный запрос");
        }
        if (userId == event.getInitiator().getId()) {
            throw new ValidateRequestException("Инициатор события не может добавить запрос на участие в своём событии");
        }
        if (event.getState() != State.PUBLISHED) {
            throw new ValidateRequestException("Нельзя участвовать в неопубликованном событии");
        }
        if (event.getConfirmedRequests() > 0 && event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            throw new ValidateRequestException("У события достигнут лимит запросов на участие");
        }

        Request request = Request.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(user)
                .build();
        if (event.getRequestModeration().equals(false) || event.getParticipantLimit() == 0) {
            request.setStatus(RequestState.CONFIRMED);
            event.setConfirmedRequests(+1L);
        } else {
            request.setStatus(RequestState.PENDING);
        }
        Request savedRequest = requestRepository.save(request);
        log.info("Private:Создан запрос на участие:\n{}", savedRequest);
        return requestMapper.toParticipationRequestDto(savedRequest);
    }

    /**
     * Обновление статуса запроса на участие.
     *
     * @param userId    идентификатор пользователя.
     * @param requestId идентификатор запроса на участие.
     * @return обновленный запрос на участие.
     */
    @Override
    public ParticipationRequestDto updateRequestPrivate(long userId, long requestId) {
        log.debug("Private:Обновление запроса на участие. Пользователь ID: {}, Запрос на участие ID: {}", userId, requestId);
        userServiceImpl.getUserByIdOrThrow(userId);
        Request request = getRequestByIdOrThrow(requestId);
        if (userId != request.getRequester().getId()) {
            throw new ValidateRequestException("Пользователь не является хозяином запроса.");
        }
        request.setStatus(RequestState.CANCELED);
        Request savedRequest = requestRepository.save(request);
        log.info("Private:Запрос на участие обновлён:\n{}", savedRequest);
        return requestMapper.toParticipationRequestDto(savedRequest);
    }

    /**
     * Получение запроса на участие по идентификатору или выброс исключения, если он не найден.
     *
     * @param requestId идентификатор запроса на участие.
     * @return запрос на участие.
     */
    @Override
    public Request getRequestByIdOrThrow(long requestId) {
        return requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException(
                "Request with id = " + requestId + " was not found"));
    }
}