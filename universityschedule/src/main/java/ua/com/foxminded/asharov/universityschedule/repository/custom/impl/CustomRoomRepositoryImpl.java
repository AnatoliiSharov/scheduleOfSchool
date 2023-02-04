package ua.com.foxminded.asharov.universityschedule.repository.custom.impl;

import ua.com.foxminded.asharov.universityschedule.entity.Room;
import ua.com.foxminded.asharov.universityschedule.repository.custom.CustomRoomRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Repository
public class CustomRoomRepositoryImpl implements CustomRoomRepository {

    static final String SELECT_FREE_BY_TIME = "SELECT r FROM Room r WHERE r NOT IN(SELECT l.room FROM Lecture l WHERE l.serialNumberPerDay=:serialNumberPerDay AND l.date=:date)";

    @Autowired
    EntityManagerFactory factory;

    @PersistenceContext
    EntityManager em;

    @SuppressWarnings("unchecked")
    @Override
    public List<Room> findFreeOneByTime(LocalDate date, int numberPerDay) {
        log.debug("CustomRoomRepositoryImpl/findFreeOneByTime started with date = {}, numberPerDay = {}", date,
                numberPerDay);
        return em.createQuery(SELECT_FREE_BY_TIME).setParameter("serialNumberPerDay", numberPerDay)
                .setParameter("date", date).getResultList();
    }

}
