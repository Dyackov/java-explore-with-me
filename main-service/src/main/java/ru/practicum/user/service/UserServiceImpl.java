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

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserDto createUserAdmin(NewUserRequest newUserRequest) {
        log.debug("Admin:Создание пользователя: {}", newUserRequest);
        User user = userMapper.toUser(newUserRequest);
        User resultUser = userRepository.save(user);
        log.info("Admin:Создан пользователь:\n{}", resultUser);
        return userMapper.toUserDto(resultUser);
    }

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

    @Override
    public void deleteUserByIdAdmin(long userId) {
        log.debug("Admin:Удаление пользователя id: {}", userId);
        getUserByIdOrThrow(userId);
        userRepository.deleteById(userId);
        log.info("Admin:Пользователь с id: {} , удалён", userId);
    }

    @Override
    public User getUserByIdOrThrow(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException(
                "User with id = " + userId + " not found"));
    }
}