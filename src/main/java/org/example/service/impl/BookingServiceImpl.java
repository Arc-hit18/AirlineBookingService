package org.example.service.impl;

import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import org.example.model.*;
import org.example.repository.BookingDao;
import org.example.repository.FlightRunDao;
import org.example.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingDao bookingDao;
    private final FlightRunDao flightRunDao;
    @Value("${booking.retry.max:3}")
    private int maxRetry;

    @Autowired
    public BookingServiceImpl(BookingDao bookingDao, FlightRunDao flightRunDao) {
        this.bookingDao = bookingDao;
        this.flightRunDao = flightRunDao;
    }

    @Override
    @Transactional
    public Booking bookSeats(User user, int flightRunId, int seatsRequired) {
        return bookSeatsWithRetry(user, flightRunId, seatsRequired, maxRetry);
    }

    @Override
    @Transactional
    public Booking bookSeatsWithRetry(User user, int flightRunId, int seatsRequired, int maxTries) {
        int tries = 0;
        while (true) {
            tries++;
            try {
                FlightRun run = flightRunDao.findById(flightRunId);
                if (run == null) throw new IllegalArgumentException("No such flight run");
                if (run.getSeatAvailable() < seatsRequired) throw new IllegalArgumentException("Not enough seats available");

                run.setSeatAvailable(run.getSeatAvailable() - seatsRequired);
                try {
                    flightRunDao.update(run); // will throw if concurrent modification
                } catch (OptimisticLockException ole) {
                    if (tries >= maxTries) throw new RuntimeException("Booking failed due to concurrent update. Please retry.");
                    continue; // retry
                }
                Booking booking = Booking.builder()
                        .flightRun(run)
                        .user(user)
                        .seatsReserved(seatsRequired)
                        .amountPaid(run.getCost() * seatsRequired)
                        .bookingDate(new Date())
                        .status(BookingStatus.RESERVED)
                        .createdAt(new Date())
                        .build();
                bookingDao.save(booking);
                return booking;
            } catch (OptimisticLockException ex) {
                if (tries >= maxTries) throw new RuntimeException("Booking failed due to too many conflicts.");
            }
        }
    }

    @Override
    public Booking getBookingById(int id) {
        return bookingDao.findById(id);
    }

    @Override
    public List<Booking> getBookingsByUserId(int userId) {
        return bookingDao.findByUserId(userId);
    }
}
