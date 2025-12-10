package org.example.dto.booking;

import org.example.model.Booking;

public class BookingFetchResponse {
    private int bookingId;
    private int userId;
    private String userName;
    private int flightRunId;
    private int flightId;
    private String flightName;
    private int seatsReserved;
    private int amountPaid;
    private String status;
    public BookingFetchResponse(Booking booking) {
        this.bookingId = booking.getId();
        this.userId = booking.getUser().getId();
        this.userName = booking.getUser().getName();
        this.flightRunId = booking.getFlightRun().getId();
        this.flightId = booking.getFlightRun().getFlight().getId();
        this.flightName = booking.getFlightRun().getFlight().getName();
        this.seatsReserved = booking.getSeatsReserved();
        this.amountPaid = booking.getAmountPaid();
        this.status = booking.getStatus().name();
    }
    public int getBookingId() { return bookingId; }
    public int getUserId() { return userId; }
    public String getUserName() { return userName; }
    public int getFlightRunId() { return flightRunId; }
    public int getFlightId() { return flightId; }
    public String getFlightName() { return flightName; }
    public int getSeatsReserved() { return seatsReserved; }
    public int getAmountPaid() { return amountPaid; }
    public String getStatus() { return status; }
}
