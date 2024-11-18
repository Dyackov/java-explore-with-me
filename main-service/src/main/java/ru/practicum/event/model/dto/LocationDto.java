package ru.practicum.event.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO класс для представления местоположения события.
 * Содержит данные о широте и долготе.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {

    /**
     * Широта местоположения.
     */
    @NotNull
    private Double lat;

    /**
     * Долгота местоположения.
     */
    @NotNull
    private Double lon;
}
