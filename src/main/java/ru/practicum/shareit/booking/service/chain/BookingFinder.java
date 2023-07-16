package ru.practicum.shareit.booking.service.chain;

import ru.practicum.shareit.booking.enums.BookingRequestStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.util.List;

public class BookingFinder {
    private BookingFinder next;

    public static BookingFinder link(BookingFinder first, BookingFinder... chain) {
        BookingFinder head = first;
        for (BookingFinder item : chain) {
            head.next = item;
            head = item;
        }
        return first;
    }

    public void add(BookingFinder last) {
        if (next == null) {
            next = last;
        } else {
            next.add(last);
        }
    }

    public List<Booking> find(BookingRepository bookingRepository, Long userId, BookingRequestStatus state) {
        return null;
    }

    protected List<Booking> next(BookingRepository bookingRepository, Long userId, BookingRequestStatus state) {
        return (next == null) ? null : next.find(bookingRepository, userId, state);
    }
}
