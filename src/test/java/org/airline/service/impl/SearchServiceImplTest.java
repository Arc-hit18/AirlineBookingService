package org.airline.service.impl;

import org.airline.dto.search.SearchFlightResponse;
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
    void testSearchFlights_directOneWay() {
        Flight flight = Flight.builder().id(1).src("DEL").dest("BOM").name("F1").build();
        FlightRun fr = FlightRun.builder().id(10).flight(flight).runDate(LocalDate.now()).seatAvailable(10).cost(1000).build();
        when(flightRunDao.findRunsFromDate(any())).thenReturn(List.of(fr));
        searchService.refreshCache();
        List<SearchFlightResponse> responses = searchService.searchFlights(0, "DEL", "BOM", LocalDate.now());
        assertFalse(responses.isEmpty());
        SearchFlightResponse response = responses.get(0);
        assertEquals(1, response.getRunIds().size());
        assertEquals(1, response.getFlightNames().size());
        assertEquals(1000, response.getTotalCost());
        assertEquals("F1", response.getFlightNames().get(0));
    }

    @Test
    void testSearchFlights_indirectPath() {
        Flight f1 = Flight.builder().id(1).src("DEL").dest("JAI").name("F1").build();
        FlightRun r1 = FlightRun.builder().id(10).flight(f1).runDate(LocalDate.now()).seatAvailable(8).cost(1000).build();
        Flight f2 = Flight.builder().id(2).src("JAI").dest("BOM").name("F2").build();
        FlightRun r2 = FlightRun.builder().id(20).flight(f2).runDate(LocalDate.now()).seatAvailable(9).cost(800).build();
        when(flightRunDao.findRunsFromDate(any())).thenReturn(List.of(r1, r2));
        searchService.refreshCache();
        List<SearchFlightResponse> responses = searchService.searchFlights(1, "DEL", "BOM", LocalDate.now());
        assertTrue(responses.stream().anyMatch(r -> r.getRunIds().size() == 2));
        SearchFlightResponse response = responses.stream().filter(r -> r.getRunIds().size() == 2).findFirst().orElseThrow();
        assertEquals(2, response.getFlightNames().size());
        assertEquals(1800, response.getTotalCost());
    }

    @Test
    void testSearchFlights_noFlightsFound() {
        when(flightRunDao.findRunsFromDate(any())).thenReturn(List.of());
        searchService.refreshCache();
        List<SearchFlightResponse> responses = searchService.searchFlights(2, "DEL", "BOM", LocalDate.now());
        assertTrue(responses.isEmpty());
    }

    @Test
    void testSearchFlights_excludeZeroSeats() {
        Flight f1 = Flight.builder().id(1).src("DEL").dest("BOM").name("F1").build();
        FlightRun r1 = FlightRun.builder().id(10).flight(f1).runDate(LocalDate.now()).seatAvailable(0).cost(1000).build();
        when(flightRunDao.findRunsFromDate(any())).thenReturn(List.of(r1));
        searchService.refreshCache();
        List<SearchFlightResponse> responses = searchService.searchFlights(0, "DEL", "BOM", LocalDate.now());
        assertTrue(responses.isEmpty());
    }

    @Test
    void testSearchFlights_futureOnly() {
        Flight f1 = Flight.builder().id(1).src("DEL").dest("BOM").name("F1").build();
        FlightRun past = FlightRun.builder().id(11).flight(f1).runDate(LocalDate.now().minusDays(1)).seatAvailable(5).cost(1000).build();
        FlightRun future = FlightRun.builder().id(12).flight(f1).runDate(LocalDate.now().plusDays(1)).seatAvailable(5).cost(1000).build();
        when(flightRunDao.findRunsFromDate(any())).thenReturn(List.of(past, future));
        searchService.refreshCache();
        List<SearchFlightResponse> responses = searchService.searchFlights(0, "DEL", "BOM", LocalDate.now().plusDays(1));
        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
        assertEquals(1000, responses.get(0).getTotalCost());
    }

    @Test
    void testCache_isolation() {
        Flight f1 = Flight.builder().id(1).src("DEL").dest("BOM").name("F1").build();
        FlightRun fr = FlightRun.builder().id(10).flight(f1).runDate(LocalDate.now()).seatAvailable(10).cost(1000).build();
        when(flightRunDao.findRunsFromDate(any())).thenReturn(List.of(fr));
        searchService.refreshCache();
        // Mutate source list after refreshing cache
        when(flightRunDao.findRunsFromDate(any())).thenReturn(List.of());
        List<SearchFlightResponse> responses = searchService.searchFlights(0, "DEL", "BOM", LocalDate.now());
        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
        assertEquals(1000, responses.get(0).getTotalCost());
    }
}
