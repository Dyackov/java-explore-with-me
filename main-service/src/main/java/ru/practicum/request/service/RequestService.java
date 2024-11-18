package ru.practicum.request.service;

import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    List<ParticipationRequestDto> getRequestsPrivate(@PathVariable Long userId);

    ParticipationRequestDto createRequestPrivate(long userId, long eventId);

    ParticipationRequestDto updateRequestPrivate(long userId, long requestId);

    Request getRequestByIdOrThrow(long requestId);
}
