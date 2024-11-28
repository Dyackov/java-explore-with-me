package ru.practicum.request.service;

import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.dto.ParticipationRequestDto;

import java.util.List;

/**
 * Сервис для работы с заявками на участие в событиях.
 */
public interface RequestService {

    /**
     * Получение всех заявок пользователя на участие в событиях.
     *
     * @param userId идентификатор пользователя.
     * @return список заявок на участие.
     */
    List<ParticipationRequestDto> getRequestsPrivate(@PathVariable Long userId);

    /**
     * Создание заявки на участие в событии.
     *
     * @param userId  идентификатор пользователя.
     * @param eventId идентификатор события.
     * @return созданная заявка на участие.
     */
    ParticipationRequestDto createRequestPrivate(long userId, long eventId);

    /**
     * Обновление (отмена) заявки на участие в событии.
     *
     * @param userId    идентификатор пользователя.
     * @param requestId идентификатор заявки.
     * @return обновленная заявка на участие.
     */
    ParticipationRequestDto updateRequestPrivate(long userId, long requestId);

    /**
     * Получение заявки по идентификатору или выбрасывание исключения, если заявка не найдена.
     *
     * @param requestId идентификатор заявки.
     * @return заявка.
     */
    Request getRequestByIdOrThrow(long requestId);
}
