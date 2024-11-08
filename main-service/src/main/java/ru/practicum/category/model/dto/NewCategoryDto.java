package ru.practicum.category.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {
    @NotNull
    @NotEmpty(message = "Имя категории не может быть пустым")
    private String name;
}
