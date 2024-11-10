package ru.practicum.request.service;

import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    List<ParticipationRequestDto> getRequests(@PathVariable Long userId);

    ParticipationRequestDto createRequest(long userId, long eventId);

    ParticipationRequestDto updateRequest(long userId, long requestId);

    Request getRequestByIdOrThrow(long requestId);
}
