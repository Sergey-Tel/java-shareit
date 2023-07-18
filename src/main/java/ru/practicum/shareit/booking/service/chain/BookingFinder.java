package ru.practicum.shareit.booking.service.chain;

import ru.practicum.shareit.booking.enums.BookingRequestStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.util.ArrayList;
import java.util.List;

public abstract class BookingFinder {
    private BookingFinder next;
    private BookingRepository bookingRepository;
    private BookingRequestStatus state;

    public BookingFinder(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public BookingFinder(BookingRepository bookingRepository, BookingRequestStatus state) {
        this.bookingRepository = bookingRepository;
        this.state = state;
    }

    public abstract List<Booking> findBookings(long userId);

    public boolean isCorrectStatus(BookingRequestStatus status) {
        return status == state;
    }

    public List<Booking> find(BookingRequestStatus status, long userId) {
        if (isCorrectStatus(status)) {
            return findBookings(userId);
        }
        if (next == null) {
            return new ArrayList<>();
        }
        return next.find(status, userId);
    }

    public static BookingFinder link(BookingFinder first, BookingFinder... list) {
        BookingFinder head = first;
        for (BookingFinder item : list) {
            head.next = item;
            head = item;
        }
        return head;
    }

    public void add(BookingFinder last) {
        if (next == null) {
            next = last;
        } else {
            next.add(last);
        }
    }

}
