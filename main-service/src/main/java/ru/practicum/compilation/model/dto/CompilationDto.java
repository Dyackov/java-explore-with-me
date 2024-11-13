package ru.practicum.compilation.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UniqueElements;
import ru.practicum.event.model.dto.EventShortDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompilationDto {

    private Long id;

    @UniqueElements
    private List<EventShortDto> events;

    @NotNull
    private Boolean pinned;

    @NotNull
    @Length(min = 1, max = 50)
    private String title;
}