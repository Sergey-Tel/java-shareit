package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.InvalidItemOwnerException;
import ru.practicum.shareit.exceptions.ItemAccessDeniedException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InMemoryItemStorage implements ItemStorage {
    private final UserStorage userStorage;

    private int nextId = 0;

    private final Map<Integer, Item> storageMap = new HashMap<>();

    @Override
    public Item getById(int itemId) throws ItemNotFoundException {
        if (storageMap.containsKey(itemId)) {
            return storageMap.get(itemId);
        } else {
            throw new ItemNotFoundException("Item not found");
        }
    }

    @Override
    public List<Item> getOwnedItemsList(int ownerId) {
        return storageMap.values().stream()
                .filter(item -> item.getOwnerId() == ownerId)
                .collect(Collectors.toList());
    }

    @Override
    public Item create(Item item, int ownerId) throws InvalidItemOwnerException {
        if (userStorage.getById(ownerId) == null) {
            throw new InvalidItemOwnerException("The owner with the specified id does not exist");
        } else {
            if (item != null) {
                item.setId(++nextId);
                item.setOwnerId(ownerId);
                storageMap.put(item.getId(), item);
            }
        }

        return item;
    }

    @Override
    public Item update(Item item, int ownerId) throws ItemAccessDeniedException, ItemNotFoundException {
        if (item != null) {
            Item storageItem = storageMap.get(item.getId());
            if (storageItem != null) {
                if (storageItem.getOwnerId() == ownerId) {
                    if (item.getName() != null && !item.getName().isBlank()) storageItem.setName(item.getName());
                    if (item.getDescription() != null && !item.getDescription().isBlank())
                        storageItem.setDescription(item.getDescription());
                    if (item.getAvailable() != null) storageItem.setAvailable(item.getAvailable());
                } else {
                    throw new ItemAccessDeniedException("The item belongs to another user");
                }
            } else {
                throw new ItemNotFoundException("Item not found");
            }

            return storageItem;
        }

        return item;
    }

    @Override
    public void delete(int itemId, int ownerId) throws ItemAccessDeniedException, ItemNotFoundException {
        Item storageItem = storageMap.get(itemId);
        if (storageItem != null) {
            if (storageItem.getOwnerId() == ownerId) {
                storageMap.remove(itemId);
            } else {
                throw new ItemAccessDeniedException("The item belongs to another user");
            }
        } else {
            throw new ItemNotFoundException("Item not found");
        }
    }

    private static boolean searchFunction(Item item, String text) {
        return item.getName().concat(" ".concat(item.getDescription()))
                .toLowerCase()
                .contains(text.toLowerCase());
    }

    @Override
    public List<Item> search(String text) {
        return storageMap.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> searchFunction(item, text))
                .collect(Collectors.toList());
    }
}
