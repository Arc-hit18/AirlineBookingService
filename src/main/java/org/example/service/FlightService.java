package org.example.service;

import org.example.model.Flight;
import java.time.LocalDateTime;
import java.util.List;

public interface FlightService {
    List<Integer> createFlightAndRuns(String src, String dest, String name, LocalDateTime arrivalTime, LocalDateTime departureTime, int months, int cost, int seats);
    BulkFlightResult bulkCreateFlightsAndRuns(List<org.example.dto.flight.FlightRequest> flights);
    Flight getFlightById(int id);

    class BulkFlightResult {
        private int flightsCreated;
        private int runsCreated;
        private List<String> successNames;
        private List<String> failedNames;
        public BulkFlightResult(int flightsCreated, int runsCreated, List<String> successNames, List<String> failedNames) {
            this.flightsCreated = flightsCreated;
            this.runsCreated = runsCreated;
            this.successNames = successNames;
            this.failedNames = failedNames;
        }
        public int getFlightsCreated() { return flightsCreated; }
        public int getRunsCreated() { return runsCreated; }
        public List<String> getSuccessNames() { return successNames; }
        public List<String> getFailedNames() { return failedNames; }
    }
}
