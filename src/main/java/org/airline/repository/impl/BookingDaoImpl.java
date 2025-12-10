package org.airline.repository.impl;

import org.airline.model.Booking;
import org.airline.repository.BookingDao;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@Repository
public class BookingDaoImpl implements BookingDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void save(Booking booking) {
        em.persist(booking);
    }

    @Override
    public Booking findById(int id) {
        return em.find(Booking.class, id);
    }

    @Override
    public List<Booking> findByUserId(int userId) {
        return em.createQuery("SELECT b FROM Booking b JOIN FETCH b.flightRun fr JOIN FETCH fr.flight WHERE b.user.id = :userId", Booking.class)
            .setParameter("userId", userId)
            .getResultList();
    }
}
