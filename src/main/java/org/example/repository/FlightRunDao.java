package org.example.repository;

import org.example.model.FlightRun;
import java.time.LocalDate;
import java.util.List;

public interface FlightRunDao {
    void save(FlightRun run);
    FlightRun findById(int id);
    List<FlightRun> findByDateAndSrc(String src, LocalDate runDate);
    List<FlightRun> findByDate(LocalDate runDate);
    List<FlightRun> findRunsFromDate(LocalDate fromDateInclusive);
    void update(FlightRun run);
}
