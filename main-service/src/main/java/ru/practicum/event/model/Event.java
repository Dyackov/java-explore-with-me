package ru.practicum.event.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.category.model.Category;
import ru.practicum.event.model.enums.State;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Сущность для представления события в системе.
 * Хранит информацию о событии, включая его аннотацию, описание, дату проведения, инициатора, местоположение, статус и другие параметры.
 */
@Getter
@Setter
@Entity
@Table(name = "events")
public class Event {

    /**
     * Уникальный идентификатор события.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Аннотация события (краткое описание).
     */
    @NotNull
    @Column(name = "annotation", nullable = false, length = Integer.MAX_VALUE)
    private String annotation;

    /**
     * Категория, к которой относится событие.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /**
     * Количество подтвержденных заявок на участие.
     */
    @Column(name = "confirmed_requests")
    private Long confirmedRequests;

    /**
     * Дата и время создания события.
     */
    @Column(name = "created_on")
    private LocalDateTime createdOn;

    /**
     * Описание события (подробное описание).
     */
    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    /**
     * Дата и время начала события.
     */
    @NotNull
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    /**
     * Инициатор события (пользователь, который создал событие).
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    /**
     * Местоположение события.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    /**
     * Указывает, является ли событие платным.
     */
    @NotNull
    @Column(name = "paid", nullable = false)
    private Boolean paid = false;

    /**
     * Ограничение на количество участников события.
     */
    @Column(name = "participant_limit")
    private Long participantLimit;

    /**
     * Дата и время публикации события.
     */
    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    /**
     * Указывает, требуется ли модерация заявок на участие.
     */
    @Column(name = "request_moderation")
    private Boolean requestModeration;

    /**
     * Статус события (например, ожидает, опубликовано, отменено).
     */
    @Column(name = "state")
    @Enumerated(value = EnumType.STRING)
    private State state;

    /**
     * Название события.
     */
    @NotNull
    @Column(name = "title", nullable = false, length = Integer.MAX_VALUE)
    private String title;

    /**
     * Количество просмотров события.
     */
    @Column(name = "views")
    private Long views;

    /**
     * Метод для проверки равенства двух объектов событий.
     * Сравнивает все поля сущности.
     *
     * @param o Объект для сравнения.
     * @return true, если объекты равны, иначе false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) &&
                Objects.equals(annotation, event.annotation) &&
                Objects.equals(category, event.category) &&
                Objects.equals(confirmedRequests, event.confirmedRequests) &&
                Objects.equals(createdOn, event.createdOn) &&
                Objects.equals(description, event.description) &&
                Objects.equals(eventDate, event.eventDate) &&
                Objects.equals(initiator, event.initiator) &&
                Objects.equals(location, event.location) &&
                Objects.equals(paid, event.paid) &&
                Objects.equals(participantLimit, event.participantLimit) &&
                Objects.equals(publishedOn, event.publishedOn) &&
                Objects.equals(requestModeration, event.requestModeration) &&
                state == event.state && Objects.equals(title, event.title) &&
                Objects.equals(views, event.views);
    }

    /**
     * Метод для вычисления хэш-кода для объекта события.
     * Используется для корректной работы с хэш-таблицами.
     *
     * @return Хэш-код объекта события.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, annotation, category, confirmedRequests, createdOn, description, eventDate, initiator,
                location, paid, participantLimit, publishedOn, requestModeration, state, title, views);
    }

    /**
     * Метод для представления события в строковом формате.
     * Используется для вывода информации о событии.
     *
     * @return Строковое представление события.
     */
    @Override
    public String toString() {
        return "Event{" +
                "\nid=" + id +
                "\ncategory=" + category +
                "\nconfirmedRequests=" + confirmedRequests +
                "\ncreatedOn=" + createdOn +
                "\neventDate=" + eventDate +
                "\ninitiator=" + initiator +
                "\nlocation=" + location +
                "\npaid=" + paid +
                "\nparticipantLimit=" + participantLimit +
                "\npublishedOn=" + publishedOn +
                "\nrequestModeration=" + requestModeration +
                "\nstate=" + state +
                "\ntitle='" + title + '\'' +
                "\nviews=" + views + "'\n}";
    }
}
