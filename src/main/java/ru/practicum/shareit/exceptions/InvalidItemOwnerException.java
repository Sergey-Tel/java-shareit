package ru.practicum.shareit.exceptions;

public class InvalidItemOwnerException extends RuntimeException {
    public InvalidItemOwnerException(String message) {
        super(message);
    }

}
