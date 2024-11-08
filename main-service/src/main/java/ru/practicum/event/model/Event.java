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

@Getter
@Setter
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "annotation", nullable = false, length = Integer.MAX_VALUE)
    private String annotation;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "confirmed_requests")
    private Long confirmedRequests;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @NotNull
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @NotNull
    @Column(name = "paid", nullable = false)
    private Boolean paid = false;

    @Column(name = "participant_limit")
    private Long participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Column(name = "state")
    @Enumerated(value = EnumType.STRING)
    private State state;

    @NotNull
    @Column(name = "title", nullable = false, length = Integer.MAX_VALUE)
    private String title;

    @Column(name = "views")
    private Long views;

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

    @Override
    public int hashCode() {
        return Objects.hash(id, annotation, category, confirmedRequests, createdOn, description, eventDate, initiator,
                location, paid, participantLimit, publishedOn, requestModeration, state, title, views);
    }

    @Override
    public String toString() {
        return "Event{" +
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
                "\nstate=" + state +
                "\ntitle='" + title + '\'' +
                "\nviews=" + views + "'\n}";
    }
}