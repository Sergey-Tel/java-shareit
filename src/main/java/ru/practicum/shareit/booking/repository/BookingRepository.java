package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.BookingShortForItem;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(Long bookerId, LocalDateTime end);

    List<Booking> findByBookerIdAndStartIsAfterOrderByStartDesc(Long bookerId, LocalDateTime start);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status);

    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartAsc(Long bookerId, LocalDateTime start,
                                                                             LocalDateTime end);

    List<Booking> findByItemInOrderByStartDesc(List<Item> items);

    List<Booking> findByItemInAndEndIsBeforeOrderByStartDesc(List<Item> items, LocalDateTime end);

    List<Booking> findByItemInAndStartIsAfterOrderByStartDesc(List<Item> items, LocalDateTime start);

    List<Booking> findByItemInAndStatusOrderByStartDesc(List<Item> items, BookingStatus status);

    List<Booking> findByItemInAndStartIsBeforeAndEndIsAfterOrderByStartDesc(List<Item> items, LocalDateTime start,
                                                                            LocalDateTime end);

    List<Booking> findByItemOwnerIdOrderByStartDesc(Long owner);

    List<Booking> findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(Long owner, LocalDateTime end);

    List<Booking> findByItemOwnerIdAndStartIsAfterOrderByStartDesc(Long owner, LocalDateTime start);

    List<Booking> findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long owner, LocalDateTime start,
                                                                            LocalDateTime end);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long owner, BookingStatus status);

    List<BookingShortForItem> findByItemInAndStatus(List<Item> items, BookingStatus status);

    List<BookingShortForItem> findByItemAndStatus(Item item, BookingStatus status);

    BookingShortForItem findFirstByItemAndBookerAndStatus(Item item, User booker, BookingStatus status);
}
