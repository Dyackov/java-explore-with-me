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

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotNull
    @NotBlank
    @Length(min = 20, max = 2000)
    String annotation;

    @NotNull
    Long category;

    @NotNull
    @NotBlank
    @Length(min = 20, max = 7000)
    String description;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    @NotNull
    LocationDto location;

    Boolean paid = Boolean.FALSE;

    @PositiveOrZero
    Long participantLimit = 0L;

    Boolean requestModeration = Boolean.TRUE;

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