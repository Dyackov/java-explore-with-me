package ru.practicum.user.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record NewUserRequest(@NotNull String name, @NotNull @Email @NotEmpty @NotBlank String email) {
}