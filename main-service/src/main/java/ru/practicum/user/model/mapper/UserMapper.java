package ru.practicum.user.model.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
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

    User toEntity(UserDto userDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserDto userDto, @MappingTarget User user);

    User toEntity(UserShortDto userShortDto);

    UserShortDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserShortDto userShortDto, @MappingTarget User user);
}