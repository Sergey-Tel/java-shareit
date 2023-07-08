package ru.practicum.shareit.common;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.time.OffsetDateTime;

@Value
@RequiredArgsConstructor
public class ErrorResponse {
    OffsetDateTime timestamp = OffsetDateTime.now();
    Integer code;
    String errorMessage;
}
