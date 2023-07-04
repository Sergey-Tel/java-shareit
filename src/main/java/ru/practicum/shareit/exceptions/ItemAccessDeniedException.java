package ru.practicum.shareit.exceptions;

public class ItemAccessDeniedException extends RuntimeException {
    public ItemAccessDeniedException() {
        super();
    }

    public ItemAccessDeniedException(String message) {
        super(message);
    }

    public ItemAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ItemAccessDeniedException(Throwable cause) {
        super(cause);
    }
}
