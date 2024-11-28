package ru.practicum.comment.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.comment.model.enums.StatusComment;

import java.time.LocalDateTime;

/**
 * DTO для представления комментария в упрощенном виде.
 * Содержит основные данные комментария, которые будут отображаться в ответах API.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    /**
     * Идентификатор комментария.
     */
    private Long id;

    /**
     * Текст комментария.
     */
    private String text;

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