package ru.practicum.user.service;

import ru.practicum.user.model.User;
import ru.practicum.user.model.dto.NewUserRequest;
import ru.practicum.user.model.dto.UserDto;

import java.util.List;

/**
 * Интерфейс сервиса для управления пользователями.
 */
public interface UserService {

    /**
     * Создаёт нового пользователя.
     *
     * @param newUserRequest данные для создания нового пользователя
     * @return DTO созданного пользователя
     */
    UserDto createUserAdmin(NewUserRequest newUserRequest);

    /**
     * Получает список пользователей по их идентификаторам с поддержкой пагинации.
     *
     * @param ids список идентификаторов пользователей
     * @param from смещение для пагинации
     * @param size количество записей на страницу
     * @return список DTO пользователей
     */
    List<UserDto> getUsersAdmin(List<Integer> ids, int from, int size);

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя, которого нужно удалить
     */
    void deleteUserByIdAdmin(long userId);

    /**
     * Получает пользователя по его идентификатору или выбрасывает исключение, если пользователь не найден.
     *
     * @param userId идентификатор пользователя
     * @return найденный пользователь
     * @throws ru.practicum.error.exception.NotFoundException если пользователь не найден
     */
    User getUserByIdOrThrow(long userId);
}
