package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class User {
    private int id;
    @NotBlank(message = "The user name cannot be empty")
    private String name;
    @Email(message = "The email address is incorrect")
    @NotBlank(message = "Email cannot be empty")
    private String email;

}
