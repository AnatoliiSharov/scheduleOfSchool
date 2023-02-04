package ua.com.foxminded.asharov.universityschedule.servise.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import net.bytebuddy.description.annotation.AnnotationValue.Sort;
import ua.com.foxminded.asharov.universityschedule.dto.LectureDto;
import ua.com.foxminded.asharov.universityschedule.entity.Course;
import ua.com.foxminded.asharov.universityschedule.entity.Group;
import ua.com.foxminded.asharov.universityschedule.entity.Lecture;
import ua.com.foxminded.asharov.universityschedule.entity.Room;
import ua.com.foxminded.asharov.universityschedule.entity.Teacher;
import ua.com.foxminded.asharov.universityschedule.repository.CourseRepository;
import ua.com.foxminded.asharov.universityschedule.repository.GroupRepository;
import ua.com.foxminded.asharov.universityschedule.repository.LectureRepository;
import ua.com.foxminded.asharov.universityschedule.repository.RoomRepository;
import ua.com.foxminded.asharov.universityschedule.repository.TeacherRepository;
import ua.com.foxminded.asharov.universityschedule.service.LectureService;
import ua.com.foxminded.asharov.universityschedule.service.impl.LectureServiceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = { LectureServiceImpl.class })
class LectureServiceImplTest {

    @MockBean
    LectureRepository lectureRep;
    @MockBean
    TeacherRepository teacherRep;
    @MockBean
    GroupRepository groupRep;
    @MockBean
    RoomRepository roomRep;
    @MockBean
    CourseRepository courseRep;

    @Autowired
    LectureService lectureServ;

    static Room rooms[];
    static Teacher teachers[];
    static Course courses[];
    static Group groups[];

    @BeforeEach
    void setUp() throws Exception {
        rooms = new Room[] { new Room(), new Room(1L, "address1", 10), new Room(2L, "address2", 20),
                new Room(3L, "address3", 30), new Room(4L, "address4", 40) };
        teachers = new Teacher[] { new Teacher(), new Teacher(1L, "name1", "surname1"),
                new Teacher(2L, "name2", "surname2"), new Teacher(3L, "name3", "surname3") };
        courses = new Course[] { new Course(), new Course(1L, "name1", "description1"),
                new Course(2L, "name2", "description2"), new Course(3L, "name3", "description3") };
        groups = new Group[] { new Group(), new Group(1L, "name1"), new Group(2L, "name2"), new Group(3L, "name3") };
    }

    @Test
    void testRetrieveTeacherLectures() {
        List<Lecture> expected = Arrays.asList(
                new Lecture(10001L, 1, LocalDate.parse("0001-01-01"), rooms[1], teachers[1], courses[1], groups[1]),
                new Lecture(10002L, 2, LocalDate.parse("0001-01-01"), rooms[2], teachers[1], courses[2], groups[2]),
                new Lecture(10003L, 3, LocalDate.parse("0001-02-01"), rooms[3], teachers[1], courses[3], groups[3]));
        Teacher teacher = teachers[1];
        LocalDate startDate = LocalDate.parse("0001-01-01");
        LocalDate finishDate = LocalDate.parse("0001-02-01");

        when(teacherRep.findById(teacher.getId())).thenReturn(Optional.of(teacher));
        when(lectureRep.findAllByDateBetweenAndTeacher(startDate, finishDate, teacher)).thenReturn(expected);
        assertEquals(expected, lectureServ.retrieveTeacherLectures(teacher.getId(), startDate, finishDate));
        verify(teacherRep).findById(teacher.getId());
        verify(lectureRep).findAllByDateBetweenAndTeacher(startDate, finishDate, teacher);
    }

    @Test
    void testRetrieveGroupLectures() {
        List<Lecture> expected = Arrays.asList(
                new Lecture(10001L, 1, LocalDate.parse("0001-01-01"), rooms[1], teachers[1], courses[1], groups[1]),
                new Lecture(10002L, 2, LocalDate.parse("0001-01-01"), rooms[2], teachers[2], courses[2], groups[1]),
                new Lecture(10003L, 3, LocalDate.parse("0001-02-01"), rooms[3], teachers[3], courses[3], groups[1]));
        Group group = groups[1];
        LocalDate startDate = LocalDate.parse("0001-01-01");
        LocalDate finishDate = LocalDate.parse("0001-02-01");

        when(groupRep.findById(group.getId())).thenReturn(Optional.of(group));
        when(lectureRep.findAllByDateBetweenAndGroup(startDate, finishDate, group)).thenReturn(expected);
        assertEquals(expected, lectureServ.retrieveGroupLectures(group.getId(), startDate, finishDate));
        verify(groupRep).findById(group.getId());
        verify(lectureRep).findAllByDateBetweenAndGroup(startDate, finishDate, group);
    }

    @Test
    void testRetrieveRoomLectures() {
        List<Lecture> expected = Arrays.asList(
                new Lecture(10001L, 1, LocalDate.parse("0001-01-01"), rooms[1], teachers[1], courses[1], groups[1]),
                new Lecture(10002L, 2, LocalDate.parse("0001-01-01"), rooms[1], teachers[2], courses[2], groups[2]),
                new Lecture(10003L, 3, LocalDate.parse("0001-02-01"), rooms[1], teachers[3], courses[3], groups[1]));
        Room room = rooms[1];
        LocalDate startDate = LocalDate.parse("0001-01-01");
        LocalDate finishDate = LocalDate.parse("0001-02-01");

        when(roomRep.findById(room.getId())).thenReturn(Optional.of(room));
        when(lectureRep.findAllByDateBetweenAndRoom(startDate, finishDate, room)).thenReturn(expected);
        assertEquals(expected, lectureServ.retrieveRoomLectures(room.getId(), startDate, finishDate));
        verify(roomRep).findById(room.getId());
        verify(lectureRep).findAllByDateBetweenAndRoom(startDate, finishDate, room);
    }

    @Test
    void testSubstituteTeacher() {
        Lecture start = new Lecture(10001L, 1, LocalDate.parse("0001-01-01"), rooms[1], teachers[1], courses[1],
                groups[1]);
        Lecture expected = new Lecture(10001L, 1, LocalDate.parse("0001-01-01"), rooms[1], teachers[2], courses[1],
                groups[1]);
        Teacher teacher = teachers[2];

        when(teacherRep.findById(teacher.getId())).thenReturn(Optional.of(teacher));
        when(lectureRep.findById(start.getId())).thenReturn(Optional.of(start));
        when(lectureRep.save(expected)).thenReturn(expected);
        assertEquals(expected, lectureServ.substituteTeacher(teacher.getId(), start.getId()));
        verify(teacherRep).findById(teacher.getId());
        verify(lectureRep).findById(start.getId());
        verify(lectureRep).save(expected);
    }

    @ParameterizedTest
    @MethodSource("provideStringsForTestEnter")
    void testEnter(Lecture start, Lecture expected) {
        
        when(lectureRep.save(start)).thenReturn(expected);
        assertEquals(expected, lectureServ.enter(start));
        verify(lectureRep).save(start);
    }

    private static Stream<Arguments> provideStringsForTestEnter() {
        return Stream.of(
                Arguments.of(
                        new Lecture(1, LocalDate.of(0001, 01, 01), rooms[1], teachers[1], courses[1], groups[1]),
                        new Lecture(1L, 1, LocalDate.of(0001, 01, 01), rooms[1], teachers[1], courses[1], groups[1])),
                Arguments.of(
                        new Lecture(1L, 1, LocalDate.of(0001, 01, 01), rooms[1], teachers[1], courses[1], groups[1]),
                        new Lecture(1L, 1, LocalDate.of(0001, 01, 01), rooms[1], teachers[1], courses[1], groups[1])));
    }

    @Test
    void testRemove() {
        when(lectureRep.existsById(10002L)).thenReturn(true);
        lectureServ.remove(10002L);
        verify(lectureRep).existsById(10002L);
        verify(lectureRep).deleteById(10002L);
    }

    @Test
    void testRetrieveAllByGroupIdByTeacherIdByRoomId() {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate startDate = LocalDate.parse("29-12-2022", pattern);
        LocalDate twoDate = LocalDate.parse("30-12-2022", pattern);
        LocalDate fourDate = LocalDate.parse("01-01-2023", pattern);
        LocalDate finishDate = LocalDate.parse("02-01-2023", pattern);
        Room room = rooms[1];
        Group group = groups[2];
        Teacher teacher = teachers[3];

        List<Lecture> exected = Arrays.asList(
                new Lecture(1L, 1, twoDate, rooms[4], teachers[3], courses[3], groups[2]),
                new Lecture(2L, 3, twoDate, rooms[1], teachers[2], courses[2], groups[2]),
                new Lecture(3L, 3, fourDate, rooms[3], teachers[2], courses[2], groups[2]),
                new Lecture(4L, 1, twoDate, rooms[4], teachers[3], courses[3], groups[2]),
                new Lecture(5L, 3, twoDate, rooms[1], teachers[3], courses[1], groups[1]),
                new Lecture(6L, 3, fourDate, rooms[3], teachers[3], courses[2], groups[2]),
                new Lecture(7L, 5, fourDate, rooms[1], teachers[3], courses[1], groups[1]),
                new Lecture(8L, 1, twoDate, rooms[1], teachers[1], courses[2], groups[1]),
                new Lecture(9L, 3, twoDate, rooms[1], teachers[2], courses[2], groups[2]),
                new Lecture(10L, 3, fourDate, rooms[1], teachers[1], courses[2], groups[3]),
                new Lecture(11L, 5, fourDate, rooms[1], teachers[3], courses[1], groups[1])
);
        when(teacherRep.findById(teacher.getId())).thenReturn(Optional.of(teacher));
        when(roomRep.findById(room.getId())).thenReturn(Optional.of(room));
        when(groupRep.findById(group.getId())).thenReturn(Optional.of(group));
        when(lectureRep.findAllByDateBetweenAndGroup(startDate, finishDate, group))
                .thenReturn(Arrays.asList(new Lecture(1L, 1, twoDate, rooms[4], teachers[3], courses[3], groups[2]),
                        new Lecture(2L, 3, twoDate, rooms[1], teachers[2], courses[2], groups[2]),
                        new Lecture(3L, 3, fourDate, rooms[3], teachers[2], courses[2], groups[2]),
                        new Lecture(4L, 1, twoDate, rooms[4], teachers[3], courses[3], groups[2]),
                        new Lecture(6L, 3, fourDate, rooms[3], teachers[3], courses[2], groups[2]),
                        new Lecture(9L, 3, twoDate, rooms[1], teachers[2], courses[2], groups[2])));
        when(lectureRep.findAllByDateBetweenAndTeacher(startDate, finishDate, teacher))
                .thenReturn(Arrays.asList(new Lecture(4L, 1, twoDate, rooms[4], teachers[3], courses[3], groups[2]),
                        new Lecture(5L, 3, twoDate, rooms[1], teachers[3], courses[1], groups[1]),
                        new Lecture(6L, 3, fourDate, rooms[3], teachers[3], courses[2], groups[2]),
                        new Lecture(7L, 5, fourDate, rooms[1], teachers[3], courses[1], groups[1]),
                        new Lecture(1L, 1, twoDate, rooms[4], teachers[3], courses[3], groups[2]),
                        new Lecture(11L, 5, fourDate, rooms[1], teachers[3], courses[1], groups[1])));
        when(lectureRep.findAllByDateBetweenAndRoom(startDate, finishDate, room))
                .thenReturn(Arrays.asList(new Lecture(8L, 1, twoDate, rooms[1], teachers[1], courses[2], groups[1]),
                        new Lecture(9L, 3, twoDate, rooms[1], teachers[2], courses[2], groups[2]),
                        new Lecture(10L, 3, fourDate, rooms[1], teachers[1], courses[2], groups[3]),
                        new Lecture(11L, 5, fourDate, rooms[1], teachers[3], courses[1], groups[1]),
                        new Lecture(2L, 3, twoDate, rooms[1], teachers[2], courses[2], groups[2]),
                        new Lecture(5L, 3, twoDate, rooms[1], teachers[3], courses[1], groups[1])));

        List<Lecture> actual = lectureServ.retrieveAllByGroupIdByTeacherIdByRoomId(group.getId(), teacher.getId(),
                room.getId(), startDate, finishDate);
        Collections.sort(actual, (o1, o2) -> o1.getId().compareTo(o2.getId()));
        
        assertEquals(exected, actual);

        verify(teacherRep).findById(teacher.getId());
        verify(roomRep).findById(room.getId());
        verify(groupRep).findById(group.getId());
        verify(lectureRep).findAllByDateBetweenAndGroup(startDate, finishDate, group);
        verify(lectureRep).findAllByDateBetweenAndTeacher(startDate, finishDate, teacher);
        verify(lectureRep).findAllByDateBetweenAndRoom(startDate, finishDate, room);
    }

}
