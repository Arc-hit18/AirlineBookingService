package org.airline.dto.flight;

import org.airline.model.Flight;

public class FlightDetailsResponse {
    private int id;
    private String src;
    private String dest;
    private String name;
    private String departureTime;
    private String arrivalTime;

    public FlightDetailsResponse(Flight f) {
        this.id = f.getId();
        this.src = f.getSrc();
        this.dest = f.getDest();
        this.name = f.getName();
        this.departureTime = f.getDepartureTime() != null ? f.getDepartureTime().toString() : null;
        this.arrivalTime = f.getArrivalTime() != null ? f.getArrivalTime().toString() : null;
    }
    public int getId() { return id; }
    public String getSrc() { return src; }
    public String getDest() { return dest; }
    public String getName() { return name; }
    public String getDepartureTime() { return departureTime; }
    public String getArrivalTime() { return arrivalTime; }
}
