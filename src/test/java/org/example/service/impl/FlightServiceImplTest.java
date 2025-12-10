package org.example.service.impl;

import org.example.model.Flight;
import org.example.model.FlightRun;
import org.example.repository.FlightDao;
import org.example.repository.FlightRunDao;
import org.example.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FlightServiceImplTest {
    private FlightDao flightDao;
    private FlightRunDao flightRunDao;
    private FlightServiceImpl flightService;

    @BeforeEach
    void setUp() {
        flightDao = mock(FlightDao.class);
        flightRunDao = mock(FlightRunDao.class);
        flightService = new FlightServiceImpl(flightDao, flightRunDao);
    }

    @Test
    void testCreateFlightAndRuns_success() {
        doNothing().when(flightDao).save(any(Flight.class));
        doNothing().when(flightRunDao).save(any(FlightRun.class));
        List<Integer> ids = flightService.createFlightAndRuns("DEL","BOM","F1",
            LocalDateTime.now(), LocalDateTime.now(), 0, 1200, 50);
        assertFalse(ids.isEmpty());
        verify(flightDao).save(any());
        verify(flightRunDao, atLeastOnce()).save(any());
    }

    @Test
    void testGetFlightById_found() {
        Flight f = Flight.builder().id(11).build();
        when(flightDao.findById(11)).thenReturn(f);
        assertEquals(f, flightService.getFlightById(11));
    }

    @Test
    void testGetFlightById_notFound() {
        when(flightDao.findById(999)).thenReturn(null);
        assertNull(flightService.getFlightById(999));
    }

    @Test
    void testBulkCreate_successAndFail() {
        doNothing().when(flightDao).save(any());
        doNothing().when(flightRunDao).save(any());
        List<org.example.dto.flight.FlightRequest> reqs = List.of(
            buildReq("A"),
            buildReq("B")
        );
        FlightServiceImpl spy = spy(flightService);
        doReturn(List.of(1)).doThrow(new RuntimeException()).when(spy).createFlightAndRuns(anyString(), anyString(), anyString(), any(), any(), anyInt(), anyInt(), anyInt());
        FlightService.BulkFlightResult res = spy.bulkCreateFlightsAndRuns(reqs);
        assertEquals(2, res.getSuccessNames().size() + res.getFailedNames().size());
    }
    private org.example.dto.flight.FlightRequest buildReq(String name) {
        org.example.dto.flight.FlightRequest req = new org.example.dto.flight.FlightRequest();
        req.setSrc("DEL"); req.setDest("BOM"); req.setName(name);
        req.setArrivalTime(LocalDateTime.now()); req.setDepartureTime(LocalDateTime.now());
        req.setMonths(0); req.setCost(100); req.setSeats(25);
        return req;
    }
}
