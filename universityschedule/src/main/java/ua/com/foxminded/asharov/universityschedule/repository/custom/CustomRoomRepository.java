package ua.com.foxminded.asharov.universityschedule.repository.custom;

import ua.com.foxminded.asharov.universityschedule.entity.Room;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface CustomRoomRepository {

    List<Room> findFreeOneByTime(LocalDate date, int numberPerDay);

}
