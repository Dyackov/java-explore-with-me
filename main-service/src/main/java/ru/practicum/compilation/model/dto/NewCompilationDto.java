package ru.practicum.compilation.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {

    @NotNull
    private List<Long> events;

    @NotNull
    Boolean pinned;

    @NotNull
    @Length(min = 1, max = 50)
    String title;
}