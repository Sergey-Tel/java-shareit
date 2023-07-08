package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    long id;
    String name;
    String description;
    Boolean available;
    long owner;
}
