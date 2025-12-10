package org.example.repository;

import org.example.model.Booking;
import java.util.List;

public interface BookingDao {
    void save(Booking booking);
    Booking findById(int id);
    List<Booking> findByUserId(int userId);
}
