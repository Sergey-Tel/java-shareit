package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.UserNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    int nextId = 0;

    private final Map<Integer, User> storageMap = new HashMap<>();

    @Override
    public User getById(int userId) throws UserNotFoundException {
        if (storageMap.containsKey(userId)) {
            return storageMap.get(userId);
        } else {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(storageMap.values());
    }

    @Override
    public User create(User user) {
        if (user != null) {
            user.setId(++nextId);
            storageMap.put(user.getId(), user);
        }

        return user;
    }

    @Override
    public User update(User user) throws UserNotFoundException {
        if (user != null) {
            User storageUser = storageMap.get(user.getId());
            if (storageUser != null) {
                if (user.getEmail() != null && !user.getEmail().isBlank()) storageUser.setEmail(user.getEmail());
                if (user.getName() != null && !user.getName().isBlank()) storageUser.setName(user.getName());
            } else {
                throw new UserNotFoundException("Пользователь не найден");
            }

            return storageUser;
        }

        return user;
    }

    @Override
    public void delete(int userId) {
        storageMap.remove(userId);
    }
}
