package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.mapper.UserMapper.toUser;
import static ru.practicum.shareit.user.mapper.UserMapper.toUserDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public List<UserDto> getAllUsers() {
        return userRepository.getAllUsers()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        return toUserDto(userRepository.getUserById(id));
    }

    public UserDto addUser(UserDto userDto) {
        User user = toUser(userDto);
        if (!isValidUser(user))
            throw new ValidationException("Ошибка валидации пользователя");
        return toUserDto(userRepository.addUser(user));
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        User updatedUser = toUser(userDto);
        updatedUser.setId(id);
        if (!isValidUser(updatedUser))
            throw new ValidationException("Ошибка валидации пользователя");

        return toUserDto(userRepository.updateUser(updatedUser));
    }

    public void removeUser(Long id) {
        userRepository.removeUser(id);
    }

    private boolean isValidUser(User user) {
        return userRepository.isValidUser(user);
    }

}