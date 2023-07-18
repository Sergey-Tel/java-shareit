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

    public FinderByOwner(BookingRepository bookingRepository) {
        // PAST
        first = new BookingFinder(bookingRepository, PAST) {
            @Override
            public List<Booking> findBookings(long ownerId) {
                return bookingRepository.findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(ownerId, LocalDateTime.now());
            }
        };

        // FUTURE
        first.add(new BookingFinder(bookingRepository, FUTURE) {
            @Override
            public List<Booking> findBookings(long ownerId) {
                return bookingRepository.findByItemOwnerIdAndStartIsAfterOrderByStartDesc(ownerId, LocalDateTime.now());
            }
        });

        // CURRENT
        first.add(new BookingFinder(bookingRepository, CURRENT) {
            @Override
            public List<Booking> findBookings(long ownerId) {
                return bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(ownerId, LocalDateTime.now(), LocalDateTime.now());
            }
        });

        // WAITING
        first.add(new BookingFinder(bookingRepository, WAITING) {
            @Override
            public List<Booking> findBookings(long ownerId) {
                return bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.WAITING);
            }
        });

        // REJECTED
        first.add(new BookingFinder(bookingRepository, REJECTED) {
            @Override
            public List<Booking> findBookings(long ownerId) {
                return bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.REJECTED);
            }
        });

        // ALL
        first.add(new BookingFinder(bookingRepository) {
            @Override
            public boolean isCorrectStatus(BookingRequestStatus status) {
                return true;
            }

            @Override
            public List<Booking> findBookings(long ownerId) {
                return bookingRepository.findByItemOwnerIdOrderByStartDesc(ownerId);
            }
        });
    }

    public static BookingFinder getFinder(BookingRepository bookingRepository) {
        return (new FinderByOwner(bookingRepository)).first;
    }
}
