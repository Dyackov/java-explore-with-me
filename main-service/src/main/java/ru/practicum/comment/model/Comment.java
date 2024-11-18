package ru.practicum.comment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.comment.model.enums.StatusComment;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

/**
 * Сущность комментария, связанная с событием и пользователем.
 * Комментарии используются для добавления отзывов или информации о событии.
 */
@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {

    /**
     * Уникальный идентификатор комментария.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Текст комментария, ограниченный длиной от 5 до 1000 символов.
     */
    @Size(min = 5, max = 1000)
    @NotNull
    @Column(name = "text", nullable = false, length = 1000)
    private String text;

    /**
     * Пользователь, который написал комментарий.
     * Связь с сущностью User.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "commentator_id", nullable = false)
    private User commentator;

    /**
     * Событие, к которому относится комментарий.
     * Связь с сущностью Event.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    /**
     * Дата и время создания комментария.
     */
    @Column(name = "created")
    private LocalDateTime created;

    /**
     * Статус комментария, который может быть одним из значений:
     * PENDING, PUBLISHED, CANCELED.
     */
    @NotNull
    @Column(name = "status", nullable = false, length = Integer.MAX_VALUE)
    @Enumerated(value = EnumType.STRING)
    private StatusComment status;

    /**
     * Переопределение метода toString для удобного вывода комментария в лог.
     *
     * @return Строковое представление комментария.
     */
    @Override
    public String toString() {
        return "Comment{" +
                "\ncommentator=" + commentator +
                "\nid=" + id +
                "\ntext='" + text + '\'' +
                "\nevent=" + event +
                "\ncreated=" + created +
                "\nstatus=" + status +
                '}';
    }
}