package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemService {
    private final ItemStorage itemStorage;

    public Item getById(int itemId) {
        return itemStorage.getById(itemId);
    }

    public List<Item> getOwnedItemsList(int ownerId) {
        return itemStorage.getOwnedItemsList(ownerId);
    }

    public Item create(Item item, int ownerId) {
        return itemStorage.create(item, ownerId);
    }

    public Item update(Item item, int ownerId) {
        return itemStorage.update(item, ownerId);
    }

    void delete(int itemId, int ownerId) {
        itemStorage.delete(itemId, ownerId);
    }

    public List<Item> search(String text) {
        return itemStorage.search(text);
    }
}
