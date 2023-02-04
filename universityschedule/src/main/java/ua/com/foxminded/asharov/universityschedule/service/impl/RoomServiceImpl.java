package ua.com.foxminded.asharov.universityschedule.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.asharov.universityschedule.repository.*;
import ua.com.foxminded.asharov.universityschedule.entity.Room;
import ua.com.foxminded.asharov.universityschedule.exception.UniversityException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class RoomServiceImpl implements ua.com.foxminded.asharov.universityschedule.service.RoomService {
    private final RoomRepository roomRep;

    public RoomServiceImpl(RoomRepository roomRep) {
        this.roomRep = roomRep;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Room> retrieveAll() {
        log.debug("retrieveAllRooms in RoomServiceFeature started");
        return StreamSupport.stream(roomRep.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Room retrieveById(Long roomId) {
        log.debug("retrievRoomById in RoomServiceFeature started with roomId = {}", roomId);
        return roomRep.findById(roomId).orElseThrow(() -> new UniversityException(
                "dont find Room with RoomServiceImpl/retrieveById/findById when roomId = " + roomId));
    }

    @Override
    public Room enter(Room room) {
        log.debug("enter in RoomServiceFeature started with roomDto = {}", room);
        return roomRep.save(room);
    }

    @Override
    public void removeById(Long id) {
        log.debug("removeById in RoomServiceFeature started with id = {}", id);

        if (roomRep.existsById(id)) {
            roomRep.deleteById(id);
        } else {
            throw new UniversityException("dont find Room with RoomServiceImpl/removeById/existsById when id = " + id);
        }

    }

    @Override
    public List<Room> retrieveFreeByTime(LocalDate date, int numberPerDay) {
        log.debug(
                "start removeById in retrieveSubstitutesByTime started with LocalTime date= {}, int numberPerDay = {}",
                date, numberPerDay);
        return roomRep.findFreeOneByTime(date, numberPerDay);
    }

}
