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

/**
 * DTO для представления подборки.
 * Содержит информацию о подборке, её событиях, закреплении и названии.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompilationDto {

    /**
     * ID подборки.
     */
    private Long id;

    /**
     * Список событий, входящих в подборку.
     */
    @UniqueElements
    private List<EventShortDto> events;

    /**
     * Флаг, показывающий, является ли подборка закрепленной.
     */
    @NotNull
    private Boolean pinned;

    /**
     * Название подборки. Должно быть не пустым и длина не менее 1 и не более 50 символов.
     */
    @NotNull
    @Length(min = 1, max = 50)
    private String title;
}
