package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.common.Common;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto addNewBooking(@RequestHeader(Common.X_HEADER_NAME) Long userId,
                                            @RequestBody BookingRequestDto bookingRequestDto) {
        return bookingService.addNewBooking(userId, bookingRequestDto);
    }

    @PatchMapping(value = "/{bookingId}")
    public BookingResponseDto approveBooking(@RequestHeader(Common.X_HEADER_NAME) Long userId,
                                             @PathVariable Long bookingId,
                                             @RequestParam(name = "approved") Boolean isApproved) {
        return bookingService.approveBooking(userId, bookingId, isApproved);
    }

    @GetMapping(value = "/{bookingId}")
    public BookingResponseDto getBookingById(@RequestHeader(Common.X_HEADER_NAME) Long userId,
                                             @PathVariable Long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingResponseDto> getAllBookings(@RequestHeader(Common.X_HEADER_NAME) Long userId,
                                                   @RequestParam(defaultValue = "ALL") String state,
                                                   @RequestParam(defaultValue = "0") int from,
                                                   @RequestParam(defaultValue = "10") int size) {
        return bookingService.getAllBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getAllBookingsForAllItems(@RequestHeader(Common.X_HEADER_NAME) Long userId,
                                                              @RequestParam(defaultValue = "ALL") String state,
                                                              @RequestParam(defaultValue = "0") int from,
                                                              @RequestParam(defaultValue = "10") int size) {
        return bookingService.getAllBookingsForOwner(userId, state, from, size);
    }
}