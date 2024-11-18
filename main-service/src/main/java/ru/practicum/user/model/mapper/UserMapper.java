package ru.practicum.user.model.mapper;

import org.mapstruct.Mapper;
import ru.practicum.user.model.User;
import ru.practicum.user.model.dto.NewUserRequest;
import ru.practicum.user.model.dto.UserDto;
import ru.practicum.user.model.dto.UserShortDto;

import java.util.List;

@Mapper
public interface UserMapper {
    User toUser(NewUserRequest newUserRequest);

    List<UserDto> toUsersDto(List<User> users);

    UserDto toUserDto(User user);

    User toEntity(UserShortDto userShortDto);

    UserShortDto toDto(User user);
}