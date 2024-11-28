package ru.practicum.event.service;

import com.querydsl.core.BooleanBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.StatisticsClient;
import ru.practicum.category.model.Category;
import ru.practicum.category.service.CategoryService;
import ru.practicum.error.exception.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.dto.*;
import ru.practicum.event.model.enums.State;
import ru.practicum.event.model.mapper.EventMapper;
import ru.practicum.event.model.mapper.LocationMapper;
import ru.practicum.event.storage.EventRepository;
import ru.practicum.event.storage.LocationRepository;
import ru.practicum.model.DtoEndpointHit;
import ru.practicum.model.DtoViewStats;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.dto.ParticipationRequestDto;
import ru.practicum.request.model.enums.RequestState;
import ru.practicum.request.model.mapper.RequestMapper;
import ru.practicum.request.storage.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.event.model.QEvent.event;

/**
 * Сервис для работы с событиями.
 * Реализует интерфейс {@link EventService}.
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;

    private final CategoryService categoryServiceImpl;
    private final UserService userServiceImpl;

    private final EventMapper eventMapper;
    private final LocationMapper locationMapper;
    private final RequestMapper requestMapper;

    @Value("main-service")
    private String serviceName;
    private final StatisticsClient statisticsClient;

    /**
     * Получение списка событий для пользователя.
     *
     * @param userId идентификатор пользователя
     * @param from   количество элементов для пропуска
     * @param size   количество элементов на странице
     * @return список кратких данных о событиях
     */
    @Override
    public List<EventShortDto> getEventsByUserIdPrivate(long userId, int from, int size) {
        log.debug("Private:Получение всех событий. ID пользователя:{}, from:{}, size:{}", userId, from, size);
        userServiceImpl.getUserByIdOrThrow(userId);
        Pageable pageable = createPageable(from, size, Sort.by(Sort.Direction.ASC, "createdOn"));
        List<Event> events = eventRepository.findAll(pageable).getContent();
        setViews(events);
        log.info("Private:Получен список всех событий. ID пользователя:\n{}", events);
        return events.stream().map(eventMapper::toEventShortDto).toList();
    }

    /**
     * Создание нового события для пользователя.
     *
     * @param userId      идентификатор пользователя
     * @param newEventDto данные для создания события
     * @return полные данные о созданном событии
     * @throws DataTimeException если дата события некорректна
     */
    @Override
    public EventFullDto createEventPrivate(long userId, NewEventDto newEventDto) {
        log.debug("Private:Создание события: {}", newEventDto);
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now())) {
            throw new DataTimeException("Дата и время на которые намечено событие не может быть в прошлом");
        } else if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new DataTimeException("Дата и время на которые намечено событие не может быть раньше, " +
                    "чем через два часа от текущего момента");
        }
        User initiator = userServiceImpl.getUserByIdOrThrow(userId);
        Category category = categoryServiceImpl.getCategoryByIdOrThrow(newEventDto.getCategory());
        Event event = eventMapper.toEvent(newEventDto);
        event.setCategory(category);
        event.setConfirmedRequests(0L);
        event.setCreatedOn(LocalDateTime.now());
        event.setInitiator(initiator);
        Location locationFromDto = locationMapper.toLocation(newEventDto.getLocation());
        Location savedLocation = locationRepository.save(locationFromDto);
        event.setLocation(savedLocation);
        event.setPublishedOn(LocalDateTime.now());
        event.setState(State.PENDING);
        event.setViews(0L);
        Event savedEvent = eventRepository.save(event);
        log.info("Private:Создано событие:\n{}", savedEvent);

        return eventMapper.toEventFullDto(savedEvent);
    }

    /**
     * Получение события по идентификатору для пользователя.
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return полные данные о событии
     */
    @Override
    public EventFullDto getEventByIdPrivate(long userId, long eventId) {
        log.debug("Private:Получение события, ID пользователя: {}, ID события:{}", userId, eventId);
        getEventByIdOrThrow(eventId);
        Event event = getEventAndCheckAuthorization(userId, eventId);
        setViews(List.of(event));
        log.info("Private:Получено событие:\n{}", event);
        return eventMapper.toEventFullDto(event);
    }


    /**
     * Обновляет событие.
     *
     * @param userId                 ID пользователя, инициировавшего обновление события.
     * @param eventId                ID события, которое нужно обновить.
     * @param updateEventUserRequest Данные для обновления события.
     * @return {@link EventFullDto}     Обновлённое событие в виде DTO.
     * @throws DataTimeException      Если дата события в прошлом или менее чем через два часа от текущего времени.
     * @throws StateValidateException Если состояние события уже опубликовано.
     */
    @Override
    public EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest updateEventUserRequest) {
        log.debug("Обновление события ID: {}\n{}", eventId, updateEventUserRequest);
        userServiceImpl.getUserByIdOrThrow(userId);
        Event event = getEventAndCheckAuthorization(userId, eventId);

        if (updateEventUserRequest.getEventDate() != null) {
            if (updateEventUserRequest.getEventDate().isBefore(LocalDateTime.now())) {
                throw new DataTimeException("Дата и время на которые намечено событие не может быть в прошлом");
            } else if (updateEventUserRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new DataTimeException("Дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента");
            }
        }

        if (event.getState().equals(State.PUBLISHED)) {
            throw new StateValidateException("Можно изменить только отложенные или отмененные события");
        }

        Event builderEvent = buildPrivateEventForUpdate(updateEventUserRequest, event);
        Event savedEvent = eventRepository.save(builderEvent);
        setViews(List.of(savedEvent));
        log.info("Событие обновлено:\n{}", savedEvent);
        return eventMapper.toEventFullDto(savedEvent);
    }

    /**
     * Получает запросы на участие в событии для пользователя.
     *
     * @param userId  ID пользователя.
     * @param eventId ID события.
     * @return Список DTO запросов на участие.
     */
    @Override
    public List<ParticipationRequestDto> getParticipationRequestsForUserEventsPrivate(long userId, long eventId) {
        log.debug("Private:Получение запросов на участие в событии. ID пользователя: {}, ID события :{}", userId, eventId);
        userServiceImpl.getUserByIdOrThrow(userId);
        getEventAndCheckAuthorization(userId, eventId);
        List<Request> requests = requestRepository.findByEventId(eventId);
        log.info("Private:Получен список запросов на участие.\n{}", requests);
        return requests.stream().map(requestMapper::toParticipationRequestDto).toList();
    }

    /**
     * Обновляет статус заявок на участие в событии.
     *
     * @param userId                          ID пользователя, инициировавшего изменение статуса заявки.
     * @param eventId                         ID события, для которого обновляются заявки.
     * @param eventRequestStatusUpdateRequest Данные для обновления статуса заявок.
     * @return {@link EventRequestStatusUpdateResult} Результат обновления статусов заявок.
     * @throws ValidationException Если превышен лимит заявок или если статус заявки не может быть изменён.
     */
    @Override
    public EventRequestStatusUpdateResult updateRequestStatusPrivate(long userId, long eventId,
                                                                     EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.debug("Private:Изменение статуса (подтверждена, отменена) заявок на участие в событии. " +
                "ID пользователя: {}, ID события :{}", userId, eventId);
        userServiceImpl.getUserByIdOrThrow(userId);
        Event event = getEventAndCheckAuthorization(userId, eventId);

        if (event.getParticipantLimit() == 0 || event.getRequestModeration().equals(false)) {
            String message = "";
            if (event.getParticipantLimit() == 0) {
                message = "Лимит заявок равен 0, ";
            }
            if (event.getRequestModeration().equals(false)) {
                message = "Отключена пре-модерация заявок, ";
            }
            throw new ValidationException(message + "подтверждение заявки не требуется");
        }

        if (event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            throw new ValidationException("Достигнут лимит по заявкам на данное событие");
        }

        List<Request> requests = requestRepository.findAllById(eventRequestStatusUpdateRequest.getRequestIds());
        log.info("Private:Заявки для изменения статуса:\n{}", requests);

        List<Request> confirmedRequests = new ArrayList<>();
        List<Request> rejectedRequests = new ArrayList<>();

        for (Request request : requests) {
            if (request.getStatus() != RequestState.PENDING) {
                throw new ValidationException("Статус можно изменить только у заявок, находящихся в состоянии ожидания");
            }
            if (eventRequestStatusUpdateRequest.getStatus() == RequestState.CONFIRMED) {
                if (event.getConfirmedRequests() < event.getParticipantLimit()) {
                    request.setStatus(RequestState.CONFIRMED);
                    confirmedRequests.add(request);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                }
            } else {
                request.setStatus(RequestState.REJECTED);
                rejectedRequests.add(request);
            }
        }

        eventRepository.save(event);
        requestRepository.saveAll(confirmedRequests);

        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedRequests.stream().map(requestMapper::toParticipationRequestDto).toList())
                .rejectedRequests(rejectedRequests.stream().map(requestMapper::toParticipationRequestDto).toList())
                .build();
        log.info("Private:Статусы изменены заявок на участие изменены.\n{}", eventRequestStatusUpdateResult);
        return eventRequestStatusUpdateResult;
    }

    /**
     * Получение всех событий для администратора с фильтрацией по пользователям, состояниям, категориям и времени.
     *
     * @param users      Список ID пользователей, чьи события нужно получить.
     * @param states     Список состояний событий.
     * @param categories Список категорий событий.
     * @param rangeStart Время начала диапазона событий.
     * @param rangeEnd   Время окончания диапазона событий.
     * @param from       Смещение для пагинации.
     * @param size       Размер страницы для пагинации.
     * @return Список полных DTO событий.
     */
    @Override
    public List<EventFullDto> getAllEventsAdmin(List<Long> users, List<State> states, List<Long> categories,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        log.debug("""
                Admin:Получение списка по поиску событий:
                Список ID пользователей: {}
                Список состояний: {}
                Список ID категорий: {}
                Дата и время не раньше которых должно произойти событие: {}
                Дата и время не позже которых должно произойти событие: {}
                 from: {}, size: {}""", users, states, categories, rangeStart, rangeEnd, from, size);
        Pageable pageable = createPageable(from, size, Sort.by(Sort.Direction.ASC, "createdOn"));
        Page<Event> eventPage;
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (users != null && !users.isEmpty()) {
            booleanBuilder.and(event.initiator.id.in(users));
        }
        if (states != null && !states.isEmpty()) {
            booleanBuilder.and(event.state.in(states));
        }
        if (categories != null && !categories.isEmpty()) {
            booleanBuilder.and(event.category.id.in(categories));
        }

        checkTime(rangeStart, rangeEnd, booleanBuilder);

        if (booleanBuilder.getValue() != null) {
            eventPage = eventRepository.findAll(booleanBuilder, pageable);
        } else {
            eventPage = eventRepository.findAll(pageable);
        }
        List<Event> events = eventPage.getContent();
        setViews(events);
        log.info("Admin:Получен список по поиску событий.\n{}", events);
        return events.stream().map(eventMapper::toEventFullDto).collect(Collectors.toList());
    }

    /**
     * Обновление события для администратора.
     *
     * @param eventId                 ID события, которое нужно обновить.
     * @param updateEventAdminRequest Запрос на обновление события.
     * @return Полное DTO обновленного события.
     * @throws DataTimeException   Если дата начала события меньше чем на час от времени публикации.
     * @throws ValidationException Если событие уже опубликовано или отменено.
     */
    @Override
    public EventFullDto updateEvent(long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        log.debug("Admin:Обновление события ID: {}\n{}", eventId, updateEventAdminRequest);
        Event event = getEventByIdOrThrow(eventId);
        if (updateEventAdminRequest.getEventDate() != null) {
            if (updateEventAdminRequest.getEventDate().isBefore(event.getPublishedOn().plusHours(1))) {
                throw new DataTimeException("Дата начала изменяемого события должна быть не ранее чем за час от даты публикации");
            }
        }

        if (event.getState() == State.PUBLISHED) {
            throw new ValidationException("Событие уже опубликовано");
        }
        if (event.getState() == State.CANCELED) {
            throw new ValidationException("Нельзя опубликовать отменённое событие");
        }
        Event builderEvent = buildAdminEventForUpdate(updateEventAdminRequest, event);
        setViews(List.of(builderEvent));
        Event savedEvent = eventRepository.save(builderEvent);
        log.info("Событие обновлено :\n{}", savedEvent);
        return eventMapper.toEventFullDto(savedEvent);
    }

    /**
     * Получение события по ID для публичного доступа.
     *
     * @param id      ID события.
     * @param request HTTP-запрос для отслеживания хитов.
     * @return Полное DTO события.
     * @throws NotFoundException Если событие не найдено или не опубликовано.
     */
    @Override
    public EventFullDto getEventByIdPublic(int id, HttpServletRequest request) {
        log.debug("Public:Получение события, ID события:{}", id);
        Event event = getEventByIdOrThrow(id);

        if (event.getState() != State.PUBLISHED) {
            throw new NotFoundException("Событие не найдено.");
        }
        createHit(request);
        setViews(List.of(event));
        log.info("Public:Получено событие :\n{}", event);
        return eventMapper.toEventFullDto(event);
    }

    /**
     * Получение всех событий с фильтрацией для публичного доступа.
     *
     * @param text          Текст для поиска в аннотации или описании событий.
     * @param categories    Список категорий для фильтрации.
     * @param paid          Фильтрация по статусу оплаты.
     * @param rangeStart    Дата и время начала диапазона событий.
     * @param rangeEnd      Дата и время окончания диапазона событий.
     * @param onlyAvailable Фильтрация по доступным событиям (с доступными участниками).
     * @param sort          Тип сортировки событий.
     * @param from          Смещение для пагинации.
     * @param size          Размер страницы для пагинации.
     * @param request       HTTP-запрос для отслеживания хитов.
     * @return Список кратких DTO событий.
     */
    @Override
    public List<EventShortDto> getAllEventsPublic(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                                  LocalDateTime rangeEnd, Boolean onlyAvailable, String sort,
                                                  int from, int size, HttpServletRequest request) {
        log.debug("""
                Public:Получение событии с фильтрацией.
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
        Page<Event> eventPage;
        Pageable pageable;

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (text != null && !text.isEmpty()) {
            booleanBuilder.and(event.annotation.containsIgnoreCase(text))
                    .or(event.description.containsIgnoreCase(text));
        }

        if (categories != null && !categories.isEmpty()) {
            booleanBuilder.and(event.category.id.in(categories));
        }

        if (paid != null) {
            booleanBuilder.and(event.paid.eq(paid));
        }

        checkTime(rangeStart, rangeEnd, booleanBuilder);

        if (onlyAvailable) {
            booleanBuilder.and(event.participantLimit.eq(0L).or(event.confirmedRequests
                    .lt(event.participantLimit)));
        }

        if (sort != null && !sort.isEmpty()) {
            pageable = createPageable(from, size, Sort.unsorted());
            if (sort.equals("EVENT_DATE")) {
                pageable = createPageable(from, size, Sort.by(Sort.Direction.ASC, "eventDate"));
            }
            if (sort.equals("VIEWS")) {
                pageable = createPageable(from, size, Sort.by(Sort.Direction.DESC, "views"));
            }
        } else {
            pageable = createPageable(from, size, Sort.unsorted());
        }
        log.info("booleanBuilder; {}", booleanBuilder.getValue());

        if (booleanBuilder.getValue() != null) {
            eventPage = eventRepository.findAll(booleanBuilder, pageable);
        } else {
            eventPage = eventRepository.findAll(pageable);
        }
        List<Event> events = eventPage.getContent();
        createHit(request);
        setViews(events);
        log.info("Public:Получены события :\n{}", events);
        return events.stream().map(eventMapper::toEventShortDto).collect(Collectors.toList());
    }

    /**
     * Получает событие по ID или выбрасывает исключение, если событие не найдено.
     *
     * @param eventId ID события.
     * @return Событие с указанным ID.
     * @throws NotFoundException если событие не найдено.
     */
    @Override
    public Event getEventByIdOrThrow(long eventId) {
        log.info("Попытка получения События по ID: {}", eventId);
        return eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(
                "Event with id = " + eventId + " was not found"));
    }

    /**
     * Получает событие и проверяет, является ли пользователь владельцем этого события.
     *
     * @param userId  ID пользователя.
     * @param eventId ID события.
     * @return Событие с указанным ID.
     * @throws NotFoundException      если событие не найдено.
     * @throws AuthorizationException если пользователь не является владельцем события.
     */
    @Override
    public Event getEventAndCheckAuthorization(long userId, long eventId) {
        log.info("Проверка авторизации, Пользователь ID: {}, Событие ID: {}", userId, eventId);
        Event event = getEventByIdOrThrow(eventId);
        if (event.getInitiator().getId() != userId) {
            throw new AuthorizationException("Пользователь ID: " + userId +
                    " не является владельцем события ID: " + eventId);
        }
        return event;
    }

    /**
     * Создаёт объект Pageable для пагинации с указанными параметрами.
     *
     * @param from Начальный индекс для пагинации.
     * @param size Размер страницы.
     * @param sort Параметры сортировки.
     * @return Объект Pageable для дальнейшего использования в запросах.
     */
    private Pageable createPageable(int from, int size, Sort sort) {
        log.debug("Create Pageable with offset from {}, size {}", from, size);
        return PageRequest.of(from / size, size, sort);
    }

    /**
     * Обновляет событие на основе данных из запроса для пользователя.
     *
     * @param updateEventUserRequest Данные запроса для обновления события.
     * @param event                  Существующее событие, которое нужно обновить.
     * @return Обновлённое событие.
     */
    private Event buildPrivateEventForUpdate(UpdateEventUserRequest updateEventUserRequest, Event event) {
        Optional.ofNullable(updateEventUserRequest.getAnnotation()).ifPresent(event::setAnnotation);
        if (updateEventUserRequest.getCategory() != null) {
            Category category = categoryServiceImpl.getCategoryByIdOrThrow(updateEventUserRequest.getCategory());
            event.setCategory(category);
        }
        Optional.ofNullable(updateEventUserRequest.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(updateEventUserRequest.getEventDate()).ifPresent(event::setEventDate);
        if (updateEventUserRequest.getLocation() != null) {
            Location location = locationMapper.toLocation(updateEventUserRequest.getLocation());
            boolean checkExistLocation = locationRepository.existsByLatAndLon(location.getLat(), location.getLon());
            if (!checkExistLocation) {
                locationRepository.save(location);
            }
            event.setLocation(location);
        }
        Optional.ofNullable(updateEventUserRequest.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(updateEventUserRequest.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(updateEventUserRequest.getRequestModeration()).ifPresent(event::setRequestModeration);
        if (updateEventUserRequest.getStateAction() != null) {
            switch (updateEventUserRequest.getStateAction()) {
                case CANCEL_REVIEW -> event.setState(State.CANCELED);
                case SEND_TO_REVIEW -> event.setState(State.PENDING);
            }
        }
        Optional.ofNullable(updateEventUserRequest.getTitle()).ifPresent(event::setTitle);
        return event;
    }

    /**
     * Обновляет событие на основе данных из запроса для администратора.
     *
     * @param updateEventAdminRequest Данные запроса для обновления события.
     * @param event                   Существующее событие, которое нужно обновить.
     * @return Обновлённое событие.
     */
    private Event buildAdminEventForUpdate(UpdateEventAdminRequest updateEventAdminRequest, Event event) {
        Optional.ofNullable(updateEventAdminRequest.getAnnotation()).ifPresent(event::setAnnotation);
        log.debug("Обновлено поле annotation: {}", updateEventAdminRequest.getAnnotation());
        if (updateEventAdminRequest.getCategory() != null) {
            Category category = categoryServiceImpl.getCategoryByIdOrThrow(updateEventAdminRequest.getCategory());
            event.setCategory(category);
            log.debug("Обновлено поле category: {}", updateEventAdminRequest.getCategory());
        }
        Optional.ofNullable(updateEventAdminRequest.getDescription()).ifPresent(event::setDescription);
        log.debug("Обновлено поле description: {}", updateEventAdminRequest.getDescription());
        Optional.ofNullable(updateEventAdminRequest.getEventDate()).ifPresent(event::setEventDate);
        log.debug("Обновлено поле eventDate: {}", updateEventAdminRequest.getEventDate());
        if (updateEventAdminRequest.getLocation() != null) {
            Location location = locationMapper.toLocation(updateEventAdminRequest.getLocation());
            boolean checkExistLocation = locationRepository.existsByLatAndLon(location.getLat(), location.getLon());
            if (!checkExistLocation) {
                locationRepository.save(location);
            }
            event.setLocation(location);
            log.debug("Обновлено поле location: {}", updateEventAdminRequest.getLocation());
        }
        Optional.ofNullable(updateEventAdminRequest.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(updateEventAdminRequest.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(updateEventAdminRequest.getRequestModeration()).ifPresent(event::setRequestModeration);
        if (updateEventAdminRequest.getStateAction() != null) {
            switch (updateEventAdminRequest.getStateAction()) {
                case PUBLISH_EVENT -> event.setState(State.PUBLISHED);
                case REJECT_EVENT -> event.setState(State.CANCELED);
            }
        }
        Optional.ofNullable(updateEventAdminRequest.getTitle()).ifPresent(event::setTitle);
        return event;
    }

    /**
     * Проверяет корректность временных рамок для событий.
     *
     * @param rangeStart     Начало временного интервала.
     * @param rangeEnd       Конец временного интервала.
     * @param booleanBuilder Строитель логического выражения для фильтрации.
     * @throws DataTimeException если время начала позже времени окончания.
     */
    private void checkTime(LocalDateTime rangeStart, LocalDateTime rangeEnd, BooleanBuilder booleanBuilder) {
        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new DataTimeException("Время начала должно быть не позже времени окончания.");
            }
            booleanBuilder.and(event.eventDate.between(rangeStart, rangeEnd));
        } else if (rangeStart == null && rangeEnd != null) {
            booleanBuilder.and(event.eventDate.before(rangeEnd));
        } else if (rangeStart != null) {
            booleanBuilder.and(event.eventDate.after(rangeStart));
        }
    }

    /**
     * Отправляет информацию о визите на сервер статистики.
     *
     * @param request HTTP запрос для получения информации о визите.
     */
    private void createHit(HttpServletRequest request) {
        DtoEndpointHit dtoEndpointHit = DtoEndpointHit.builder()
                .app(serviceName)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
        log.info("dtoEndpointHit: {}", dtoEndpointHit);
        statisticsClient.createHit(dtoEndpointHit);
    }

    /**
     * Получает статистику просмотров для списка событий.
     *
     * @param events Список событий.
     * @return Список статистики просмотров.
     */
    private List<DtoViewStats> getStats(List<Event> events) {
        List<String> uris = events.stream().map(event -> "/events/" + event.getId()).toList();
        log.info("uris: {}", uris);
        LocalDateTime start = LocalDateTime.now().minusYears(50);
        LocalDateTime end = LocalDateTime.now();
        List<DtoViewStats> stats = statisticsClient.getStats(start, end, uris, true);
        log.info("stats: {}", stats);
        if (stats.isEmpty()) {
            return Collections.emptyList();
        }
        return stats;
    }

    /**
     * Устанавливает количество просмотров для списка событий.
     *
     * @param events Список событий.
     */
    private void setViews(List<Event> events) {
        log.info("events: {}", events);
        if (events.isEmpty()) {
            return;
        }
        Map<String, Long> mapUriAndHits = getStats(events).stream()
                .collect(Collectors.toMap(DtoViewStats::getUri, DtoViewStats::getHits));
        log.info("mapUriAndHits: {}", mapUriAndHits);

        for (Event event : events) {
            event.setViews(mapUriAndHits.getOrDefault("/events/" + event.getId(), 0L));
        }
    }
}