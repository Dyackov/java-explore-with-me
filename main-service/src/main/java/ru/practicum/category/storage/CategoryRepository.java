package ru.practicum.category.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.category.model.Category;

/**
 * Репозиторий для работы с сущностью категории.
 * Расширяет {@link JpaRepository} для выполнения стандартных операций с базой данных.
 *
 * <p>Репозиторий предоставляет методы для работы с таблицей категорий в базе данных,
 * такие как сохранение, удаление, поиск категорий и т.д.</p>
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}