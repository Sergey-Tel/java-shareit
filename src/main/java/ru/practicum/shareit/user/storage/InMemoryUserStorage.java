package ru.practicum.shareit.user.storage;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.User;

import java.util.*;

@Component
@Getter
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> userStorageMap = new HashMap<>();
    private final Set<String> emailSet = new HashSet<>();
    private long userId = 1;

    private long setId() {
        return userId++;
    }

    public User saveUser(User user) {
        if (user.getId() == 0) {
            checkEmail(user);
            user.setId(setId());
            emailSet.add(user.getEmail());
        } else {
            User existingUser = getUserById(user.getId())
                    .orElseThrow(() -> new NotFoundException(String.format("User %s not found", user.getId())));

            if (!existingUser.getEmail().equals(user.getEmail())) {
                checkEmail(user);
                emailSet.remove(existingUser.getEmail());
                emailSet.add(user.getEmail());
            }
        }

        userStorageMap.put(user.getId(), user);
        return user;
    }


    public List<User> getAllUsers() {
        return new ArrayList<>(userStorageMap.values());
    }

    public Optional<User> getUserById(long id) {
        return userStorageMap.containsKey(id) ? Optional.of(userStorageMap.get(id)) : Optional.empty();
    }

    public User deleteUser(long id) {
        User user = getUserById(id).orElseThrow(() -> new NotFoundException(String.format("User %s not found", id)));
        emailSet.remove(user.getEmail());
        return userStorageMap.remove(id);
    }

    private void checkEmail(User user) {
        if (emailSet.contains(user.getEmail())) {
            throw new ConflictException("Email already used");
        }
    }

}
