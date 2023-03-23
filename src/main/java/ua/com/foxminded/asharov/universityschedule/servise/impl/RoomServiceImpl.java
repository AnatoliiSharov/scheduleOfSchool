package ua.com.foxminded.asharov.universityschedule.servise.impl;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.asharov.universityschedule.dao.RoomDao;
import ua.com.foxminded.asharov.universityschedule.model.Room;

@Service
public class RoomServiceImpl implements ua.com.foxminded.asharov.universityschedule.service.RoomService {
    private static final Logger logger = LoggerFactory.getLogger(RoomServiceImpl.class);

    private final RoomDao roomDao;

    public RoomServiceImpl(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Room> retrieveAll() {
        logger.debug("retrieveAllRooms in RoomServiceFeature started");
        return roomDao.findAll();
    }

    @Override
    public Room retrieveById(Long roomId) {
        logger.debug("retrievRoomById in RoomServiceFeature started with roomId = {}", roomId);
        return roomDao.findById(roomId).orElse(new Room());
    }

    @Override
    public Room enter(Room room) {
        logger.debug("enter in RoomServiceFeature started with room = {}", room);
        return roomDao.save(room);
    }

    @Override
    public void removeById(Long id) {
        logger.debug("removeById in RoomServiceFeature started with id = {}", id);
        roomDao.deleteById(id);
    }

    @Override
    public List<Room> retrieveFreeByTime(LocalDate date, int numberPerDay) {
        logger.debug(
                "start removeById in retrieveSubstitutesByTime started with LocalTime date= {}, int numberPerDay = {}",
                date, numberPerDay);
        return roomDao.findFreeOneByTime(date, numberPerDay);
    }

}
