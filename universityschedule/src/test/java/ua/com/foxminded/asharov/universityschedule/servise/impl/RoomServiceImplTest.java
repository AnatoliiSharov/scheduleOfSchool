package ua.com.foxminded.asharov.universityschedule.servise.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ua.com.foxminded.asharov.universityschedule.dao.RoomDao;
import ua.com.foxminded.asharov.universityschedule.model.Room;
import ua.com.foxminded.asharov.universityschedule.service.RoomService;

@SpringBootTest(classes = {RoomServiceImpl.class})
class RoomServiceImplTest {

    @MockBean
    RoomDao roomDao;

    @Autowired
    RoomService roomServ;

    @Test
    void testRetrieveAll() {
        List<Room> expected = Arrays.asList(new Room(10001L, "address", 20), new Room(10002L, "address2", 10),
                new Room(10003L, "address3", 30));

        when(roomDao.findAll()).thenReturn(expected);
        assertEquals(expected, roomServ.retrieveAll());
        verify(roomDao).findAll();
    }

    @Test
    void testRetrieveById() {
        Room expected = new Room(10001L, "address", 20);
        Long roomId = 10001L;

        when(roomDao.findById(roomId)).thenReturn(Optional.of(expected));
        Room actual = roomServ.retrieveById(roomId);

        assertEquals(expected, actual);
        verify(roomDao).findById(roomId);
    }

    @ParameterizedTest
    @MethodSource("provideStringsForTestEnterTeacher")
    void testEnterTeacher(Room start, Room expected) {

        when(roomDao.save(start)).thenReturn(expected);
        Room actual = roomServ.enter(start);

        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(expected.getCapacity(), actual.getCapacity());
        assertEquals(expected.getId(), actual.getId());
        verify(roomDao).save(start);
    }

    private static Stream<Arguments> provideStringsForTestEnterTeacher() {
        return Stream.of(Arguments.of(new Room(10001L, "address", 20), new Room(10001L, "address", 20)),
                Arguments.of(new Room(null, "address", 20), new Room(10001L, "address", 20)));
    }

    @Test
    void testRemoveById() {
        roomServ.removeById(10002L);
        verify(roomDao).deleteById(10002L);
    }

    @Test
    void testRetrieveFreeByTime() {
        List<Room> expected = Arrays.asList(new Room(10002L, "Str, bldg1, fl1, off2", 20),
                new Room(10003L, "Str, bldg1, fl2, off3", 30), new Room(10005L, "Str, bldg2, fl2, off2", 20));

        when(roomDao.findFreeOneByTime(LocalDate.of(0001, 01, 01), 1)).thenReturn(expected);
        List<Room> actual = roomServ.retrieveFreeByTime(LocalDate.of(0001, 01, 01), 1);

        assertEquals(expected, actual);
        verify(roomDao).findFreeOneByTime(LocalDate.of(0001, 01, 01), 1);
    }

}
