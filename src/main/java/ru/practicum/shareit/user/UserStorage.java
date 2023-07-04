package ru.practicum.shareit.user;

import ru.practicum.shareit.exceptions.UserNotFoundException;

import java.util.List;

public interface UserStorage {
    User getById(int userId) throws UserNotFoundException;

    List<User> getAll();

    User create(User user);

    User update(User user) throws UserNotFoundException;

    void delete(int userId);
}
