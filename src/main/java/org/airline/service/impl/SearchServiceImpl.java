package org.airline.service.impl;

import org.airline.model.FlightRun;
import org.airline.repository.FlightRunDao;
import org.airline.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {
    private static final Logger log = LoggerFactory.getLogger(SearchServiceImpl.class);

    private final FlightRunDao flightRunDao;

    @Value("${search.cache.refreshRate:10000}")
    private long refreshRate;

    @Autowired
    public SearchServiceImpl(FlightRunDao flightRunDao) { this.flightRunDao = flightRunDao; }

    // Immutable cache reference; swaps are atomic to avoid readers seeing a cleared map.
    private final AtomicReference<Map<LocalDate, Map<String, List<FlightRun>>>> cacheRef =
            new AtomicReference<>(Collections.emptyMap());

    @PostConstruct
    public void initCache() {
        refreshCache();
    }

    @Scheduled(fixedRateString = "${search.cache.refreshRate:10000}")
    public void refreshCache() {
        // Load all dates; if volume is huge, consider limiting to a window
        LocalDate today = LocalDate.now();
        List<FlightRun> all = flightRunDao.findRunsFromDate(today).stream()
            .filter(fr -> fr.getSeatAvailable() > 0)          // skip runs with no seats
            .toList();
        Map<LocalDate, Map<String, List<FlightRun>>> next = all.stream()
            .collect(Collectors.groupingBy(FlightRun::getRunDate,
                     Collectors.groupingBy(fr -> fr.getFlight().getSrc(),
                         Collectors.mapping(fr -> fr, Collectors.toUnmodifiableList()))));
        Map<LocalDate, Map<String, List<FlightRun>>> immutableNext = next.entrySet().stream()
            .collect(Collectors.toUnmodifiableMap(
                Map.Entry::getKey,
                e -> Collections.unmodifiableMap(e.getValue())
            ));
        cacheRef.set(immutableNext);
        log.info("Search cache refreshed: days={}, totalRuns={}", immutableNext.size(), all.size());
    }

    @Override
    public Set<List<FlightRun>> findFlights(int k, String src, String dest, LocalDate date) {
        Map<String, List<FlightRun>> graph = cacheRef.get().getOrDefault(date, Collections.emptyMap());
        if (graph.isEmpty()) {
            return Collections.emptySet();
        }
        Set<List<FlightRun>> result = new HashSet<>();
        List<FlightRun> path = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        dfs(k, src, dest, graph, path, result, visited);
        return result;
    }
    private void dfs(int k, String curr, String dest, Map<String, List<FlightRun>> graph, List<FlightRun> path, Set<List<FlightRun>> result, Set<String> visited) {
        if (curr.equals(dest)) {
            result.add(new ArrayList<>(path));
            return;
        }
        if (k < 0) return;
        visited.add(curr);
        if(graph.get(curr) != null) {
            for (FlightRun fr : graph.get(curr)) {
                String next = fr.getFlight().getDest();
                if (!visited.contains(next)) {
                    path.add(fr);
                    dfs(k - 1, next, dest, graph, path, result, visited);
                    path.remove(path.size() - 1);
                }
            }
        }
        visited.remove(curr);
    }
}
