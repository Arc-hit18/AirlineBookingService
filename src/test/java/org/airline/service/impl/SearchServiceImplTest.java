package org.airline.service.impl;

import org.airline.model.Flight;
import org.airline.model.FlightRun;
import org.airline.repository.FlightRunDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SearchServiceImplTest {
    private FlightRunDao flightRunDao;
    private SearchServiceImpl searchService;

    @BeforeEach
    void setUp() {
        flightRunDao = mock(FlightRunDao.class);
        searchService = new SearchServiceImpl(flightRunDao);
    }

    @Test
    void testFindFlights_directOneWay() {
        Flight flight = Flight.builder().id(1).src("DEL").dest("BOM").name("F1").build();
        FlightRun fr = FlightRun.builder().id(10).flight(flight).runDate(LocalDate.now()).seatAvailable(10).cost(1000).build();
        when(flightRunDao.findRunsFromDate(any())).thenReturn(List.of(fr));
        searchService.refreshCache();
        Set<List<FlightRun>> result = searchService.findFlights(0, "DEL", "BOM", LocalDate.now());
        assertFalse(result.isEmpty());
        List<FlightRun> path = result.iterator().next();
        assertEquals(1, path.size());
        assertEquals("DEL", path.get(0).getFlight().getSrc());
        assertEquals("BOM", path.get(0).getFlight().getDest());
    }

    @Test
    void testFindFlights_indirectPath() {
        Flight f1 = Flight.builder().id(1).src("DEL").dest("JAI").name("F1").build();
        FlightRun r1 = FlightRun.builder().id(10).flight(f1).runDate(LocalDate.now()).seatAvailable(8).cost(1000).build();
        Flight f2 = Flight.builder().id(2).src("JAI").dest("BOM").name("F2").build();
        FlightRun r2 = FlightRun.builder().id(20).flight(f2).runDate(LocalDate.now()).seatAvailable(9).cost(800).build();
        when(flightRunDao.findRunsFromDate(any())).thenReturn(List.of(r1, r2));
        searchService.refreshCache();
        Set<List<FlightRun>> result = searchService.findFlights(1, "DEL", "BOM", LocalDate.now());
        assertTrue(result.stream().anyMatch(path -> path.size() == 2));
    }

    @Test
    void testFindFlights_noFlightsFound() {
        when(flightRunDao.findRunsFromDate(any())).thenReturn(List.of());
        searchService.refreshCache();
        Set<List<FlightRun>> result = searchService.findFlights(2, "DEL", "BOM", LocalDate.now());
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindFlights_excludeZeroSeats() {
        Flight f1 = Flight.builder().id(1).src("DEL").dest("BOM").name("F1").build();
        FlightRun r1 = FlightRun.builder().id(10).flight(f1).runDate(LocalDate.now()).seatAvailable(0).cost(1000).build();
        when(flightRunDao.findRunsFromDate(any())).thenReturn(List.of(r1));
        searchService.refreshCache();
        Set<List<FlightRun>> result = searchService.findFlights(0, "DEL", "BOM", LocalDate.now());
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindFlights_futureOnly() {
        Flight f1 = Flight.builder().id(1).src("DEL").dest("BOM").name("F1").build();
        FlightRun past = FlightRun.builder().id(11).flight(f1).runDate(LocalDate.now().minusDays(1)).seatAvailable(5).cost(1000).build();
        FlightRun future = FlightRun.builder().id(12).flight(f1).runDate(LocalDate.now().plusDays(1)).seatAvailable(5).cost(1000).build();
        when(flightRunDao.findRunsFromDate(any())).thenReturn(List.of(past, future));
        searchService.refreshCache();
        Set<List<FlightRun>> result = searchService.findFlights(0, "DEL", "BOM", LocalDate.now().plusDays(1));
        assertFalse(result.isEmpty());
    }

    @Test
    void testCache_isolation() {
        Flight f1 = Flight.builder().id(1).src("DEL").dest("BOM").name("F1").build();
        FlightRun fr = FlightRun.builder().id(10).flight(f1).runDate(LocalDate.now()).seatAvailable(10).cost(1000).build();
        when(flightRunDao.findRunsFromDate(any())).thenReturn(List.of(fr));
        searchService.refreshCache();
        // Mutate source list after refreshing cache
        when(flightRunDao.findRunsFromDate(any())).thenReturn(List.of());
        Set<List<FlightRun>> result = searchService.findFlights(0, "DEL", "BOM", LocalDate.now());
        assertFalse(result.isEmpty());
    }
}
