package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(@Valid @RequestBody Item item, @RequestHeader(value = ItemHeader.X_HEADER_NAME, defaultValue = "0") int ownerId)
            throws ValidationException {
        log.info("Create item, owner {}: " + item.toString(), ownerId);
        if (ownerId <= 0) {
            throw new ValidationException("An erroneous owner id is specified");
        }

        return ItemDtoMapper.toItemDto(itemService.create(item, ownerId));
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto update(@PathVariable int itemId, @Valid @RequestBody ItemDto itemDto, @RequestHeader(value = ItemHeader.X_HEADER_NAME, defaultValue = "0") int ownerId)
            throws ValidationException {
        log.info("Update item {}, ownerId {}: " + itemDto, itemId, ownerId);
        if (ownerId <= 0) {
            throw new ValidationException("An erroneous owner id is specified");
        }

        itemDto.setId(itemId);
        Item item = ItemDtoMapper.toItem(itemDto);

        return ItemDtoMapper.toItemDto(itemService.update(item, ownerId));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getOwnedItemsList(@RequestHeader(value = ItemHeader.X_HEADER_NAME, defaultValue = "0") int ownerId) {
        log.info("Get owned items list, ownerId {}", ownerId);
        if (ownerId <= 0) {
            throw new ValidationException("An erroneous owner id is specified");
        }

        return ItemDtoMapper.toItemDtoList(itemService.getOwnedItemsList(ownerId));
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto get(@PathVariable int itemId) {
        log.info("Get itemId {}", itemId);
        return ItemDtoMapper.toItemDto(itemService.getById(itemId));
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> search(@RequestParam(defaultValue = "") String text) {
        log.info("Search text '{}'", text);
        if (!text.isBlank()) {
            return ItemDtoMapper.toItemDtoList(itemService.search(text));
        } else {
            return List.of();
        }
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable int itemId, @RequestHeader(value = ItemHeader.X_HEADER_NAME, defaultValue = "0") int ownerId) {
        log.info("Delete itemId {}, ownerId {}", itemId, ownerId);
        if (ownerId <= 0) {
            throw new ValidationException("An erroneous owner id is specified");
        }

        itemService.delete(itemId, ownerId);
    }

    private static class ItemDtoMapper {
        public static Item toItem(ItemDto itemDto) {
            if (itemDto != null)
                return new Item(itemDto.getId(), 0, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable());
            else return null;
        }

        public static ItemDto toItemDto(Item item) {
            if (item != null)
                return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
            else return null;
        }

        public static List<ItemDto> toItemDtoList(List<Item> itemList) {
            List<ItemDto> itemDtoList = new ArrayList<>();

            if (itemList != null) {
                for (Item item : itemList) {
                    itemDtoList.add(toItemDto(item));
                }
            }

            return itemDtoList;
        }
    }
}
