package ru.practicum.user.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * DTO для запроса на создание нового пользователя.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {

    /**
     * Имя пользователя. Не может быть пустым, длина от 2 до 250 символов.
     */
    @NotBlank
    @Length(min = 2, max = 250)
    private String name;

    /**
     * Электронная почта пользователя. Не может быть пустой, должна быть валидной.
     */
    @Email
    @NotBlank
    @Length(min = 6, max = 254)
    private String email;
}