package ru.practicum.user.model.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDto {
    private String email;
    private Long id;
    private String name;
}
