package org.example.repository;

import org.example.model.FlightRun;
import java.time.LocalDate;
import java.util.List;

/**
 * Data access abstraction for {@link FlightRun} entities (individual dated runs of a flight).
 */
public interface FlightRunDao {

    /**
     * Persist a new flight run.
     *
     * @param run flight run to be saved
     */
    void save(FlightRun run);

    /**
     * Find a flight run by its primary key.
     *
     * @param id run identifier
     * @return matching run, or {@code null} if none found
     */
    FlightRun findById(int id);

    /**
     * Find all runs for a given origin and date.
     *
     * @param src origin airport code
     * @param runDate date of the run
     * @return list of matching runs, possibly empty
     */
    List<FlightRun> findByDateAndSrc(String src, LocalDate runDate);

    /**
     * Find all runs on a specific date.
     *
     * @param runDate date of the run
     * @return list of matching runs, possibly empty
     */
    List<FlightRun> findByDate(LocalDate runDate);

    /**
     * Find all runs from the given date (inclusive) onwards.
     *
     * @param fromDateInclusive start date (inclusive)
     * @return list of runs from the given date, possibly empty
     */
    List<FlightRun> findRunsFromDate(LocalDate fromDateInclusive);

    /**
     * Update an existing flight run.
     *
     * @param run run entity with updated state
     */
    void update(FlightRun run);
}
