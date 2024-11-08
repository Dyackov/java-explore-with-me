package ru.practicum.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.event.model.enums.StateAction;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventUserRequest {

    @Length(min = 20, max = 2000)
    String annotation;

    Long category;

    @Length(min = 20, max = 7000)
    String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    LocationDto location;

    Boolean paid;

    Long participantLimit;

    Boolean requestModeration;

    StateAction stateAction;

    @Length(max = 120)
    String title;
}