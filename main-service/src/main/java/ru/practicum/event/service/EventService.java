package ru.practicum.event.service;

import ru.practicum.event.model.Event;
import ru.practicum.event.model.dto.*;
import ru.practicum.event.model.enums.State;
import ru.practicum.request.model.dto.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    List<EventShortDto> getEventsByUserIdPrivate(long userId, int from, int size);

    EventFullDto createEventPrivate(long userId, NewEventDto newEventDto);

    EventFullDto getEventByIdPrivate(long userId, long eventId);

    EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getParticipationRequestsForUserEventsPrivate(long userId, long eventId);

    EventRequestStatusUpdateResult updateRequestStatusPrivate(long userId, long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    List<EventFullDto> getAllEventsAdmin(List<Long> users,
                                         List<State> states,
                                         List<Long> categories,
                                         LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd,
                                         int from,
                                         int size);

    EventFullDto updateEvent(long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    EventFullDto getEventByIdPublic(int id);

    List<EventShortDto> getAllEventsPublic(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, int from, int size);

    Event getEventByIdOrThrow(long eventId);

    Event getEventAndCheckAuthorization(long userId, long eventId);

}
