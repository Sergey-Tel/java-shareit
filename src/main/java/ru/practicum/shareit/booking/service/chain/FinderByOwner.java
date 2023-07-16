package ru.practicum.shareit.booking.service.chain;

import ru.practicum.shareit.booking.enums.BookingRequestStatus;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.booking.enums.BookingRequestStatus.*;

public class FinderByOwner {
    private BookingFinder first = null;

    public FinderByOwner() {
        // PAST
        first = new BookingFinder() {
            @Override
            public List<Booking> find(BookingRepository bookingRepository, Long ownerId, BookingRequestStatus state) {
                if (state == PAST) {
                    return bookingRepository.findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(ownerId, LocalDateTime.now());
                }
                return next(bookingRepository, ownerId, state);
            }
        };

        // FUTURE
        first.add(new BookingFinder() {
            @Override
            public List<Booking> find(BookingRepository bookingRepository, Long ownerId, BookingRequestStatus state) {
                if (state == FUTURE) {
                    return bookingRepository.findByItemOwnerIdAndStartIsAfterOrderByStartDesc(ownerId, LocalDateTime.now());
                }
                return next(bookingRepository, ownerId, state);
            }
        });

        // CURRENT
        first.add(new BookingFinder() {
            @Override
            public List<Booking> find(BookingRepository bookingRepository, Long ownerId, BookingRequestStatus state) {
                if (state == CURRENT) {
                    return bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(ownerId, LocalDateTime.now(), LocalDateTime.now());
                }
                return next(bookingRepository, ownerId, state);
            }
        });

        // WAITING, REJECTED
        first.add(new BookingFinder() {
            @Override
            public List<Booking> find(BookingRepository bookingRepository, Long ownerId, BookingRequestStatus state) {
                if (state == WAITING || state == REJECTED) {
                    return bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.valueOf(state.name()));
                }
                return next(bookingRepository, ownerId, state);
            }
        });

        // ALL
        first.add(new BookingFinder() {
            @Override
            public List<Booking> find(BookingRepository bookingRepository, Long ownerId, BookingRequestStatus state) {
                return bookingRepository.findByItemOwnerIdOrderByStartDesc(ownerId);
            }
        });
    }

    public static BookingFinder getFinder() {
        return (new FinderByOwner()).first;
    }

}
