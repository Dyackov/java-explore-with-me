package ru.practicum.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.user.model.dto.UserShortDto;

import java.time.LocalDateTime;

/**
 * DTO класс для передачи полной информации о событии.
 * Используется для отображения детализированных данных события в ответах API.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {

    /**
     * Уникальный идентификатор события.
     */
    private Long id;

    /**
     * Аннотация события.
     * Не может быть пустым.
     */
    @NotNull
    private String annotation;

    /**
     * Категория события.
     */
    @NotNull
    private CategoryDto category;

    /**
     * Количество подтвержденных запросов на участие.
     */
    private Long confirmedRequests;

    /**
     * Дата и время создания события.
     * Формат: yyyy-MM-dd HH:mm:ss.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    /**
     * Описание события.
     */
    private String description;

    /**
     * Дата и время проведения события.
     * Не может быть пустым.
     * Формат: yyyy-MM-dd HH:mm:ss.
     */
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    /**
     * Инициатор события.
     * Не может быть пустым.
     */
    @NotNull
    private UserShortDto initiator;

    /**
     * Локация события.
     * Не может быть пустой.
     */
    @NotNull
    private LocationDto location;

    /**
     * Флаг, указывающий, является ли событие платным.
     * Не может быть пустым.
     */
    @NotNull
    private Boolean paid;

    /**
     * Лимит участников события.
     */
    private Long participantLimit;

    /**
     * Дата и время публикации события.
     * Формат: yyyy-MM-dd HH:mm:ss.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    /**
     * Флаг, указывающий, требуется ли модерация запросов на участие.
     */
    private Boolean requestModeration;

    /**
     * Статус события.
     */
    private String state;

    /**
     * Заголовок события.
     * Не может быть пустым.
     */
    @NotNull
    private String title;

    /**
     * Количество просмотров события.
     */
    private Long views;

    /**
     * Переопределение метода toString для удобного отображения информации о событии.
     *
     * @return Строковое представление объекта EventFullDto.
     */
    @Override
    public String toString() {
        return "EventFullDto{" +
                "\nannotation='" + annotation + '\'' +
                "\nid=" + id +
                "\ncategory=" + category +
                "\nconfirmedRequests=" + confirmedRequests +
                "\ncreatedOn=" + createdOn +
                "\ndescription='" + description + '\'' +
                "\neventDate=" + eventDate +
                "\ninitiator=" + initiator +
                "\nlocation=" + location +
                "\npaid=" + paid +
                "\nparticipantLimit=" + participantLimit +
                "\npublishedOn=" + publishedOn +
                "\nrequestModeration=" + requestModeration +
                "\nstate='" + state + '\'' +
                "\ntitle='" + title + '\'' +
                "\nviews=" + views + "\n'}'";
    }
}