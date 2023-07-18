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

    public FinderByBooker(BookingRepository bookingRepository) {
        // PAST
        first = new BookingFinder(bookingRepository, PAST) {
            @Override
            public List<Booking> findBookings(long userId) {
                return bookingRepository.findByBookerIdAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now());
            }
        };

        // FUTURE
        first.add(new BookingFinder(bookingRepository, FUTURE) {
            @Override
            public List<Booking> findBookings(long userId) {
                return bookingRepository.findByBookerIdAndStartIsAfterOrderByStartDesc(userId, LocalDateTime.now());
            }
        });

        // CURRENT
        first.add(new BookingFinder(bookingRepository, CURRENT) {
            @Override
            public List<Booking> findBookings(long userId) {
                return bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartAsc(userId,
                        LocalDateTime.now(), LocalDateTime.now());
            }
        });

        // WAITING
        first.add(new BookingFinder(bookingRepository, WAITING) {
            @Override
            public List<Booking> findBookings(long userId) {
                return bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId,
                        BookingStatus.WAITING);
            }
        });

        // REJECTED
        first.add(new BookingFinder(bookingRepository, REJECTED) {
            @Override
            public List<Booking> findBookings(long userId) {
                return bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId,
                        BookingStatus.REJECTED);
            }
        });

        // ALL
        first.add(new BookingFinder(bookingRepository) {
            @Override
            public boolean isCorrectStatus(BookingRequestStatus status) {
                return true;
            }

            @Override
            public List<Booking> findBookings(long userId) {
                return bookingRepository.findByBookerIdOrderByStartDesc(userId);
            }
        });
    }

    public static BookingFinder getFinder(BookingRepository bookingRepository) {
        return (new FinderByBooker(bookingRepository)).first;
    }
}
