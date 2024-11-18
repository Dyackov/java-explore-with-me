package ru.practicum.event.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.request.model.dto.ParticipationRequestDto;

import java.util.List;

/**
 * DTO класс для результата обновления статуса заявок на участие в событии.
 * Содержит информацию о подтвержденных и отклоненных заявках.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventRequestStatusUpdateResult {

    /**
     * Список подтвержденных заявок на участие в событии.
     */
    private List<ParticipationRequestDto> confirmedRequests;

    /**
     * Список отклоненных заявок на участие в событии.
     */
    private List<ParticipationRequestDto> rejectedRequests;
}
