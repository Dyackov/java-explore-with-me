package ru.practicum.comment.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для обновления комментария. Содержит новый текст комментария.
 * Используется при обновлении существующего комментария в системе.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentDto {

    /**
     * Новый текст комментария. Должен быть не пустым и содержать от 5 до 1000 символов.
     */
    @Size(min = 5, max = 1000, message = "Текст комментария должен содержать от 5 до 1000 символов.")
    @NotBlank(message = "Текст комментария не может быть пустым.")
    private String text;
}
