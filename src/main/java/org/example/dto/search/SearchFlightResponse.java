package org.example.dto.search;

import java.util.List;

public class SearchFlightResponse {
    private List<Integer> runIds;
    private List<String> flightNames;
    private int totalCost;

    public SearchFlightResponse(List<Integer> runIds, List<String> flightNames, int totalCost) {
        this.runIds = runIds;
        this.flightNames = flightNames;
        this.totalCost = totalCost;
    }
    public List<Integer> getRunIds() { return runIds; }
    public List<String> getFlightNames() { return flightNames; }
    public int getTotalCost() { return totalCost; }
}

