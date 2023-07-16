package ru.practicum.shareit.booking.service.chain;

import ru.practicum.shareit.booking.enums.BookingRequestStatus;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.booking.enums.BookingRequestStatus.*;

public class FinderByBooker {
    private BookingFinder first = null;

    public FinderByBooker() {
        // PAST
        first = new BookingFinder() {
            @Override
            public List<Booking> find(BookingRepository bookingRepository, Long userId, BookingRequestStatus state) {
                if (state == PAST) {
                    return bookingRepository.findByBookerIdAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now());
                }
                return next(bookingRepository, userId, state);
            }
        };

        // FUTURE
        first.add(new BookingFinder() {
            @Override
            public List<Booking> find(BookingRepository bookingRepository, Long userId, BookingRequestStatus state) {
                if (state == FUTURE) {
                    return bookingRepository.findByBookerIdAndStartIsAfterOrderByStartDesc(userId, LocalDateTime.now());
                }
                return next(bookingRepository, userId, state);
            }
        });

        // CURRENT
        first.add(new BookingFinder() {
            @Override
            public List<Booking> find(BookingRepository bookingRepository, Long userId, BookingRequestStatus state) {
                if (state == CURRENT) {
                    return bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartAsc(userId,
                            LocalDateTime.now(), LocalDateTime.now());
                }
                return next(bookingRepository, userId, state);
            }
        });

        // WAITING, REJECTED
        first.add(new BookingFinder() {
            @Override
            public List<Booking> find(BookingRepository bookingRepository, Long userId, BookingRequestStatus state) {
                if (state == WAITING || state == REJECTED) {
                    return bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId,
                            BookingStatus.valueOf(state.name()));
                }
                return next(bookingRepository, userId, state);
            }
        });

        // ALL
        first.add(new BookingFinder() {
            @Override
            public List<Booking> find(BookingRepository bookingRepository, Long userId, BookingRequestStatus state) {
                return bookingRepository.findByBookerIdOrderByStartDesc(userId);
            }
        });
    }

    public static BookingFinder getFinder() {
        return (new FinderByBooker()).first;
    }
}
