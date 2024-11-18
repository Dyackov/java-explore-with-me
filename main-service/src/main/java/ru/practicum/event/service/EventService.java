package ru.practicum.event.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.error.exception.AuthorizationException;
import ru.practicum.error.exception.NotFoundException;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.dto.*;
import ru.practicum.event.model.enums.State;
import ru.practicum.request.model.dto.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Интерфейс для работы с сервисом событий.
 */
public interface EventService {

    /**
     * Получение списка событий пользователя по его ID.
     *
     * @param userId Идентификатор пользователя.
     * @param from   Смещение для пагинации.
     * @param size   Размер страницы для пагинации.
     * @return Список DTO кратких данных событий.
     */
    List<EventShortDto> getEventsByUserIdPrivate(long userId, int from, int size);

    /**
     * Создание нового события для пользователя.
     *
     * @param userId        Идентификатор пользователя.
     * @param newEventDto   DTO данных нового события.
     * @return DTO полных данных события.
     */
    EventFullDto createEventPrivate(long userId, NewEventDto newEventDto);

    /**
     * Получение события по ID для пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @param eventId Идентификатор события.
     * @return DTO полных данных события.
     */
    EventFullDto getEventByIdPrivate(long userId, long eventId);

    /**
     * Обновление события для пользователя.
     *
     * @param userId                   Идентификатор пользователя.
     * @param eventId                  Идентификатор события.
     * @param updateEventUserRequest   DTO запроса на обновление события.
     * @return DTO полных данных обновленного события.
     */
    EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest updateEventUserRequest);

    /**
     * Получение заявок на участие в событии пользователя.
     *
     * @param userId  Идентификатор пользователя.
     * @param eventId Идентификатор события.
     * @return Список DTO заявок на участие.
     */
    List<ParticipationRequestDto> getParticipationRequestsForUserEventsPrivate(long userId, long eventId);

    /**
     * Обновление статуса заявок на участие в событии.
     *
     * @param userId                        Идентификатор пользователя.
     * @param eventId                       Идентификатор события.
     * @param eventRequestStatusUpdateRequest DTO запроса на обновление статуса заявки.
     * @return Результат обновления статуса заявки.
     */
    EventRequestStatusUpdateResult updateRequestStatusPrivate(long userId, long eventId,
                                                              EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    /**
     * Получение всех событий для админа с фильтрацией.
     *
     * @param users       Список идентификаторов пользователей.
     * @param states      Список состояний событий.
     * @param categories  Список категорий событий.
     * @param rangeStart  Дата начала диапазона.
     * @param rangeEnd    Дата окончания диапазона.
     * @param from        Смещение для пагинации.
     * @param size        Размер страницы для пагинации.
     * @return Список DTO полных данных событий.
     */
    List<EventFullDto> getAllEventsAdmin(List<Long> users,
                                         List<State> states,
                                         List<Long> categories,
                                         LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd,
                                         int from,
                                         int size);

    /**
     * Обновление события для админа.
     *
     * @param eventId                 Идентификатор события.
     * @param updateEventAdminRequest DTO запроса на обновление события.
     * @return DTO полных данных обновленного события.
     */
    EventFullDto updateEvent(long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    /**
     * Получение события по ID для публичного доступа.
     *
     * @param id      Идентификатор события.
     * @param request HTTP-запрос.
     * @return DTO полных данных события.
     */
    EventFullDto getEventByIdPublic(int id, HttpServletRequest request);

    /**
     * Получение всех событий с фильтрацией для публичного доступа.
     *
     * @param text               Текст для поиска в событиях.
     * @param categories         Список категорий событий.
     * @param paid               Признак оплаты события.
     * @param rangeStart         Дата начала диапазона.
     * @param rangeEnd           Дата окончания диапазона.
     * @param onlyAvailable      Признак доступности события.
     * @param sort               Параметры сортировки.
     * @param from               Смещение для пагинации.
     * @param size               Размер страницы для пагинации.
     * @param request            HTTP-запрос.
     * @return Список DTO кратких данных событий.
     */
    List<EventShortDto> getAllEventsPublic(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd, Boolean onlyAvailable, String sort,
                                           int from, int size, HttpServletRequest request);

    /**
     * Получение события по ID и выброс исключения, если не найдено.
     *
     * @param eventId Идентификатор события.
     * @return Событие.
     * @throws NotFoundException Если событие не найдено.
     */
    Event getEventByIdOrThrow(long eventId);

    /**
     * Получение события по ID с проверкой авторизации пользователя.
     *
     * @param userId   Идентификатор пользователя.
     * @param eventId  Идентификатор события.
     * @return Событие.
     * @throws AuthorizationException Если пользователь не авторизован для этого события.
     */
    Event getEventAndCheckAuthorization(long userId, long eventId);

}
