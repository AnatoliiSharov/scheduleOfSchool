package ua.com.foxminded.asharov.universityschedule.dao.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.foxminded.asharov.universityschedule.model.Room;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static ua.com.foxminded.asharov.universityschedule.model.Room.*;

public class RoomDaoUtil implements DaoUtil<Room> {
    private static final Logger logger = LoggerFactory.getLogger(RoomDaoUtil.class);

    @Override
    public Room mapRow(ResultSet rs, int rowNum) throws SQLException {
        Room room = new Room();

        room.setId(rs.getLong(ROOM_ID));
        room.setAddress(rs.getString(ROOM_ADDRESS));
        room.setCapacity(rs.getInt(ROOM_CAPACITY));
        logger.debug("Room mapRow return course = {}", room);
        return room;
    }

    @Override
    public Map<String, Object> matchParam(Room entity) {
        Map<String, Object> param = new HashMap<>();

        param.put(ROOM_ID, entity.getId());
        param.put(ROOM_ADDRESS, entity.getAddress());
        param.put(ROOM_CAPACITY, entity.getCapacity());
        logger.debug("Room matchParam return param = {}", param);
        return param;
    }

}
