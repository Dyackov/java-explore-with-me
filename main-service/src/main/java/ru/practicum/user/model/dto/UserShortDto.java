package ru.practicum.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для краткого представления информации о пользователе.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserShortDto {

    /**
     * Идентификатор пользователя.
     */
    private Long id;

    /**
     * Имя пользователя.
     */
    private String name;
}