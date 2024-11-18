package ru.practicum.event.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * Сущность для представления местоположения события.
 * Хранит информацию о широте и долготе местоположения.
 */
@Getter
@Setter
@Entity
@Table(name = "location")
@ToString
public class Location {

    /**
     * Уникальный идентификатор местоположения.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Широта местоположения.
     */
    @Column(name = "lat")
    private Double lat;

    /**
     * Долгота местоположения.
     */
    @Column(name = "lon")
    private Double lon;

    /**
     * Метод для проверки равенства двух объектов местоположений.
     * Сравнивает все поля сущности.
     *
     * @param o Объект для сравнения.
     * @return true, если объекты равны, иначе false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(id, location.id) && Objects.equals(lat, location.lat) && Objects.equals(lon, location.lon);
    }

    /**
     * Метод для вычисления хэш-кода для объекта местоположения.
     * Используется для корректной работы с хэш-таблицами.
     *
     * @return Хэш-код объекта местоположения.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, lat, lon);
    }
}