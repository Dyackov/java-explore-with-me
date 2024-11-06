package ru.practicum.category.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class NewCategoryDto {
    @NotNull
    @NotEmpty(message = "Имя категории не может быть пустым")
    private String name;
}
