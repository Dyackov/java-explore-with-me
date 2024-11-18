package ru.practicum.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.user.model.dto.UserShortDto;

import java.time.LocalDateTime;

/**
 * DTO класс для краткой информации о событии.
 * Содержит основные данные о событии, такие как аннотация, категория, дата события и инициатор.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventShortDto {

    /**
     * Идентификатор события.
     */
    Long id;

    /**
     * Краткая аннотация события.
     */
    @NotNull
    String annotation;

    /**
     * Категория, к которой принадлежит событие.
     */
    @NotNull
    CategoryDto category;

    /**
     * Количество подтвержденных заявок на участие в событии.
     */
    Long confirmedRequests;

    /**
     * Дата и время события.
     */
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    /**
     * Информации о инициаторе события.
     */
    @NotNull
    UserShortDto initiator;

    /**
     * Платное ли событие.
     */
    @NotNull
    Boolean paid;

    /**
     * Название события.
     */
    @NotNull
    String title;

    /**
     * Количество просмотров события.
     */
    Long views;
}
