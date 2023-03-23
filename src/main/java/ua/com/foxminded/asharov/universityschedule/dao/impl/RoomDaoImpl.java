package ua.com.foxminded.asharov.universityschedule.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.asharov.universityschedule.dao.AbstractCrudEntityDao;
import ua.com.foxminded.asharov.universityschedule.dao.RoomDao;
import ua.com.foxminded.asharov.universityschedule.dao.util.RoomDaoUtil;
import ua.com.foxminded.asharov.universityschedule.model.Room;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static ua.com.foxminded.asharov.universityschedule.model.Room.ROOM_ID;
import static ua.com.foxminded.asharov.universityschedule.model.Room.ROOM_TABLE_NAME;

@Repository
public class RoomDaoImpl extends AbstractCrudEntityDao<Room, Long> implements RoomDao {
    private static final Logger logger = LoggerFactory.getLogger(RoomDaoImpl.class);

    static final String FIND_BY_ID = "SELECT * FROM rooms WHERE room_id = ?";
    static final String SELECT_ALL = "SELECT * FROM rooms";
    static final String DELETE_BY_ID = "DELETE FROM rooms WHERE room_id = ?";
    static final String SELECT_BY_ADDRESS = "SELECT * FROM rooms WHERE room_address = ?";
    static final String SELECT_EQUALS_OR_MORE_CAPACITY = "SELECT * FROM rooms WHERE room_CAPACITY >= ?";
    static final String UPDATE_BY_ID = "UPDATE rooms SET room_address = :room_address, room_capacity = :room_capacity WHERE room_id = :room_id";
    static final String SELECT_FREE_BY_TIME = "SELECT * FROM rooms AS r WHERE r.room_id NOT IN(SELECT l.room_id FROM lectures AS l WHERE date_lecture = ? and day_lecture_number = ?)";

    JdbcTemplate template;
    NamedParameterJdbcTemplate namedParameterTemplate;
    SimpleJdbcInsert simpleInsert;
    RoomDaoUtil roomDaoUtil;

    public RoomDaoImpl(JdbcTemplate template, NamedParameterJdbcTemplate namedParameterTemplate) {
        this.template = template;
        this.namedParameterTemplate = namedParameterTemplate;
        this.simpleInsert = new SimpleJdbcInsert(template).withTableName(ROOM_TABLE_NAME)
            .usingGeneratedKeyColumns(ROOM_ID);
        this.roomDaoUtil = new RoomDaoUtil();
    }

    @Override
    public Optional<Room> findById(Long id) {
        logger.debug("start findById in RoomDaoImpl started with id = {}", id);

        if (id == null) {
            logger.debug("start findById in RoomDaoImpl return {} with id = {}", "EMPTY", id);
            return Optional.empty();
        }
        try {
            return Optional.of(template.queryForObject(FIND_BY_ID, roomDaoUtil::mapRow, id));
        } catch (EmptyResultDataAccessException e) {
            logger.error("start findById in RoomDaoImpl has exception {} and return {} with id = {}", e, "EMPTY", id);
            return Optional.empty();
        }
    }

    @Override
    public List<Room> findAll() {
        logger.debug("start findAll in RoomDaoImpl started");
        return template.query(SELECT_ALL, roomDaoUtil::mapRow);
    }

    @Override
    public void deleteById(Long id) {
        logger.debug("start deleteById in RoomDaoImpl started with id = {}", id);
        template.update(DELETE_BY_ID, id);
    }

    @Override
    public List<Room> findEqualsOrMoreCapacityRooms(int seatsCapacity) {
        logger.debug("start findEqualsOrMoreCapacityRooms in RoomDaoImpl started with seatsCapacity = {}",
            seatsCapacity);
        return template.query(SELECT_EQUALS_OR_MORE_CAPACITY, roomDaoUtil::mapRow, seatsCapacity);
    }

    @Override
    public List<Room> findRoomByAddress(String address) {
        logger.debug("start findRoomByAddress in RoomDaoImpl started with address = {}", address);
        return template.query(SELECT_BY_ADDRESS, roomDaoUtil::mapRow, address);
    }

    @Override
    protected Room update(Room entity) {
        logger.debug("start update in RoomDaoImpl started with Room = {}", entity);

        if (namedParameterTemplate.update(UPDATE_BY_ID, roomDaoUtil.matchParam(entity)) != 1) {
            logger.error("update for Room {} with Room = {}", "Unable to update Room ", entity);
            throw new EmptyResultDataAccessException("Unable to update room " + entity, 0);
        }
        logger.debug("update in RoomDaoImpl was success with result = {}", entity);
        return entity;
    }

    @Override
    protected Room create(Room entity) {
        logger.debug("start create in RoomDaoImpl started with Room = {}", entity);
        entity.setId(simpleInsert.executeAndReturnKey(roomDaoUtil.matchParam(entity)).longValue());
        logger.debug("create in RoomDaoImpl was success with result = {}", entity);
        return entity;
    }

    @Override
    public List<Room> findFreeOneByTime(LocalDate date, int numberPerDay) {
        logger.debug("start findFreeOneByTime in RoomDaoImpl started with date = {}, numberPerDay = {}", date,
            numberPerDay);
        return template.query(SELECT_FREE_BY_TIME, roomDaoUtil::mapRow, date, numberPerDay);
    }

}
