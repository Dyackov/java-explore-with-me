package ru.practicum.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

/**
 * DTO класс для создания нового события.
 * Содержит необходимые поля для создания события, включая аннотацию, описание, дату, локацию и другие параметры.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {

    /**
     * Аннотация события, описывающая его суть.
     * Должна быть длиной от 20 до 2000 символов.
     */
    @NotNull
    @NotBlank
    @Length(min = 20, max = 2000)
    String annotation;

    /**
     * Идентификатор категории события.
     * Должен быть ненулевым.
     */
    @NotNull
    Long category;

    /**
     * Подробное описание события.
     * Должно быть длиной от 20 до 7000 символов.
     */
    @NotNull
    @NotBlank
    @Length(min = 20, max = 7000)
    String description;

    /**
     * Дата и время события.
     * Формат: "yyyy-MM-dd HH:mm:ss".
     */
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    /**
     * Местоположение события.
     * Должно быть ненулевым.
     */
    @NotNull
    LocationDto location;

    /**
     * Указывает, является ли событие платным.
     * По умолчанию: false.
     */
    Boolean paid = Boolean.FALSE;

    /**
     * Лимит участников события.
     * Должен быть положительным числом или 0 (без ограничения).
     * По умолчанию: 0.
     */
    @PositiveOrZero
    Long participantLimit = 0L;

    /**
     * Указывает, требуется ли модерация запросов на участие.
     * По умолчанию: true.
     */
    Boolean requestModeration = Boolean.TRUE;

    /**
     * Название события.
     * Должно быть длиной от 3 до 120 символов.
     */
    @NotNull
    @Length(min = 3, max = 120)
    String title;

    @Override
    public String toString() {
        return "NewEventDto{" +
                "\nannotation='" + annotation + '\'' +
                "\ncategory=" + category +
                "\ndescription='" + description + '\'' +
                "\neventDate=" + eventDate +
                "\nlocation=" + location +
                "\npaid=" + paid +
                "\nparticipantLimit=" + participantLimit +
                "\nrequestModeration=" + requestModeration +
                "\ntitle='" + title + '\'' +
                '}';
    }
}
