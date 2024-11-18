package ru.practicum.comment.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.comment.model.enums.StatusComment;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStatusCommentAdmin {
    @NotNull
    private StatusComment status;
}