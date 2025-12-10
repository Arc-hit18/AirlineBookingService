package org.airline.dto.flight;

import java.util.List;

public class FlightResponse {
    private List<Integer> runIds;
    public FlightResponse(List<Integer> runIds) { this.runIds = runIds; }
    public List<Integer> getRunIds() { return runIds; }
}

