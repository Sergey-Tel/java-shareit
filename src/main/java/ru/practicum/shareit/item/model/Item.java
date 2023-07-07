package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class Item {
    private int id;
    private int ownerId;
    @NotBlank(message = "The name of the item cannot be empty")
    private String name;
    @NotBlank(message = "The description of a thing cannot be empty")
    private String description;
    @NotNull
    private Boolean available;
}
