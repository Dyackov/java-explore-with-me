package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.category.service.CategoryService;
import ru.practicum.error.exception.AuthorizationException;
import ru.practicum.error.exception.DataTimeException;
import ru.practicum.error.exception.NotFoundException;
import ru.practicum.error.exception.StateValidateException;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.dto.EventFullDto;
import ru.practicum.event.model.dto.EventShortDto;
import ru.practicum.event.model.dto.NewEventDto;
import ru.practicum.event.model.dto.UpdateEventUserRequest;
import ru.practicum.event.model.enums.State;
import ru.practicum.event.model.mapper.EventMapper;
import ru.practicum.event.model.mapper.LocationMapper;
import ru.practicum.event.storage.EventRepository;
import ru.practicum.event.storage.LocationRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;

    private final CategoryService categoryServiceImpl;
    private final UserService userServiceImpl;

    private final EventMapper eventMapper;
    private final LocationMapper locationMapper;

    @Override
    public List<EventShortDto> getEventsByUserId(long userId, int from, int size) {
        log.debug("Получение всех событий. ID пользователя:{}, from:{}, size:{}", userId, from, size);
        userServiceImpl.getUserByIdOrThrow(userId);
        Pageable pageable = createPageable(from, size, Sort.by(Sort.Direction.ASC, "createdOn"));
        List<Event> events = eventRepository.findAll(pageable).getContent();
        log.info("Получен список всех событий. ID пользователя:\n{}", events);
        return events.stream().map(eventMapper::toEventShortDto).toList();
    }

    @Override
    public EventFullDto createEvent(long userId, NewEventDto newEventDto) {
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
    public EventFullDto getEventById(long userId, long eventId) {
        log.debug("Получение события, ID пользователя: {}, ID события:{}", userId, eventId);
        getEventByIdOrThrow(eventId);
        Event event = getEventAndCheckAuthorization(userId, eventId);
        log.info("Получено событие:\n{}", event);
        return eventMapper.toEventFullDto(event);
    }


    @Override
    public EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest updateEventUserRequest) {
        log.debug("Обновление события ID: {}\n{}", eventId, updateEventUserRequest);
        User user = userServiceImpl.getUserByIdOrThrow(userId);
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
        Event builderEvent = buildEventForUpdate(updateEventUserRequest, event);
        Event savedEvent = eventRepository.save(builderEvent);
        log.info("Событие обновлено:\n{}", savedEvent);
        return eventMapper.toEventFullDto(savedEvent);
    }


    @Override
    public Event getEventByIdOrThrow(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(
                "Event with id = " + eventId + " was not found"));
    }

    @Override
    public Event getEventAndCheckAuthorization(long userId, long eventId) {
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


    private Event buildEventForUpdate(UpdateEventUserRequest updateEventUserRequest, Event event) {
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
}


