package ru.practicum.user.model.mapper;

import org.mapstruct.Mapper;
import ru.practicum.user.model.User;
import ru.practicum.user.model.dto.NewUserRequest;
import ru.practicum.user.model.dto.UserDto;
import ru.practicum.user.model.dto.UserShortDto;

import java.util.List;

/**
 * Маппер для преобразования объектов типа User и DTO.
 */
@Mapper
public interface UserMapper {

    /**
     * Преобразует NewUserRequest в объект User.
     *
     * @param newUserRequest запрос на создание нового пользователя
     * @return объект User
     */
    User toUser(NewUserRequest newUserRequest);

    /**
     * Преобразует список объектов User в список DTO пользователей (UserDto).
     *
     * @param users список пользователей
     * @return список DTO пользователей
     */
    List<UserDto> toUsersDto(List<User> users);

    /**
     * Преобразует объект User в DTO пользователя (UserDto).
     *
     * @param user объект пользователя
     * @return DTO пользователя
     */
    UserDto toUserDto(User user);

    /**
     * Преобразует объект User в краткое DTO пользователя (UserShortDto).
     *
     * @param user объект пользователя
     * @return краткое DTO пользователя
     */
    UserShortDto toDto(User user);
}
