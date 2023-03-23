package ua.com.foxminded.asharov.universityschedule.repository.custom.impl;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import ua.com.foxminded.asharov.universityschedule.entity.Room;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomRoomRepositoryImplTest {

    @Autowired
    EntityManager entityManager;
    @Autowired
    CustomRoomRepositoryImpl roomCustRepImpl;
    
    DateTimeFormatter pattern;

    @BeforeEach
    void setUp() throws Exception {
        pattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }
    
    @Test
    @Sql(scripts = { "/db/clean_db.sql", "/db/TestData.sql" })
    void testFindFreeOneByTime() {
        List<Room> expected = Arrays.asList(new Room(10002L, "Str, bldg1, fl1, off2", 20),
                new Room(10003L, "Str, bldg1, fl2, off3", 30), new Room(10005L, "Str, bldg2, fl2, off2", 20));

            assertEquals(expected, roomCustRepImpl.findFreeOneByTime(LocalDate.parse("01-01-0001", pattern), 1));
        }


}
