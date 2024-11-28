package ru.practicum.category.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) для создания новой категории.
 * Используется для получения данных от пользователя для создания категории через API.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {

    /**
     * Название категории.
     * Должно быть обязательным для заполнения (не пустое) и иметь длину от 1 до 50 символов.
     */
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
}