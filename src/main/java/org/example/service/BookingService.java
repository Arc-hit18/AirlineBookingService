package org.example.service;

import org.example.model.Booking;
import org.example.model.User;
import java.util.List;

/**
 * Service boundary for booking operations such as reserving seats and
 * retrieving booking information for users.
 */
public interface BookingService {

    /**
     * Book seats on a specific flight run for a user, using the configured retry
     * policy to handle optimistic locking / concurrent update conflicts under
     * the hood.
     *
     * @param user          user who is booking
     * @param flightRunId   identifier of the flight run
     * @param seatsRequired number of seats to reserve
     * @return created booking
     */
    Booking bookSeats(User user, int flightRunId, int seatsRequired);

    /**
     * Book seats with an explicit maximum number of retry attempts when
     * optimistic locking / concurrent update conflicts occur.
     *
     * @param user        user who is booking
     * @param flightRunId identifier of the flight run
     * @param seatsRequired number of seats to reserve
     * @param maxTries    maximum retry attempts
     * @return created booking
     */
    Booking bookSeatsWithRetry(User user, int flightRunId, int seatsRequired, int maxTries);

    /**
     * Fetch a booking by its identifier.
     *
     * @param id booking identifier
     * @return booking, or {@code null} if not found
     */
    Booking getBookingById(int id);

    /**
     * Fetch all bookings associated with a given user.
     *
     * @param userId user identifier
     * @return list of bookings for the user, possibly empty
     */
    List<Booking> getBookingsByUserId(int userId);
}
