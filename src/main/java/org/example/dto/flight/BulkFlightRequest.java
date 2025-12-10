package org.example.dto.flight;

import java.util.List;

public class BulkFlightRequest {
    private List<FlightRequest> flights;
    public List<FlightRequest> getFlights() { return flights; }
    public void setFlights(List<FlightRequest> flights) { this.flights = flights; }
}

