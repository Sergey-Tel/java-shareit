package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;

public class UserMapper {

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toUser(UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public static User updateExistingUser(User user, User existingUser) {
        if (user.getName() == null)
            user.setName(existingUser.getName());
        if (user.getEmail() == null)
            user.setEmail(existingUser.getEmail());
        return user;
    }
}