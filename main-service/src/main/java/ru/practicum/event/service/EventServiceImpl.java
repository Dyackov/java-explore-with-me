package ru.practicum.event.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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
import ru.practicum.request.model.Request;
import ru.practicum.request.model.dto.ParticipationRequestDto;
import ru.practicum.request.model.enums.RequestState;
import ru.practicum.request.model.mapper.RequestMapper;
import ru.practicum.request.storage.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.practicum.event.model.QEvent.event;

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

    @Override
    public List<EventShortDto> getEventsByUserIdPrivate(long userId, int from, int size) {
        log.debug("Получение всех событий. ID пользователя:{}, from:{}, size:{}", userId, from, size);
        userServiceImpl.getUserByIdOrThrow(userId);
        Pageable pageable = createPageable(from, size, Sort.by(Sort.Direction.ASC, "createdOn"));
        List<Event> events = eventRepository.findAll(pageable).getContent();
        log.info("Получен список всех событий. ID пользователя:\n{}", events);
        return events.stream().map(eventMapper::toEventShortDto).toList();
    }

    @Override
    public EventFullDto createEventPrivate(long userId, NewEventDto newEventDto) {
        log.debug("Создание события: {}", newEventDto);
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
        log.info("Создано событие:\n{}", savedEvent);

        return eventMapper.toEventFullDto(savedEvent);
    }

    @Override
    public EventFullDto getEventByIdPrivate(long userId, long eventId) {
        log.debug("Получение события, ID пользователя: {}, ID события:{}", userId, eventId);
        getEventByIdOrThrow(eventId);
        Event event = getEventAndCheckAuthorization(userId, eventId);
        log.info("Получено событие:\n{}", event);
        return eventMapper.toEventFullDto(event);
    }


    @Override
    public EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest updateEventUserRequest) {
        log.debug("Обновление события ID: {}\n{}", eventId, updateEventUserRequest);
        userServiceImpl.getUserByIdOrThrow(userId);
        Event event = getEventAndCheckAuthorization(userId, eventId);
        if (updateEventUserRequest.getEventDate() != null) {
            if (updateEventUserRequest.getEventDate().isBefore(LocalDateTime.now())) {
                throw new DataTimeException("Дата и время на которые намечено событие не может быть в прошлом");
            } else if (updateEventUserRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new DataTimeException("Дата и время на которые намечено событие не может быть раньше, " +
                        "чем через два часа от текущего момента");
            }
        }
        if (event.getState().equals(State.PUBLISHED)) {
            throw new StateValidateException("Можно изменить только отложенные или отмененные события");
        }
        Event builderEvent = buildPrivateEventForUpdate(updateEventUserRequest, event);
        Event savedEvent = eventRepository.save(builderEvent);
        log.info("Событие обновлено:\n{}", savedEvent);
        return eventMapper.toEventFullDto(savedEvent);
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequestsForUserEventsPrivate(long userId, long eventId) {
        log.debug("Получение запросов на участие в событии. ID пользователя: {}, ID события :{}", userId, eventId);
        userServiceImpl.getUserByIdOrThrow(userId);
        getEventAndCheckAuthorization(userId, eventId);
        List<Request> requests = requestRepository.findByEventId(eventId);
        log.info("Получен список запросов на участие.\n{}", requests);
        return requests.stream().map(requestMapper::toParticipationRequestDto).toList();
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestStatusPrivate(long userId, long eventId,
                                                                     EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("Изменение статуса (подтверждена, отменена) заявок на участие в событии. " +
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
        log.info("Заявки для изменения статуса:\n{}", requests);

        List<Request> confirmedRequests = new ArrayList<>();
        List<Request> rejectedRequests = new ArrayList<>();

        for (Request request : requests) {
            if (request.getStatus() != RequestState.PENDING) {
                throw new ValidationException("Статус можно изменить только у заявок, находящихся в состоянии ожидания");
            }
            if (event.getConfirmedRequests() < event.getParticipantLimit()) {
                request.setStatus(RequestState.CONFIRMED);
                confirmedRequests.add(request);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
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
        log.info("Статусы изменены заявок на участие изменены.\n{}", eventRequestStatusUpdateResult);
        return eventRequestStatusUpdateResult;
    }

    @Override
    public List<EventFullDto> getAllEventsAdmin(List<Long> users, List<State> states, List<Long> categories,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        log.debug("""
                Получение списка по поиску событий:
                Список ID пользователей: {}\
                Список состояний: {}\
                Список ID категорий: {}\
                Дата и время не раньше которых должно произойти событие: {}\
                Дата и время не позже которых должно произойти событие: {}\
                 from: {}, size: {}""", users, states, categories, rangeStart, rangeEnd, from, size);
        Pageable pageable = createPageable(from, size, Sort.by(Sort.Direction.ASC, "createdOn"));
        List<Event> events = eventRepository.findEvents(users, states, categories, rangeStart, rangeEnd, pageable);
        log.info("Получен список по поиску событий.\n{}", events);
        return events.stream().map(eventMapper::toEventFullDto).toList();
    }

    @Override
    public EventFullDto updateEvent(long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        log.debug("admin Обновление события ID: {}\n{}", eventId, updateEventAdminRequest);
        Event event = getEventByIdOrThrow(eventId);
        if (updateEventAdminRequest.getEventDate() != null) {
            if (updateEventAdminRequest.getEventDate().isBefore(event.getPublishedOn().plusHours(1))) {
                throw new DataTimeException("Дата начала изменяемого события должна быть не ранее чем за час от даты публикации");
            }
        }

        if (event.getState() == State.PUBLISHED) {
            throw new ValidationException("Событие уже опубликовано");
        }
        Event builderEvent = buildAdminEventForUpdate(updateEventAdminRequest, event);
        Event savedEvent = eventRepository.save(builderEvent);
        log.info("Событие обновлено :\n{}", savedEvent);
        return eventMapper.toEventFullDto(savedEvent);
    }

    @Override
    public EventFullDto getEventByIdPublic(int id) {
        log.debug("Получение события, ID события:{}", id);
        Event event = getEventByIdOrThrow(id);

        if (event.getState() != State.PUBLISHED) {
            throw new NotFoundException("Событие не найдено.");
        }
        log.info("Получено событие :\n{}", event);
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventShortDto> getAllEventsPublic(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                                  LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, int from, int size) {
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
        return events.stream().map(eventMapper::toEventShortDto).toList();
    }


    @Override
    public Event getEventByIdOrThrow(long eventId) {
        log.info("Попытка получения События по ID: {}", eventId);
        return eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(
                "Event with id = " + eventId + " was not found"));
    }

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

    private Pageable createPageable(int from, int size, Sort sort) {
        log.debug("Create Pageable with offset from {}, size {}", from, size);
        return PageRequest.of(from / size, size, sort);
    }

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

    private Event buildAdminEventForUpdate(UpdateEventAdminRequest updateEventAdminRequest, Event event) {
        Optional.ofNullable(updateEventAdminRequest.getAnnotation()).ifPresent(event::setAnnotation);
        if (updateEventAdminRequest.getCategory() != null) {
            Category category = categoryServiceImpl.getCategoryByIdOrThrow(updateEventAdminRequest.getCategory());
            event.setCategory(category);
        }
        Optional.ofNullable(updateEventAdminRequest.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(updateEventAdminRequest.getEventDate()).ifPresent(event::setEventDate);
        if (updateEventAdminRequest.getLocation() != null) {
            Location location = locationMapper.toLocation(updateEventAdminRequest.getLocation());
            boolean checkExistLocation = locationRepository.existsByLatAndLon(location.getLat(), location.getLon());
            if (!checkExistLocation) {
                locationRepository.save(location);
            }
            event.setLocation(location);
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
}


