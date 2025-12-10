package org.airline.resource;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.airline.dto.search.SearchFlightResponse;
import org.airline.model.FlightRun;
import org.airline.service.SearchService;
import org.airline.exception.ApiError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.*;

@Tag(name = "Search", description = "Endpoints for searching direct/connecting flights")
@RestController
@RequestMapping("/search")
public class SearchResource {
    private final SearchService searchService;
    @Autowired
    public SearchResource(SearchService searchService) { this.searchService = searchService; }
    @Operation(summary = "Find flights between locations", description = "Searches for all direct and indirect flights up to k stops on a specific date.")
    @GetMapping
    public ResponseEntity<?> searchFlights(
        @Parameter(description = "Source airport code") @RequestParam String src,
        @Parameter(description = "Destination airport code") @RequestParam String dest,
        @Parameter(description = "Travel date in YYYY-MM-DD format") @RequestParam String date,
        @Parameter(description = "Maximum number of stopovers allowed (k >= 0)") @RequestParam int stops
    ) {
        List<SearchFlightResponse> responses = searchService.searchFlights(stops, src, dest, LocalDate.parse(date));
        if (responses.isEmpty()) {
            return ResponseEntity.status(404)
                .body(new ApiError(404, "NO_FLIGHTS_FOUND", "No valid flights found for the criteria."));
        }
        return ResponseEntity.ok(responses);
    }
}
