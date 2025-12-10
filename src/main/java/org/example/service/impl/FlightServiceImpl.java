package org.example.service.impl;

import org.example.model.Flight;
import org.example.model.FlightRun;
import org.example.repository.FlightDao;
import org.example.repository.FlightRunDao;
import org.example.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FlightServiceImpl implements FlightService {
    private final FlightDao flightDao;
    private final FlightRunDao flightRunDao;
    @Autowired
    public FlightServiceImpl(FlightDao flightDao, FlightRunDao flightRunDao) {
        this.flightDao = flightDao;
        this.flightRunDao = flightRunDao;
    }
    @Override
    @Transactional
    public List<Integer> createFlightAndRuns(String src, String dest, String name, LocalDateTime arrivalTime, LocalDateTime departureTime, int months, int cost, int seats) {
        Flight flight = new Flight();
        flight.setSrc(src);
        flight.setDest(dest);
        flight.setName(name);
        flight.setArrivalTime(arrivalTime);
        flight.setDepartureTime(departureTime);
        flightDao.save(flight);
        List<Integer> runIds = new ArrayList<>();
        LocalDate currDate = departureTime.toLocalDate();
        LocalDate end = currDate.plusMonths(months);
        while (!currDate.isAfter(end)) {
            FlightRun run = new FlightRun();
            run.setFlight(flight);
            run.setCost(cost);
            run.setSeatAvailable(seats);
            run.setRunDate(currDate);
            flightRunDao.save(run);
            runIds.add(run.getId());
            currDate = currDate.plusDays(1);
        }
        return runIds;
    }

    @Override
    @Transactional
    public BulkFlightResult bulkCreateFlightsAndRuns(List<org.example.dto.flight.FlightRequest> flights) {
        int flightsCreated = 0;
        int runsCreated = 0;
        List<String> successNames = new ArrayList<>();
        List<String> failedNames = new ArrayList<>();
        for (org.example.dto.flight.FlightRequest req : flights) {
            try {
                List<Integer> runs = createFlightAndRuns(
                    req.getSrc(), req.getDest(), req.getName(),
                    req.getArrivalTime(), req.getDepartureTime(),
                    req.getMonths(), req.getCost(), req.getSeats()
                );
                flightsCreated += 1;
                runsCreated += runs.size();
                successNames.add(req.getName());
            } catch (RuntimeException ex) {
                failedNames.add(req.getName());
            }
        }
        return new BulkFlightResult(flightsCreated, runsCreated, successNames, failedNames);
    }

    @Override
    public Flight getFlightById(int id) {
        return flightDao.findById(id);
    }
}
