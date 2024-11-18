package ru.practicum.error.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Модель для представления ошибки в формате JSON.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ApiError {
    /**
     * Статус HTTP-ответа.
     */
    private String status;

    /**
     * Причина ошибки.
     */
    private String reason;

    /**
     * Сообщение об ошибке.
     */
    private String message;

    /**
     * Время, когда произошла ошибка.
     * Формат: yyyy-MM-dd HH:mm:ss.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
