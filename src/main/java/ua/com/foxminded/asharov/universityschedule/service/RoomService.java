package ua.com.foxminded.asharov.universityschedule.service;

import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.asharov.universityschedule.entity.Room;

import java.time.LocalDate;
import java.util.List;

public interface RoomService {
    @Transactional(readOnly = true)
    List<Room> retrieveAll();

    @Transactional(readOnly = true)
    Room retrieveById(Long roomId);

    @Transactional(readOnly = true)
    List<Room> retrieveFreeByTime(LocalDate date, int numberPerDay);

    @Transactional
    Room enter(Room room);

    @Transactional
    void removeById(Long id);

}
