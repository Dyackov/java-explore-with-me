package ru.practicum.request.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.request.model.enums.RequestState;

import java.time.LocalDateTime;

/**
 * DTO для представления заявки на участие в событии.
 * Содержит информацию о заявке, ее статусе и времени создания.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {

    /**
     * Идентификатор заявки.
     */
    private Long id;

    /**
     * Время создания заявки.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    /**
     * Идентификатор события, на которое сделана заявка.
     */
    @NotNull
    private Long event;

    /**
     * Идентификатор пользователя, сделавшего заявку.
     */
    @NotNull
    private Long requester;

    /**
     * Статус заявки.
     */
    private RequestState status;
}
