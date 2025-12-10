package org.example.repository;

import org.example.model.Booking;
import java.util.List;

/**
 * Data access abstraction for {@link Booking} entities.
 *
 * Implementations are responsible for persisting and retrieving booking records
 * from the underlying data store.
 */
public interface BookingDao {

    /**
     * Persist a new booking in the data store.
     *
     * @param booking booking to be saved
     */
    void save(Booking booking);

    /**
     * Find a booking by its primary key.
     *
     * @param id booking identifier
     * @return matching booking, or {@code null} if none found
     */
    Booking findById(int id);

    /**
     * Find all bookings created by a specific user.
     *
     * @param userId user identifier
     * @return list of bookings for the given user, possibly empty
     */
    List<Booking> findByUserId(int userId);
}
