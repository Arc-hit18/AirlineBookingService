package org.example.dto.booking;

import org.example.model.Booking;

public class BookingResponse {
    private int bookingId;
    private int flightRunId;
    private int userId;
    private int seatsReserved;
    private int amountPaid;
    private String status;
    public BookingResponse(Booking b) {
        this.bookingId = b.getId();
        this.flightRunId = b.getFlightRun().getId();
        this.userId = b.getUser().getId();
        this.seatsReserved = b.getSeatsReserved();
        this.amountPaid = b.getAmountPaid();
        this.status = b.getStatus().name();
    }
    public int getBookingId() { return bookingId; }
    public int getFlightRunId() { return flightRunId; }
    public int getUserId() { return userId; }
    public int getSeatsReserved() { return seatsReserved; }
    public int getAmountPaid() { return amountPaid; }
    public String getStatus() { return status; }
}

