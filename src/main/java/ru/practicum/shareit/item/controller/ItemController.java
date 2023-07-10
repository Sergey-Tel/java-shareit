package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemHeader;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader(ItemHeader.X_HEADER_NAME) Long userId) {
        return itemService.getAllItems(userId);
    }

    @GetMapping(value = "/{itemId}")
    public ItemDto getItemById(@RequestHeader(ItemHeader.X_HEADER_NAME) Long userId,
                               @PathVariable Long itemId) {
        return itemService.getItemById(userId, itemId);
    }

    @PostMapping
    public ItemDto addNewItem(@RequestHeader(ItemHeader.X_HEADER_NAME) Long userId,
                              @RequestBody @Validated ItemDto itemDto) {
        return itemService.addNewItem(userId, itemDto);
    }

    @PatchMapping(value = "/{itemId}")
    public ItemDto updateItem(@RequestHeader(ItemHeader.X_HEADER_NAME) Long userId,
                              @PathVariable Long itemId,
                              @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader(ItemHeader.X_HEADER_NAME) Long userId,
                           @PathVariable Long itemId) {
        itemService.deleteItem(userId, itemId);
    }

    @GetMapping("search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        return itemService.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComment(@RequestHeader(ItemHeader.X_HEADER_NAME) Long userId,
                                         @PathVariable Long itemId,
                                         @RequestBody @Validated CommentRequestDto text) {
        return itemService.addComment(userId, itemId, text);
    }
}

