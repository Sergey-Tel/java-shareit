package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.common.Common;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class BookingControllerTest {

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    private BookingRequestDto bookingRequestDto;
    private BookingResponseDto bookingResponseDto;

    private DateTimeFormatter fmt = DateTimeFormatter.ofPattern(Common.DT_FORMAT);

    @BeforeEach
    void setUp() {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Test item name")
                .description("Test item description")
                .available(true)
                .build();
        UserDto bookerDto = UserDto.builder()
                .id(1L)
                .name("Test user name")
                .email("test@test.com")
                .build();

        LocalDateTime start = LocalDateTime.now().plusMinutes(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        bookingRequestDto = BookingRequestDto.builder()
                .itemId(1L)
                .start(start)
                .end(end)
                .build();

        bookingResponseDto = BookingResponseDto.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(itemDto)
                .booker(bookerDto)
                .status(BookingStatus.APPROVED)
                .build();
    }

    @Test
    void addNewBooking() throws Exception {
        when(bookingService.addNewBooking(anyLong(), any()))
                .thenReturn(bookingResponseDto);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Common.X_HEADER_NAME, "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingResponseDto.getStart().format(fmt))))
                .andExpect(jsonPath("$.end", is(bookingResponseDto.getEnd().format(fmt))))
                .andExpect(jsonPath("$.item").exists())
                .andExpect(jsonPath("$.booker").exists())
                .andExpect(jsonPath("$.status", is(bookingResponseDto.getStatus().toString())));
    }

    @Test
    void approveBooking() throws Exception {
        when(bookingService.approveBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingResponseDto);

        mvc.perform(patch("/bookings/{bookingId}", 1)
                        .param("approved", "true")
                        .header(Common.X_HEADER_NAME, "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.start").exists())
                .andExpect(jsonPath("$.end").exists())
                .andExpect(jsonPath("$.item").exists())
                .andExpect(jsonPath("$.booker").exists())
                .andExpect(jsonPath("$.status", is(bookingResponseDto.getStatus().toString())));
    }

    @Test
    void getBookingById() throws Exception {
        when(bookingService.getBookingById(anyLong(), anyLong()))
                .thenReturn(bookingResponseDto);

        mvc.perform(get("/bookings/{bookingId}", 1)
                        .header(Common.X_HEADER_NAME, "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingResponseDto.getStart().format(fmt))))
                .andExpect(jsonPath("$.end", is(bookingResponseDto.getEnd().format(fmt))))
                .andExpect(jsonPath("$.item").exists())
                .andExpect(jsonPath("$.booker").exists())
                .andExpect(jsonPath("$.status", is(bookingResponseDto.getStatus().toString())));
    }

    @Test
    void getAllBookings() throws Exception {
        when(bookingService.getAllBookings(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingResponseDto));

        mvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "5")
                        .header(Common.X_HEADER_NAME, "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(bookingResponseDto.getStart().format(fmt))))
                .andExpect(jsonPath("$[0].end", is(bookingResponseDto.getEnd().format(fmt))))
                .andExpect(jsonPath("$[0].item").exists())
                .andExpect(jsonPath("$[0].booker").exists())
                .andExpect(jsonPath("$[0].status").exists());
    }

    @Test
    void getAllBookingsForAllItems() throws Exception {
        when(bookingService.getAllBookingsForOwner(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingResponseDto));

        mvc.perform(get("/bookings/owner")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "5")
                        .header(Common.X_HEADER_NAME, "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(bookingResponseDto.getStart().format(fmt))))
                .andExpect(jsonPath("$[0].end", is(bookingResponseDto.getEnd().format(fmt))))
                .andExpect(jsonPath("$[0].item").exists())
                .andExpect(jsonPath("$[0].booker").exists())
                .andExpect(jsonPath("$[0].status").exists());
    }

}
