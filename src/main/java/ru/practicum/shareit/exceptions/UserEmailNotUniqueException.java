package ru.practicum.shareit.exceptions;

public class UserEmailNotUniqueException extends RuntimeException {
    public UserEmailNotUniqueException() {
        super();
    }

    public UserEmailNotUniqueException(String message) {
        super(message);
    }

    public UserEmailNotUniqueException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserEmailNotUniqueException(Throwable cause) {
        super(cause);
    }
}
