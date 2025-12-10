package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import jakarta.persistence.CascadeType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "flight_runs",
       indexes = {
           @Index(name = "idx_fr_runDate", columnList = "runDate"),
           @Index(name = "idx_fr_src_date", columnList = "runDate, flight_id")
       })
public class FlightRun {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int cost;
    private int seatAvailable;
    private LocalDate runDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @Version
    private int version;
}
