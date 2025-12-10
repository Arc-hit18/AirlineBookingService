package org.airline.service;

import org.airline.dto.search.SearchFlightResponse;
import java.time.LocalDate;
import java.util.List;

/**
 * Service for searching possible flight options (direct and indirect connections).
 */
public interface SearchService {
    /**
     * Finds and builds sorted SearchFlightResponse DTOs for flight paths between two airports on a given date.
     * @param k    maximum number of paths to return
     * @param src  origin airport code
     * @param dest destination airport code
     * @param date date of travel
     * @return sorted list of SearchFlightResponse (by total cost)
     */
    List<SearchFlightResponse> searchFlights(int k, String src, String dest, LocalDate date);
}
