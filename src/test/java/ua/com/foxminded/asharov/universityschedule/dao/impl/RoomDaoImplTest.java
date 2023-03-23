package ua.com.foxminded.asharov.universityschedule.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.jdbc.Sql;
import ua.com.foxminded.asharov.universityschedule.model.Room;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoomDaoImplTest {

    @Autowired
    JdbcTemplate template;

    @Autowired
    NamedParameterJdbcTemplate namedParameterTemplate;
    SimpleJdbcInsert simpleInsert;
    RoomDaoImpl roomDao;

    @BeforeEach
    void setUp() throws Exception {
        roomDao = new RoomDaoImpl(template, namedParameterTemplate);
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void findById() {
        Optional<Room> actualy = roomDao.findById(10001L);

        assertTrue(actualy.isPresent());
        assertEquals(new Room(10001L, "Str, bldg1, fl1, off1", 60), actualy.get());
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void findAll() {
        List<Room> expected = Arrays.asList(new Room(10001L, "Str, bldg1, fl1, off1", 60),
            new Room(10002L, "Str, bldg1, fl1, off2", 20), new Room(10003L, "Str, bldg1, fl2, off3", 30),
            new Room(10004L, "Str, bldg2, fl1, off1", 10), new Room(10005L, "Str, bldg2, fl2, off2", 20));

        assertEquals(expected, roomDao.findAll());
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void deleteById() {
        List<Room> expected = Arrays.asList(new Room(10002L, "Str, bldg1, fl1, off2", 20),
            new Room(10003L, "Str, bldg1, fl2, off3", 30), new Room(10004L, "Str, bldg2, fl1, off1", 10),
            new Room(10005L, "Str, bldg2, fl2, off2", 20));

        roomDao.deleteById(10001L);
        assertEquals(expected, roomDao.findAll());
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void findEqualsOrMoreCapacityRooms() {
        List<Room> expected = Arrays.asList(new Room(10001L, "Str, bldg1, fl1, off1", 60),
            new Room(10002L, "Str, bldg1, fl1, off2", 20), new Room(10003L, "Str, bldg1, fl2, off3", 30),
            new Room(10005L, "Str, bldg2, fl2, off2", 20));

        assertEquals(expected, roomDao.findEqualsOrMoreCapacityRooms(20));
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void findRoomByAddress() {
        List<Room> expected = Arrays.asList(new Room(10001L, "Str, bldg1, fl1, off1", 60));

        assertEquals(expected, roomDao.findRoomByAddress("Str, bldg1, fl1, off1"));
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void update() {
        Room expected = new Room(10001L, "changedAddress", 9999);

        assertEquals(expected, roomDao.save(expected));
        assertTrue(roomDao.findAll().contains(expected));
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void create() {
        Room actual = roomDao.save(new Room("newAddress", 9999));

        assertEquals("newAddress", actual.getAddress());
        assertEquals(9999, actual.getCapacity().intValue());
        assertTrue(roomDao.findAll().contains(actual));
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void testFindFreeOneByTime() {
        List<Room> expected = Arrays.asList(new Room(10002L, "Str, bldg1, fl1, off2", 20),
            new Room(10003L, "Str, bldg1, fl2, off3", 30), new Room(10005L, "Str, bldg2, fl2, off2", 20));

        assertEquals(expected, roomDao.findFreeOneByTime(LocalDate.of(0001, 01, 01), 1));
    }

}
