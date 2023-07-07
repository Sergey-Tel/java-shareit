package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    List<Item> getAllItemsByUserId(long userId);
    Optional<Item> findItemById(long id);
    Item saveItem(Item item);
    List<Item> searchItems(String word);
    Item deleteItem(long id);
}