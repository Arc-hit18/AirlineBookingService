package org.example.service.impl;

import org.example.model.*;
import org.example.repository.BookingDao;
import org.example.repository.FlightRunDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Date;
import java.util.List;
import jakarta.persistence.OptimisticLockException;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookingServiceImplTest {
    private BookingDao bookingDao;
    private FlightRunDao flightRunDao;
    private BookingServiceImpl bookingService;
    private FlightRun run;
    private User user;

    @BeforeEach
    void setUp() {
        bookingDao = mock(BookingDao.class);
        flightRunDao = mock(FlightRunDao.class);
        bookingService = new BookingServiceImpl(bookingDao, flightRunDao);
        ReflectionTestUtils.setField(bookingService, "maxRetry", 2);
        run = FlightRun.builder().id(1).seatAvailable(10).cost(100).flight(Flight.builder().id(2).name("F").build()).build();
        user = User.builder().id(4).name("Alice").build();
    }

    @Test
    void testBookSeats_success() {
        when(flightRunDao.findById(1)).thenReturn(run);
        Booking b = bookingService.bookSeats(user, 1, 3);
        assertEquals(3, b.getSeatsReserved());
        assertEquals(run, b.getFlightRun());
        assertEquals(user, b.getUser());
        verify(bookingDao).save(any());
    }

    @Test
    void testBookSeats_insufficientSeats() {
        when(flightRunDao.findById(1)).thenReturn(run);
        assertThrows(IllegalArgumentException.class, () -> bookingService.bookSeats(user, 1, 99));
    }

    @Test
    void testBookSeats_tooManyRetries() {
        when(flightRunDao.findById(1)).thenReturn(run);
        doThrow(OptimisticLockException.class).when(flightRunDao).update(any());
        assertThrows(RuntimeException.class, () -> bookingService.bookSeats(user, 1, 1));
    }

    @Test
    void testBookSeatsWithRetry_succeedsOnSecondTry() {
        when(flightRunDao.findById(1)).thenReturn(run);
        doThrow(OptimisticLockException.class).doNothing().when(flightRunDao).update(run);
        Booking b = bookingService.bookSeatsWithRetry(user, 1, 2, 2);
        assertEquals(2, b.getSeatsReserved());
    }

    @Test
    void testGetBookingById_found() {
        Booking b = Booking.builder().id(5).build();
        when(bookingDao.findById(5)).thenReturn(b);
        assertEquals(b, bookingService.getBookingById(5));
    }

    @Test
    void testGetBookingById_notFound() {
        when(bookingDao.findById(77)).thenReturn(null);
        assertNull(bookingService.getBookingById(77));
    }

    @Test
    void testGetBookingsByUserId() {
        List<Booking> bookings = List.of(
                Booking.builder().id(1).build(),
                Booking.builder().id(2).build()
        );
        when(bookingDao.findByUserId(4)).thenReturn(bookings);
        assertEquals(2, bookingService.getBookingsByUserId(4).size());
    }
}
