package org.example.repository;

import org.example.model.Flight;
import java.util.List;

public interface FlightDao {
    void save(Flight flight);
    Flight findById(int id);
    List<Flight> findAll();
}
