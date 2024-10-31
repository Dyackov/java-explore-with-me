package ru.practicum.user.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDto {
    private String email;
    private Long id;
    private String name;
}
