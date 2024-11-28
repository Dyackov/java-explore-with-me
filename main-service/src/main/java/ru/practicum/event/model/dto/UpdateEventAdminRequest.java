package ru.practicum.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.event.model.enums.StateActionAdmin;

import java.time.LocalDateTime;

/**
 * DTO класс для обновления события администраторами.
 * Содержит поля для обновления существующего события, включая аннотацию, описание, дату, локацию и другие параметры.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventAdminRequest {

    /**
     * Аннотация события, описывающая его суть.
     * Должна быть длиной от 20 до 2000 символов.
     */
    @Length(min = 20, max = 2000)
    private String annotation;

    /**
     * Идентификатор категории события.
     * Может быть пустым, если категория не изменяется.
     */
    private Long category;

    /**
     * Подробное описание события.
     * Должно быть длиной от 20 до 7000 символов.
     */
    @Length(min = 20, max = 7000)
    private String description;

    /**
     * Дата и время события.
     * Формат: "yyyy-MM-dd HH:mm:ss".
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    /**
     * Местоположение события.
     * Может быть пустым, если локация не изменяется.
     */
    private LocationDto location;

    /**
     * Указывает, является ли событие платным.
     * Может быть пустым, если платность не изменяется.
     */
    private Boolean paid;

    /**
     * Лимит участников события.
     * Может быть пустым, если лимит не изменяется.
     */
    private Long participantLimit;

    /**
     * Указывает, требуется ли модерация запросов на участие.
     * Может быть пустым, если настройка не изменяется.
     */
    private Boolean requestModeration;

    /**
     * Действие для изменения состояния события (например, "Публикация", "Отмена").
     * Может быть пустым, если состояние не изменяется.
     */
    private StateActionAdmin stateAction;

    /**
     * Название события.
     * Должно быть длиной от 3 до 120 символов.
     */
    @Length(min = 3, max = 120)
    private String title;

}
