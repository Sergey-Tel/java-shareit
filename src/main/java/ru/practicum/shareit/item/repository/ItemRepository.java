package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerOrderByIdAsc(User owner);

    List<Item> findByOwnerOrderByIdAsc(User owner, Pageable pageable);

    List<Item> findByRequestIdIn(List<Long> requestIds);

    List<Item> findByRequestId(Long requestId);

    @Query(" select i from Item i " +
            "where i.isAvailable=true and (upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%')))")
    List<Item> search(String text, Pageable pageable);
}