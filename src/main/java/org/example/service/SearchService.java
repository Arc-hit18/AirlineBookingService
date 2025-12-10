package org.example.service;

import org.example.model.FlightRun;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface SearchService {
    Set<List<FlightRun>> findFlights(int k, String src, String dest, java.time.LocalDate date);
}
