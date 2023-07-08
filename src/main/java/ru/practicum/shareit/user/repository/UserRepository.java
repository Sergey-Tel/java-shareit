package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> getAllUsers();

    User getUserById(Long id);

    User addUser(User user);

    User updateUser(User user);

    void removeUser(Long id);

    boolean isValidUser(User user);
}
