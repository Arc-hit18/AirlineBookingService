package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings",
       indexes = {
           @Index(name = "idx_booking_user", columnList = "user_id"),
           @Index(name = "idx_booking_run", columnList = "flight_run_id"),
           @Index(name = "idx_booking_status", columnList = "status")
       })
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_run_id")
    private FlightRun flightRun;

    @Temporal(TemporalType.TIMESTAMP)
    private Date bookingDate;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private int seatsReserved;
    private int amountPaid;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createdAt;
}
