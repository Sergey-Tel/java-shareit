package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item getById(int itemId);

    List<Item> getOwnedItemsList(int ownerId);

    Item create(Item item, int ownerId);

    Item update(Item item, int ownerId);

    void delete(int itemId, int ownerId);

    List<Item> search(String text);
}