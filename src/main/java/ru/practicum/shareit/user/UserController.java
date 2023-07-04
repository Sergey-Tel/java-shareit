package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.UserEmailNotUniqueException;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody User user) {
        log.info("Create user: " + user.toString());

        UserValidator.validate(user, userService);

        return UserDtoMapper.toUserDto(userService.create(user));
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto update(@PathVariable int userId, @Valid @RequestBody UserDto userDto) {
        userDto.setId(userId);
        log.info("Update user {}: " + userDto, userId);

        User user = UserDtoMapper.toUser(userDto);

        UserValidator.validate(user, userService);

        return UserDtoMapper.toUserDto(userService.update(user));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAll() {
        log.info("Get all users");
        return UserDtoMapper.toUserDtoList(userService.getAll());
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto get(@PathVariable int userId) {
        log.info("Get userId {}", userId);
        return UserDtoMapper.toUserDto(userService.getById(userId));
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable int userId) {
        log.info("Delete userId {}", userId);
        userService.delete(userId);
    }

    private static class UserValidator {
        public static void validate(User user, UserService userService) throws UserEmailNotUniqueException {
            boolean isNewUser = user.getId() == 0;
            String userEmail = user.getEmail();

            if (isNewUser || userEmail != null && !userEmail.isBlank()) {
                List<User> allUsers = userService.getAll();
                for (User userEntry : allUsers) {
                    if (userEntry.getEmail().equals(userEmail)) {
                        if (isNewUser || userEntry.getId() != user.getId()) {
                            throw new UserEmailNotUniqueException("E-mail не уникален");
                        }
                    }
                }
            }
        }
    }

    private static class UserDtoMapper {

        public static User toUser(UserDto userDto) {
            if (userDto != null) return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
            else return null;
        }

        public static UserDto toUserDto(User user) {
            if (user != null) return new UserDto(user.getId(), user.getName(), user.getEmail());
            else return null;
        }

        public static List<UserDto> toUserDtoList(List<User> userList) {
            List<UserDto> userDtoList = new ArrayList<>();

            if (userList != null) {
                for (User user : userList) {
                    userDtoList.add(toUserDto(user));
                }
            }

            return userDtoList;
        }
    }
}
