package ru.practicum.user.service;

import ru.practicum.user.model.dto.NewUserRequest;
import ru.practicum.user.model.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(NewUserRequest newUserRequest);

    List<UserDto> getUsers(List<Integer> ids, int from, int size);

    void deleteUserById(long userId);
}
