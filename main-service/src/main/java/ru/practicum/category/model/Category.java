package ru.practicum.category.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * Сущность для категории.
 * Используется для хранения информации о категориях в базе данных.
 */
@Getter
@Setter
@Entity
@Table(name = "categories")
@ToString
public class Category {

    /**
     * Уникальный идентификатор категории.
     * Генерируется автоматически при добавлении новой категории.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Название категории.
     * Должно быть обязательным для заполнения и уникальным.
     */
    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * Сравнивает текущую категорию с другой для определения их равенства.
     * Два объекта категории считаются равными, если их ID и имя совпадают.
     *
     * @param o объект, с которым сравнивается текущая категория.
     * @return {@code true}, если объекты равны, иначе {@code false}.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id) && Objects.equals(name, category.name);
    }

    /**
     * Вычисляет хеш-код для категории.
     * Хеш-код основан на идентификаторе и имени категории.
     *
     * @return хеш-код категории.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}