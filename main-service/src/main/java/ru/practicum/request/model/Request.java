package ru.practicum.request.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.event.model.Event;
import ru.practicum.request.model.enums.RequestState;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Сущность заявки на участие в событии.
 */
@Getter
@Setter
@Entity
@Table(name = "requests")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    /**
     * Идентификатор заявки.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Дата и время создания заявки.
     */
    @Column(name = "created")
    private LocalDateTime created;

    /**
     * Событие, к которому относится заявка.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    /**
     * Пользователь, который подал заявку.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private User requester;

    /**
     * Статус заявки.
     */
    @Column(name = "status", length = Integer.MAX_VALUE)
    private RequestState status;

    /**
     * Проверяет равенство текущей заявки и другой заявки.
     *
     * @param o объект для сравнения.
     * @return true, если объекты равны, иначе false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return Objects.equals(id, request.id) &&
                Objects.equals(created, request.created) &&
                Objects.equals(event, request.event) &&
                Objects.equals(requester, request.requester) &&
                status == request.status;
    }

    /**
     * Генерирует хэш-код для заявки.
     *
     * @return хэш-код заявки.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, created, event, requester, status);
    }

    /**
     * Возвращает строковое представление заявки.
     *
     * @return строка, представляющая заявку.
     */
    @Override
    public String toString() {
        return "Request{" +
                "\ncreated=" + created +
                "\nid=" + id +
                "\nevent=" + event +
                "\nrequester=" + requester +
                "\nstatus=" + status +
                '}';
    }
}
