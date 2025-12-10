package org.example.service;

import org.example.model.Booking;
import org.example.model.User;
import java.util.List;

public interface BookingService {
    Booking bookSeats(User user, int flightRunId, int seatsRequired);
    Booking bookSeatsWithRetry(User user, int flightRunId, int seatsRequired, int maxTries);
    Booking getBookingById(int id);
    List<Booking> getBookingsByUserId(int userId);
}
