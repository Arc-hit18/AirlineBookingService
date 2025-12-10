package org.airline.repository.impl;

import org.airline.model.FlightRun;
import org.airline.repository.FlightRunDao;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public class FlightRunDaoImpl implements FlightRunDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(FlightRun run) {
        em.persist(run);
    }

    @Override
    public FlightRun findById(int id) {
        return em.find(FlightRun.class, id);
    }

    @Override
    public List<FlightRun> findByDate(LocalDate runDate) {
        return em.createQuery("SELECT fr FROM FlightRun fr JOIN FETCH fr.flight WHERE fr.runDate = :runDate", FlightRun.class)
                 .setParameter("runDate", runDate)
                 .getResultList();
    }

    @Override
    public List<FlightRun> findRunsFromDate(LocalDate fromDateInclusive) {
        return em.createQuery("SELECT fr FROM FlightRun fr JOIN FETCH fr.flight WHERE fr.runDate >= :fromDate", FlightRun.class)
                 .setParameter("fromDate", fromDateInclusive)
                 .getResultList();
    }

    @Override
    public List<FlightRun> findByDateAndSrc(String src, LocalDate runDate) {
        return em.createQuery("SELECT fr FROM FlightRun fr JOIN FETCH fr.flight WHERE fr.flight.src = :src AND fr.runDate = :runDate", FlightRun.class)
                 .setParameter("src", src)
                 .setParameter("runDate", runDate)
                 .getResultList();
    }

    @Override
    public void update(FlightRun run) {
        em.merge(run);
    }
}
