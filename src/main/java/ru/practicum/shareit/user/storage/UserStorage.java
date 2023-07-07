package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User saveUser(User user);
    List<User> getAllUsers();
    Optional<User> getUserById(long id);
    User deleteUser(long id);


}
