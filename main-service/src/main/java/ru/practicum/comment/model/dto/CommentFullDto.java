package ru.practicum.comment.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.comment.model.enums.StatusComment;
import ru.practicum.event.model.dto.EventShortDto;
import ru.practicum.user.model.dto.UserShortDto;

import java.time.LocalDateTime;

/**
 * Полное DTO для комментария, включающее подробную информацию о комментарии, пользователе, событии и статусе.
 * Содержит все данные комментария, включая информацию о пользователе-комментаторе и событии, к которому относится комментарий.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentFullDto {
    /**
     * Идентификатор комментария.
     */
    private Long id;

    /**
     * Текст комментария.
     */
    private String text;

    /**
     * Пользователь, который оставил комментарий.
     */
    private UserShortDto commentator;

    /**
     * Событие, к которому относится комментарий.
     */
    private EventShortDto event;

    /**
     * Дата и время создания комментария.
     * Формат: "yyyy-MM-dd HH:mm:ss"
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    /**
     * Статус комментария (например, одобрен/отклонен).
     */
    private StatusComment status;
}