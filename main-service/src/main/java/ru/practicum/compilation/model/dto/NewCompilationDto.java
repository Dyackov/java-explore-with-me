package ru.practicum.compilation.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

/**
 * DTO для создания новой подборки.
 * Содержит информацию о подборке, её событиях, закреплении и названии.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {

    /**
     * Список идентификаторов событий, которые должны быть включены в подборку.
     */
    @UniqueElements
    private List<Long> events;

    /**
     * Флаг, показывающий, является ли подборка закрепленной.
     * По умолчанию: false.
     */
    @NotNull
    private Boolean pinned = Boolean.FALSE;

    /**
     * Название подборки. Должно быть не пустым и длина не менее 1 и не более 50 символов.
     */
    @NotBlank
    @Length(min = 1, max = 50)
    private String title;
}
