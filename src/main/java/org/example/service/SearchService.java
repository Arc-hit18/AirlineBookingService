package org.example.service;

import org.example.model.FlightRun;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * Service for searching possible flight options (direct and indirect connections).
 */
public interface SearchService {

    /**
     * Find up to {@code k} flight paths between two airports on a given date.
     * Each path is represented as an ordered list of {@link FlightRun} segments
     * (direct flights will contain a single element).
     *
     * @param k    maximum number of paths to return
     * @param src  origin airport code
     * @param dest destination airport code
     * @param date date of travel
     * @return set of distinct paths, each a list of flight runs
     */
    Set<List<FlightRun>> findFlights(int k, String src, String dest, LocalDate date);
}
