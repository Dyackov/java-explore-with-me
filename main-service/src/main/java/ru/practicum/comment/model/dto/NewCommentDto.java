package ru.practicum.comment.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для создания нового комментария. Содержит только текст комментария.
 * Используется при создании нового комментария в системе.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCommentDto {

    /**
     * Текст комментария. Должен быть не пустым, содержать от 2 до 1000 символов.
     */
    @Size(min = 2, max = 1000, message = "Текст комментария должен содержать от 2 до 1000 символов.")
    @NotBlank(message = "Текст комментария не может быть пустым.")
    private String text;
}