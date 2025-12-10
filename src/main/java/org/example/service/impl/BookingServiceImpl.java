package org.example.service.impl;

import jakarta.persistence.OptimisticLockException;
import org.example.model.*;
import org.example.repository.BookingDao;
import org.example.repository.FlightRunDao;
import org.example.service.BookingService;
import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingDao bookingDao;
    private final FlightRunDao flightRunDao;

    private final TransactionTemplate txTemplate;
    @Value("${booking.retry.max:3}")
    private int maxRetry;
    private static final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);

    @Autowired
    public BookingServiceImpl(BookingDao bookingDao, FlightRunDao flightRunDao, PlatformTransactionManager transactionManager) {
        this.bookingDao = bookingDao;
        this.flightRunDao = flightRunDao;
        this.txTemplate = new TransactionTemplate(transactionManager);
    }

    @Override
    public Booking bookSeats(User user, int flightRunId, int seatsRequired) {
        return bookSeatsWithRetry(user, flightRunId, seatsRequired, maxRetry);
    }

    @Override
    public Booking bookSeatsWithRetry(User user, int flightRunId, int seatsRequired, int maxTries) {
        int tries = 0;
        while (true) {
            tries++;
            try {
                return txTemplate.execute(status -> doBookOnce(flightRunId, user, seatsRequired));
            } catch (OptimisticLockingFailureException |
                     StaleObjectStateException |
                     OptimisticLockException e) {
                if (tries >= maxTries) {
                    throw new RuntimeException("Booking failed due to concurrent update after " + tries + " attempts.", e);
                }
            }
        }
    }

    @Transactional
    public Booking doBookOnce(int flightRunId, User user, int seatsRequired) {
        FlightRun run = flightRunDao.findById(flightRunId);

        if (run.getSeatAvailable() < seatsRequired) {
            throw new IllegalArgumentException("Not enough seats available");
        }

        run.setSeatAvailable(run.getSeatAvailable() - seatsRequired);

        //To simulate parallel booking, we can have counter, for first counter = 0, have Thread.sleep() and for second counter = 1 no sleep.
        //We will find second booking will succeed while first booking fails if 2nd booking make seats < seats required by first booking

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
