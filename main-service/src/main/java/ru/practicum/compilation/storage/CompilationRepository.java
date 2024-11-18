package ru.practicum.compilation.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.compilation.model.Compilation;

import java.util.List;

/**
 * Репозиторий для работы с сущностью подборки.
 * Предоставляет методы для поиска и сохранения подборок в базе данных.
 */
@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    /**
     * Находит все подборки, которые имеют заданный флаг закрепленности (pinned).
     * Пагинация результатов производится с использованием переданных параметров.
     *
     * @param pinned  флаг закрепленности подборки.
     * @param pageable параметры пагинации.
     * @return список подборок, удовлетворяющих условиям.
     */
    List<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);
}
