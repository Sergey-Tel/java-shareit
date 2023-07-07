package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Long, Item> itemStorageMap = new HashMap<>();
    private long itemId = 1;

    private long setId() {
        return itemId++;
    }

    @Override
    public List<Item> getAllItemsByUserId(long userId) {
        return itemStorageMap.values().stream()
                .filter(item -> item.getOwnerId() == userId).collect(Collectors.toList());
    }

    @Override
    public Optional<Item> findItemById(long id) {
        return Optional.ofNullable(itemStorageMap.get(id));
    }

    @Override
    public Item saveItem(Item item) {
        if (itemStorageMap.containsKey(item.getId())) {
            throw new ConflictException(String.format("Item with id %s already create", item.getId()));
        }
        if (item.getId() == 0) {
            item.setId(setId());
        }
        itemStorageMap.put(item.getId(), item);
        return item;
    }

    @Override
    public List<Item> searchItems(String word) {
        return itemStorageMap.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(word.toLowerCase())
                        || item.getDescription().toLowerCase().contains(word.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Item deleteItem(long id) {
        return itemStorageMap.remove(id);
    }
}
