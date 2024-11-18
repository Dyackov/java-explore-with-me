package ru.practicum.error.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Модель для представления нескольких ошибок в формате JSON.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ApiErrors {
    /**
     * Список ошибок.
     * По умолчанию создается пустой список.
     */
    @Builder.Default
    private List<String> errors = new ArrayList<>();

    /**
     * Сообщение об ошибке или ошибки.
     */
    private String message;

    /**
     * Причина ошибки.
     */
    private String reason;

    /**
     * Статус HTTP-ответа.
     */
    private String status;

    /**
     * Время, когда произошла ошибка.
     * Формат: yyyy-MM-dd HH:mm:ss.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
