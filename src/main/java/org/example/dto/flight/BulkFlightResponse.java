package org.example.dto.flight;

public class BulkFlightResponse {
    private int flightsCreated;
    private int runsCreated;
    private java.util.List<String> successNames;
    private java.util.List<String> failedNames;
    public BulkFlightResponse(int flightsCreated, int runsCreated,
                              java.util.List<String> successNames,
                              java.util.List<String> failedNames) {
        this.flightsCreated = flightsCreated;
        this.runsCreated = runsCreated;
        this.successNames = successNames;
        this.failedNames = failedNames;
    }
    public int getFlightsCreated() { return flightsCreated; }
    public int getRunsCreated() { return runsCreated; }
    public java.util.List<String> getSuccessNames() { return successNames; }
    public java.util.List<String> getFailedNames() { return failedNames; }
}

