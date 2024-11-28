package ru.practicum.comment.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.comment.model.enums.StatusComment;

/**
 * DTO для обновления статуса комментария администраторами.
 * Используется для изменения статуса комментария на основании административного запроса.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStatusCommentAdmin {

    /**
     * Новый статус комментария. Не может быть null.
     */
    @NotNull(message = "Статус комментария не может быть null.")
    private StatusComment status;
}
