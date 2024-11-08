package ru.practicum.event.model.dto;

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
public class EventShortDto {
    Long id;
    @NotNull
    String annotation;
    @NotNull
    CategoryDto category;
    Long confirmedRequests;
    @NotNull
    LocalDateTime eventDate;
    @NotNull
    UserShortDto initiator;
    @NotNull
    Boolean paid;
    @NotNull
    String title;
    Long views;
}