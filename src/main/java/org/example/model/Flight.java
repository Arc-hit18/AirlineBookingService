package org.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "flights",
       indexes = {
           @Index(name = "idx_flights_src", columnList = "src"),
           @Index(name = "idx_flights_dest", columnList = "dest"),
           @Index(name = "idx_flights_name_unique", columnList = "name", unique = true)
       })
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String src;
    private String dest;
    @Column(unique = true)
    private String name;
    private LocalDateTime arrivalTime;
    private LocalDateTime departureTime;
}
