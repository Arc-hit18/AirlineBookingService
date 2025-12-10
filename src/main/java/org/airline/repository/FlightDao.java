package org.airline.repository;

import org.airline.model.Flight;
import java.util.List;

/**
 * Data access abstraction for {@link Flight} entities.
 */
public interface FlightDao {

    /**
     * Persist a new flight definition.
     *
     * @param flight flight to be saved
     */
    void save(Flight flight);

    /**
     * Find a flight by its primary key.
     *
     * @param id flight identifier
     * @return matching flight, or {@code null} if none found
     */
    Flight findById(int id);

    /**
     * Retrieve all flights.
     *
     * @return list of all flights, possibly empty
     */
    List<Flight> findAll();
}
