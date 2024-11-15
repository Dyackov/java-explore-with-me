package ru.practicum.user.service;

import ru.practicum.user.model.User;
import ru.practicum.user.model.dto.NewUserRequest;
import ru.practicum.user.model.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUserAdmin(NewUserRequest newUserRequest);

    List<UserDto> getUsersAdmin(List<Integer> ids, int from, int size);

    void deleteUserByIdAdmin(long userId);

    User getUserByIdOrThrow(long userId);
}
