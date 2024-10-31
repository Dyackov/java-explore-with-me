package ru.practicum.user.model.mapper;

import org.mapstruct.Mapper;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

import java.util.List;

@Mapper
public interface UserMapper {
    User toUser(NewUserRequest newUserRequest);

    List<UserDto> toUsersDto(List<User> users);

    UserDto toUserDto(User user);

}