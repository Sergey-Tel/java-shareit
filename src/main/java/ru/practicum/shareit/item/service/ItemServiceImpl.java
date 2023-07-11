package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.mapper.ItemMapper.toItem;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItemDto;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public List<ItemDto> getAllItems(Long userId) {
        return itemRepository.getAllItems()
                .stream()
                .filter(u -> u.getOwner() != null && u.getOwner().getId().equals(userId))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public ItemDto getItemById(Long itemId) {
        return toItemDto(itemRepository.getItemById(itemId));
    }

    @Override
    public ItemDto addNewItem(Long userId, ItemDto itemDto) {
        User user = userRepository.getUserById(userId);
        Item item = toItem(itemDto);
        item.setOwner(user);
        return toItemDto(itemRepository.addItem(item));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        Item existItem = itemRepository.getItemById(itemId);
        if (existItem.getOwner() != null && !userId.equals(existItem.getOwner().getId()))
            throw new NoSuchElementException("Ошибка обновления информации о вещи с id = " + itemId);

        Item updatedItem = toItem(itemDto);
        ItemMapper.updateItemWithExistingValues(updatedItem, existItem);

        return toItemDto(itemRepository.updateItem(updatedItem));
    }

    @Override
    public void deleteItem(Long userId, Long itemId) {
        Item existItem = itemRepository.getItemById(itemId);
        if (existItem.getOwner() != null && !userId.equals(existItem.getOwner().getId()))
            throw new NoSuchElementException("Ошибка удаления вещи с id = " + itemId + " пользователем с id = " + userId);
        itemRepository.removeItem(itemId);
    }

    public List<ItemDto> searchItems(String text) {
        if (text.isEmpty())
            return Collections.emptyList();
        return itemRepository.searchItems((text.toLowerCase())).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }


}