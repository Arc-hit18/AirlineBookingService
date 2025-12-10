package org.example.service;

import org.example.model.Flight;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service boundary for managing {@link Flight} definitions and their dated runs.
 */
public interface FlightService {

    /**
     * Create a flight and generate dated runs for the next given number of months.
     *
     * @param src           origin airport code
     * @param dest          destination airport code
     * @param name          human readable flight name
     * @param arrivalTime   arrival time of the base flight
     * @param departureTime departure time of the base flight
     * @param months        how many months of runs to generate
     * @param cost          base ticket cost per seat
     * @param seats         number of seats per run
     * @return list of created flight run identifiers
     */
    List<Integer> createFlightAndRuns(String src, String dest, String name,
                                      LocalDateTime arrivalTime, LocalDateTime departureTime,
                                      int months, int cost, int seats);

    /**
     * Bulk create multiple flights and their runs from a list of requests.
     *
     * @param flights list of flight creation requests
     * @return summary of how many flights/runs were created and which items succeeded or failed
     */
    BulkFlightResult bulkCreateFlightsAndRuns(List<org.example.dto.flight.FlightRequest> flights);

    /**
     * Fetch a flight by its identifier.
     *
     * @param id flight identifier
     * @return flight, or {@code null} if not found
     */
    Flight getFlightById(int id);

    /**
     * Result summary for bulk flight creation operations.
     */
    class BulkFlightResult {
        private int flightsCreated;
        private int runsCreated;
        private List<String> successNames;
        private List<String> failedNames;

        public BulkFlightResult(int flightsCreated, int runsCreated,
                                List<String> successNames, List<String> failedNames) {
            this.flightsCreated = flightsCreated;
            this.runsCreated = runsCreated;
            this.successNames = successNames;
            this.failedNames = failedNames;
        }

        /** @return number of flights successfully created */
        public int getFlightsCreated() { return flightsCreated; }

        /** @return number of runs successfully created */
        public int getRunsCreated() { return runsCreated; }

        /** @return names of flights created successfully */
        public List<String> getSuccessNames() { return successNames; }

        /** @return names of flights that failed to be created */
        public List<String> getFailedNames() { return failedNames; }
    }
}
