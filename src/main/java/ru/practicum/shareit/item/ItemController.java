package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader(ItemHeader.X_HEADER_NAME) @NotNull long userId,
                              @RequestBody @Validated({Create.class}) ItemDto itemDto) {
        return itemService.createItem(itemDto, userId);
    }

    @GetMapping("{id}")
    public ItemDto getItem(@PathVariable long id) {
        return itemService.getItemById(id);
    }

    @GetMapping
    public List<ItemDto> getAllUserItems(@RequestHeader(ItemHeader.X_HEADER_NAME) @NotNull long userId) {
        return itemService.getAllItemsByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam(name = "text", defaultValue = "") String word) {
        return itemService.searchItems(word);
    }

    @PatchMapping("{id}")
    public ItemDto updateItem(@RequestHeader(ItemHeader.X_HEADER_NAME) @NotNull long userId,
                              @RequestBody ItemDto itemDto, @PathVariable long id) {
        return itemService.updateItem(itemDto, id, userId);
    }

}
