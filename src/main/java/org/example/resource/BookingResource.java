package org.example.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.booking.BookingRequest;
import org.example.dto.booking.BookingResponse;
import org.example.dto.booking.BookingFetchResponse;
import org.example.model.Booking;
import org.example.model.User;
import org.example.service.BookingService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/booking")
@Tag(name = "Booking", description = "Endpoints for flight bookings")
public class BookingResource {
    private final BookingService bookingService;
    private final UserService userService;

    @Autowired
    public BookingResource(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @Operation(summary = "Book seats on a flight run", description = "Seat reservation using optimistic concurrency control.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse createBooking(@RequestBody BookingRequest req) {
        try {
            User user = userService.getUserById(req.getUserId());
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
            Booking booking = bookingService.bookSeats(
                user,
                req.getFlightRunId(),
                req.getSeats()
            );
            return new BookingResponse(booking);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        }
    }

    @Operation(summary = "Get booking by id")
    @GetMapping("/{id}")
    public BookingFetchResponse getBookingById(@PathVariable int id) {
        Booking booking = bookingService.getBookingById(id);
        if (booking == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found");
        return new BookingFetchResponse(booking);
    }

    @Operation(summary = "Get all bookings for user")
    @GetMapping("/user/{userId}")
    public List<BookingFetchResponse> getUserBookings(@PathVariable int userId) {
        List<Booking> bookings = bookingService.getBookingsByUserId(userId);
        return bookings.stream().map(BookingFetchResponse::new).collect(Collectors.toList());
    }
}