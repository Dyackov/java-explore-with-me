package ru.practicum.compilation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.event.model.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Сущность, представляющая подборку (compilation), которая может содержать несколько событий.
 * Включает поля для идентификатора, флага закрепления, названия и списка событий.
 */
@Getter
@Setter
@Entity
@Table(name = "compilations")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Compilation {

    /**
     * Уникальный идентификатор подборки.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Флаг, указывающий, является ли подборка закрепленной.
     */
    @NotNull
    @Column(name = "pinned", nullable = false)
    private Boolean pinned = false;

    /**
     * Название подборки.
     */
    @NotNull
    @Column(name = "title", nullable = false, length = Integer.MAX_VALUE)
    private String title;

    /**
     * Список событий, входящих в подборку.
     * Используется связь многие ко многим (ManyToMany).
     */
    @ManyToMany
    @JoinTable(
            name = "COMPILATIONS_EVENTS",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> events = new ArrayList<>();


    /**
     * Переопределенный метод equals для проверки равенства объектов Compilation.
     * Сравнивает id, pinned и title.
     *
     * @param o объект для сравнения.
     * @return true, если объекты равны, иначе false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Compilation that = (Compilation) o;
        return Objects.equals(id, that.id) && Objects.equals(pinned, that.pinned) && Objects.equals(title, that.title);
    }

    /**
     * Переопределенный метод hashCode для генерации хеш-кода объекта Compilation.
     *
     * @return хеш-код объекта.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, pinned, title);
    }

    /**
     * Переопределенный метод toString для вывода информации о подборке в строковом виде.
     *
     * @return строковое представление объекта Compilation.
     */
    @Override
    public String toString() {
        return "Compilation{" +
                "\nid=" + id +
                "\npinned=" + pinned +
                "\ntitle='" + title +
                "\nevents=" + events + '}';
    }
}