package ru.practicum.event.service;

import ru.practicum.event.model.Event;
import ru.practicum.event.model.dto.*;
import ru.practicum.request.model.dto.ParticipationRequestDto;

import java.util.List;

public interface EventService {

    List<EventShortDto> getEventsByUserId(long userId, int from, int size);

    EventFullDto createEvent(long userId, NewEventDto newEventDto);

    EventFullDto getEventById(long userId, long eventId);

    EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getParticipationRequestsForUserEvents(long userId, long eventId);

    EventRequestStatusUpdateResult updateRequestStatus(long userId, long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    Event getEventByIdOrThrow(long eventId);

    Event getEventAndCheckAuthorization(long userId, long eventId);


}
