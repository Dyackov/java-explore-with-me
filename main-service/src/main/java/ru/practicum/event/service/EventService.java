package ru.practicum.event.service;

import ru.practicum.event.model.Event;
import ru.practicum.event.model.dto.EventFullDto;
import ru.practicum.event.model.dto.EventShortDto;
import ru.practicum.event.model.dto.NewEventDto;
import ru.practicum.event.model.dto.UpdateEventUserRequest;

import java.util.List;

public interface EventService {

    List<EventShortDto> getEventsByUserId(long userId, int from, int size);

    EventFullDto createEvent(long userId, NewEventDto newEventDto);

    EventFullDto getEventById(long userId, long eventId);

    EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest updateEventUserRequest);

    Event getEventByIdOrThrow(long eventId);

    Event getEventAndCheckAuthorization(long userId, long eventId);


}
