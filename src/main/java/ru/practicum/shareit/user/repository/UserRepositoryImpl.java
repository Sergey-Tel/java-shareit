package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
@Slf4j
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private long generatorId = 0;

    @Override
    public List<User> getAllUsers() {
        log.info("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long id) {
        if (users.get(id) == null)
            throw new NoSuchElementException("Пользователь с таким id не найден");
        return users.get(id);
    }

    @Override
    public User addUser(User user) {
        user.setId(++generatorId);
        users.put(user.getId(), user);
        log.info("Добавлен пользователь: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            log.info("Пользователь с таким id не найден: {}", user.getId());
            throw new NoSuchElementException("Пользователь с таким id не найден");
        }
        User existingUser = users.get(user.getId());
        User updatedUser = UserMapper.updateExistingUser(user, existingUser);
        users.replace(user.getId(), updatedUser);
        log.info("Обновлен пользователь: {}", updatedUser);
        return updatedUser;
    }

    @Override
    public void removeUser(Long id) {
        if (!users.containsKey(id)) {
            log.info("Невозможно удалить пользователя! Пользователь с таким id не найден: {}", id);
            throw new NoSuchElementException("Невозможно удалить пользователя! Пользователь с таким id не найден");
        }
        users.remove(id);
    }

    public boolean isValidUser(User user) {
        return users.values()
                .stream()
                .filter(u -> u.getEmail().equals(user.getEmail()) && !u.getId().equals(user.getId()))
                .findFirst().isEmpty();
    }


}
