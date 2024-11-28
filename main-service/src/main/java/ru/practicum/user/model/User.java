package ru.practicum.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * Сущность пользователя.
 */
@Getter
@Setter
@Entity
@Table(name = "users")
@ToString
public class User {

    /**
     * Идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Имя пользователя.
     */
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Электронная почта пользователя.
     */
    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * Переопределённый метод equals для сравнения объектов User по id, name и email.
     *
     * @param o объект для сравнения
     * @return true, если объекты равны, иначе false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(name, user.name) &&
                Objects.equals(email, user.email);
    }

    /**
     * Переопределённый метод hashCode для генерации хэш-кода на основе id, name и email.
     *
     * @return хэш-код объекта
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }
}
