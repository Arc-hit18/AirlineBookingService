package org.example.repository.impl;

import org.example.model.Flight;
import org.example.repository.FlightDao;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@Repository
public class FlightDaoImpl implements FlightDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(Flight flight) {
        em.persist(flight);
    }

    @Override
    public Flight findById(int id) {
        return em.find(Flight.class, id);
    }

    @Override
    public List<Flight> findAll() {
        return em.createQuery("SELECT f FROM Flight f", Flight.class).getResultList();
    }
}
