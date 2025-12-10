package org.airline.dto.flight;

import java.time.LocalDateTime;

public class FlightRequest {
    private String src;
    private String dest;
    private String name;
    private LocalDateTime arrivalTime;
    private LocalDateTime departureTime;
    private int months;
    private int cost;
    private int seats;
    public String getSrc() { return src; }
    public void setSrc(String src) { this.src = src; }
    public String getDest() { return dest; }
    public void setDest(String dest) { this.dest = dest; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }
    public int getMonths() { return months; }
    public void setMonths(int months) { this.months = months; }
    public int getCost() { return cost; }
    public void setCost(int cost) { this.cost = cost; }
    public int getSeats() { return seats; }
    public void setSeats(int seats) { this.seats = seats; }
}

