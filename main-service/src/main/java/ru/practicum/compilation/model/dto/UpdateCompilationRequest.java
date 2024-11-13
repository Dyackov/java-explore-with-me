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
public class UpdateCompilationRequest {

    List<Long> eventIds;

    @NotNull
    Boolean pinned;

    @NotNull
    @Length(min = 1)
    String title;

}