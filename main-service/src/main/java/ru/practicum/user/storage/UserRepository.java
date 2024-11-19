package ru.practicum.user.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.user.model.User;

import java.util.List;

/**
 * Репозиторий для работы с сущностью пользователя.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Находит пользователей по их идентификаторам с поддержкой пагинации.
     *
     * @param ids список идентификаторов пользователей
     * @param pageable объект пагинации
     * @return список найденных пользователей
     */
    List<User> findByIdIn(List<Integer> ids, Pageable pageable);
}
