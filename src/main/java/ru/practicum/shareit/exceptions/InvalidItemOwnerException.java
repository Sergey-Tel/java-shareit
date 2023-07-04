package ru.practicum.shareit.exceptions;

public class InvalidItemOwnerException extends RuntimeException {
    public InvalidItemOwnerException() {
        super();
    }

    public InvalidItemOwnerException(String message) {
        super(message);
    }

    public InvalidItemOwnerException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidItemOwnerException(Throwable cause) {
        super(cause);
    }
}
