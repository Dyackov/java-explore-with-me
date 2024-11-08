package ru.practicum.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.user.model.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {
    private Long id;
    @NotNull
    private String annotation;
    @NotNull
    private CategoryDto category;
    private Long confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    private String description;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull
    private UserShortDto initiator;
    @NotNull
    private LocationDto location;
    @NotNull
    private Boolean paid;
    private Long participantLimit;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private String state;
    @NotNull
    private String title;
    private Long views;

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