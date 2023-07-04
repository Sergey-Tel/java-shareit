package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
public class UserDto {
    private int id;

    private String name;

    @Email(message = "Электронная почта указана неверно")
    private String email;
}
