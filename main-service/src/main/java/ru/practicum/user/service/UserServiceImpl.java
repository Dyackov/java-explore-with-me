package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.error.exception.NotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.model.dto.NewUserRequest;
import ru.practicum.user.model.dto.UserDto;
import ru.practicum.user.model.mapper.UserMapper;
import ru.practicum.user.storage.UserRepository;

import java.util.List;

/**
 * Реализация сервиса для управления пользователями.
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    /**
     * Создаёт нового пользователя.
     *
     * @param newUserRequest данные для создания нового пользователя
     * @return DTO созданного пользователя
     */
    @Override
    public UserDto createUserAdmin(NewUserRequest newUserRequest) {
        log.debug("Admin:Создание пользователя: {}", newUserRequest);
        User user = userMapper.toUser(newUserRequest);
        User resultUser = userRepository.save(user);
        log.info("Admin:Создан пользователь:\n{}", resultUser);
        return userMapper.toUserDto(resultUser);
    }

    /**
     * Получает список пользователей по их идентификаторам с поддержкой пагинации.
     *
     * @param ids список идентификаторов пользователей
     * @param from смещение для пагинации
     * @param size количество записей на страницу
     * @return список DTO пользователей
     */
    @Override
    public List<UserDto> getUsersAdmin(List<Integer> ids, int from, int size) {
        log.debug("Admin:Получение пользователей id: {}", ids);
        List<User> users;
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        if (ids == null || ids.isEmpty()) {
            users = userRepository.findAll(pageable).getContent();
        } else {
            users = userRepository.findByIdIn(ids, pageable);
        }
        List<UserDto> result = userMapper.toUsersDto(users);
        log.info("Admin:Получен список пользователей:\n{}", result);
        return result;
    }

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя, которого нужно удалить
     */
    @Override
    public void deleteUserByIdAdmin(long userId) {
        log.debug("Admin:Удаление пользователя id: {}", userId);
        getUserByIdOrThrow(userId);
        userRepository.deleteById(userId);
        log.info("Admin:Пользователь с id: {} , удалён", userId);
    }

    /**
     * Получает пользователя по его идентификатору или выбрасывает исключение, если пользователь не найден.
     *
     * @param userId идентификатор пользователя
     * @return найденный пользователь
     * @throws NotFoundException если пользователь не найден
     */
    @Override
    public User getUserByIdOrThrow(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException(
                "User with id = " + userId + " not found"));
    }
}