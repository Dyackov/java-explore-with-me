package ru.practicum.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для представления информации о пользователе.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    /**
     * Электронная почта пользователя.
     */
    private String email;

    /**
     * Идентификатор пользователя.
     */
    private Long id;

    /**
     * Имя пользователя.
     */
    private String name;
}
