package org.example.resource;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.example.dto.flight.FlightRequest;
import org.example.dto.flight.FlightResponse;
import org.example.dto.flight.BulkFlightRequest;
import org.example.dto.flight.BulkFlightResponse;
import org.example.dto.flight.FlightDetailsResponse;
import org.example.model.Flight;
import org.example.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@Tag(name = "Flights", description = "Endpoints for creating flights and runs")
@RequestMapping("/flights")
public class FlightResource {
    private final FlightService flightService;
    @Autowired
    public FlightResource(FlightService flightService) {
        this.flightService = flightService;
    }

    @Operation(summary = "Create a flight and its flight runs for upcoming months")
    @PostMapping
    public FlightResponse createFlightWithRuns(@RequestBody FlightRequest req) {
        List<Integer> runIds = flightService.createFlightAndRuns(
            req.getSrc(), req.getDest(), req.getName(),
            req.getArrivalTime(), req.getDepartureTime(),
            req.getMonths(), req.getCost(), req.getSeats()
        );
        return new FlightResponse(runIds);
    }

    @Operation(summary = "Bulk create flights and their runs")
    @PostMapping("/bulk")
    public BulkFlightResponse bulkCreate(@RequestBody BulkFlightRequest bulk) {
        FlightService.BulkFlightResult res = flightService.bulkCreateFlightsAndRuns(bulk.getFlights());
        return new BulkFlightResponse(res.getFlightsCreated(), res.getRunsCreated(), res.getSuccessNames(), res.getFailedNames());
    }

    @Operation(summary = "Get flight details by id")
    @GetMapping("/{id}")
    public FlightDetailsResponse getFlightDetails(@PathVariable int id) {
        Flight flight = flightService.getFlightById(id);
        if (flight == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight not found");
        return new FlightDetailsResponse(flight);
    }
}
