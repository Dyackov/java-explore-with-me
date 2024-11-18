package ru.practicum.category.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) для категории.
 * Используется для передачи данных о категории в ответах API.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    /**
     * Уникальный идентификатор категории.
     */
    private Long id;

    /**
     * Название категории.
     */
    private String name;
}