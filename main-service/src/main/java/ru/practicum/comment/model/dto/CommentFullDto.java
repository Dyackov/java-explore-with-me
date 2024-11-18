package ru.practicum.comment.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.comment.model.enums.StatusComment;
import ru.practicum.event.model.dto.EventShortDto;
import ru.practicum.user.model.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentFullDto {
    private Long id;
    private String text;
    private UserShortDto commentator;
    private EventShortDto event;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    private StatusComment status;
}