package ru.practicum.request.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.event.model.Event;
import ru.practicum.request.model.enums.RequestState;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "requests")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "created")
    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private User requester;

    @Column(name = "status", length = Integer.MAX_VALUE)
    private RequestState status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return Objects.equals(id, request.id) && Objects.equals(created, request.created) && Objects.equals(event, request.event) && Objects.equals(requester, request.requester) && status == request.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, created, event, requester, status);
    }

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