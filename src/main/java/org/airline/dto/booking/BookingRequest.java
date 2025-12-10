package org.airline.dto.booking;

public class BookingRequest {
    private int userId;
    private int flightRunId;
    private int seats;
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getFlightRunId() { return flightRunId; }
    public void setFlightRunId(int flightRunId) { this.flightRunId = flightRunId; }
    public int getSeats() { return seats; }
    public void setSeats(int seats) { this.seats = seats; }
}

