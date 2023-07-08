package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;

import java.util.List;

public interface ItemService {
    ItemDto add(ItemDto itemDto, Long userId);

    ItemDto update(ItemDtoUpdate itemDto, Long itemId, Long userId);

    ItemDto findById(Long itemId, Long userId);

    List<ItemDto> findAllByUserId(Long userId);

    List<ItemDto> searchByNameOrDescription(String request);
}
