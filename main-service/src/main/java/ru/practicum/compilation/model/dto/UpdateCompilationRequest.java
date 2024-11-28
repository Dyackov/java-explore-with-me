package ru.practicum.compilation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

/**
 * DTO для обновления информации о подборке.
 * Содержит информацию о событиях, закреплении и названии подборки.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationRequest {

    /**
     * Список идентификаторов событий, которые должны быть включены в подборку.
     * Все элементы списка должны быть уникальными.
     */
    @UniqueElements
    private List<Long> events;

    /**
     * Флаг, показывающий, является ли подборка закрепленной.
     */
    private Boolean pinned;

    /**
     * Название подборки. Должно быть не пустым и длина не менее 1 и не более 50 символов.
     */
    @Length(min = 1, max = 50)
    private String title;
}