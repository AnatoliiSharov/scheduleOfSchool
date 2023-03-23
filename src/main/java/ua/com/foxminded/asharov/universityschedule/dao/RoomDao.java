package ua.com.foxminded.asharov.universityschedule.dao;

import ua.com.foxminded.asharov.universityschedule.model.Room;

import java.time.LocalDate;
import java.util.List;

public interface RoomDao extends CrudEntityDao<Room, Long> {

    List<Room> findEqualsOrMoreCapacityRooms(int seatsCapacity);

    List<Room> findRoomByAddress(String address);

    List<Room> findFreeOneByTime(LocalDate date, int numberPerDay);

}
