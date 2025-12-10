package org.airline.service.impl;

import org.airline.model.*;
import org.airline.repository.BookingDao;
import org.airline.repository.FlightRunDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.persistence.OptimisticLockException;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceImplTest {
    private BookingDao bookingDao;
    private FlightRunDao flightRunDao;
    private BookingServiceImpl bookingService;
    private PlatformTransactionManager platformTransactionManager;
    private FlightRun run;
    private User user;

    // Test-specific BookingServiceImpl that bypasses TransactionTemplate for unit tests
    static class TestableBookingServiceImpl extends BookingServiceImpl {
        public TestableBookingServiceImpl(BookingDao bookingDao, FlightRunDao flightRunDao, PlatformTransactionManager txManager) {
            super(bookingDao, flightRunDao, txManager);
        }
        @Override
        public Booking bookSeatsWithRetry(User user, int flightRunId, int seatsRequired, int maxTries) {
            int tries = 0;
            while (true) {
                tries++;
                try {
                    return doBookOnce(flightRunId, user, seatsRequired);
                } catch (OptimisticLockException e) {
                    if (tries >= maxTries) throw new RuntimeException("Booking failed due to concurrent update after " + tries + " attempts.", e);
                }
            }
        }
    }

    @BeforeEach
    void setUp() {
        bookingDao = mock(BookingDao.class);
        flightRunDao = mock(FlightRunDao.class);
        platformTransactionManager = mock(PlatformTransactionManager.class);
        bookingService = new TestableBookingServiceImpl(bookingDao, flightRunDao, platformTransactionManager);
        run = FlightRun.builder().id(1).seatAvailable(10).cost(100).flight(Flight.builder().id(2).name("F").build()).build();
        user = User.builder().id(4).name("Alice").build();
    }

    @Test
    void bookSeats_success() {
        when(flightRunDao.findById(1)).thenReturn(run);
        Booking result = bookingService.bookSeats(user, 1, 3);
        assertNotNull(result);
        assertEquals(3, result.getSeatsReserved());
        assertEquals(run, result.getFlightRun());
        assertEquals(user, result.getUser());
        assertEquals(7, run.getSeatAvailable());
        verify(bookingDao, times(1)).save(any(Booking.class));
    }

    @Test
    void bookSeats_insufficientSeats() {
        when(flightRunDao.findById(1)).thenReturn(run);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> bookingService.bookSeats(user, 1, 99));
        assertEquals("Not enough seats available", ex.getMessage());
        verify(bookingDao, never()).save(any());
    }

    @Test
    void bookSeats_tooManyRetries() {
        when(flightRunDao.findById(1)).thenReturn(run);
        BookingServiceImpl spyService = spy(bookingService);
        doThrow(new OptimisticLockException("conflict")).when(spyService).doBookOnce(1, user, 1);
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> spyService.bookSeats(user, 1, 1));
        assertNotNull(ex.getMessage());
        verify(bookingDao, never()).save(any());
    }

    @Test
    void bookSeatsWithRetry_succeedsOnSecondTry() {
        when(flightRunDao.findById(1)).thenReturn(run);
        BookingServiceImpl spyService = spy(bookingService);
        doThrow(new OptimisticLockException("conflict"))
                .doCallRealMethod()
                .when(spyService).doBookOnce(1, user, 2);
        Booking result = spyService.bookSeatsWithRetry(user, 1, 2, 2);
        assertNotNull(result);
        assertEquals(2, result.getSeatsReserved());
        assertEquals(8, run.getSeatAvailable());
        verify(bookingDao, times(1)).save(any(Booking.class));
    }

    @Test
    void getBookingById_found() {
        Booking b = Booking.builder().id(5).build();
        when(bookingDao.findById(5)).thenReturn(b);
        assertEquals(b, bookingService.getBookingById(5));
    }

    @Test
    void getBookingById_notFound() {
        when(bookingDao.findById(77)).thenReturn(null);
        assertNull(bookingService.getBookingById(77));
    }

    @Test
    void getBookingsByUserId() {
        List<Booking> bookings = List.of(
                Booking.builder().id(1).build(),
                Booking.builder().id(2).build()
        );
        when(bookingDao.findByUserId(4)).thenReturn(bookings);
        assertEquals(2, bookingService.getBookingsByUserId(4).size());
    }
}
