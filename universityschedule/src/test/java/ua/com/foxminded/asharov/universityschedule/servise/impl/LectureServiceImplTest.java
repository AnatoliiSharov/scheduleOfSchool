package ua.com.foxminded.asharov.universityschedule.servise.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ua.com.foxminded.asharov.universityschedule.dao.LectureDao;
import ua.com.foxminded.asharov.universityschedule.model.Lecture;
import ua.com.foxminded.asharov.universityschedule.service.LectureService;

@SpringBootTest(classes = { LectureServiceImpl.class })
class LectureServiceImplTest {

    @MockBean
    LectureDao lectureDao;

    @Autowired
    LectureService lectureServ;

    @Test
    void testRetrieveTeacherLectures() {
        List<Lecture> expected = Arrays.asList(
                new Lecture(10001L, 1, LocalDate.parse("0001-01-01"), 10001L, 10001L, 10001L, 10001L),
                new Lecture(10002L, 2, LocalDate.parse("0001-01-01"), 10002L, 10001L, 10002L, 10002L),
                new Lecture(10003L, 3, LocalDate.parse("0001-02-01"), 10003L, 10001L, 10003L, 10003L));
        Long teacherId = 10001L;
        LocalDate startDate = LocalDate.parse("0001-01-01");
        LocalDate finishDate = LocalDate.parse("0001-02-01");

        when(lectureDao.findTeacherSchedule(teacherId, startDate, finishDate)).thenReturn(expected);
        assertEquals(expected, lectureServ.retrieveTeacherLectures(teacherId, startDate, finishDate));
        verify(lectureDao).findTeacherSchedule(teacherId, startDate, finishDate);
    }

    @Test
    void testRetrieveGroupLectures() {
        List<Lecture> expected = Arrays.asList(
                new Lecture(10001L, 1, LocalDate.parse("0001-01-01"), 10001L, 10001L, 10001L, 10001L),
                new Lecture(10002L, 2, LocalDate.parse("0001-01-01"), 10002L, 10002L, 10002L, 10001L),
                new Lecture(10003L, 3, LocalDate.parse("0001-02-01"), 10003L, 10003L, 10003L, 10001L));
        Long groupId = 10001L;
        LocalDate startDate = LocalDate.parse("0001-01-01");
        LocalDate finishDate = LocalDate.parse("0001-02-01");

        when(lectureDao.findTeacherSchedule(groupId, startDate, finishDate)).thenReturn(expected);
        assertEquals(expected, lectureServ.retrieveTeacherLectures(groupId, startDate, finishDate));
        verify(lectureDao).findTeacherSchedule(groupId, startDate, finishDate);
    }

    @Test
    void testRetrieveRoomLectures() {
        List<Lecture> expected = Arrays.asList(
                new Lecture(10001L, 1, LocalDate.parse("0001-01-01"), 10001L, 10001L, 10001L, 10001L),
                new Lecture(10002L, 2, LocalDate.parse("0001-01-01"), 10001L, 10002L, 10002L, 10002L),
                new Lecture(10003L, 3, LocalDate.parse("0001-02-01"), 10001L, 10003L, 10003L, 10001L));
        Long roomId = 10001L;
        LocalDate startDate = LocalDate.parse("0001-01-01");
        LocalDate finishDate = LocalDate.parse("0001-02-01");

        when(lectureDao.findTeacherSchedule(roomId, startDate, finishDate)).thenReturn(expected);
        assertEquals(expected, lectureServ.retrieveTeacherLectures(roomId, startDate, finishDate));
        verify(lectureDao).findTeacherSchedule(roomId, startDate, finishDate);
    }

    @Test
    void testSubstituteTeacher() {
        Lecture start = new Lecture(10001L, 1, LocalDate.parse("0001-01-01"), 10001L, 10001L, 10001L, 10001L);
        Lecture expected = new Lecture(10001L, 1, LocalDate.parse("0001-01-01"), 10001L, 10002L, 10001L, 10001L);
        Long teacherId = 10002L;

        when(lectureDao.save(expected)).thenReturn(expected);
        assertEquals(expected, lectureServ.substituteTeacher(teacherId, start));
        verify(lectureDao).save(expected);
    }

    @ParameterizedTest
    @MethodSource("provideStringsForTestEnter")
    void testEnter(Lecture start, Lecture expected) {

        when(lectureDao.save(start)).thenReturn(expected);
        Lecture actual = lectureServ.enter(start);
        assertEquals(expected.getSerialNumberPerDay(), actual.getSerialNumberPerDay());
        assertEquals(expected.getDate(), actual.getDate());
        assertEquals(expected.getRoomId(), actual.getRoomId());
        assertEquals(expected.getTeacherId(), actual.getTeacherId());
        assertEquals(expected.getGroupId(), actual.getGroupId());
        assertEquals(expected.getCourseId(), actual.getCourseId());
        verify(lectureDao).save(start);
    }

    private static Stream<Arguments> provideStringsForTestEnter() {
        return Stream.of(
                Arguments.of(new Lecture(1L, 1, LocalDate.of(0001, 01, 01), 1L, 2L, 3L, 4L),
                        new Lecture(1L, 1, LocalDate.of(0001, 01, 01), 1L, 2L, 3L, 4L)),
                Arguments.of(new Lecture(1L, 1, LocalDate.of(0001, 01, 01), 1L, 2L, 3L, 4L),
                        new Lecture(1L, 1, LocalDate.of(0001, 01, 01), 1L, 2L, 3L, 4L)));
    }

    @Test
    void testRemove() {
        lectureServ.remove(10002L);
        verify(lectureDao).deleteById(10002L);
    }

    @Test
    void testRetrieveAllByGroupIdByTeacherIdByRoomId() {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate startDate = LocalDate.parse("29-12-2022", pattern);
        LocalDate twoDate = LocalDate.parse("30-12-2022", pattern);
        LocalDate fourDate = LocalDate.parse("01-01-2023", pattern);
        LocalDate finishDate = LocalDate.parse("02-01-2023", pattern);
        Long roomId = 1L;
        Long groupId = 2L;
        Long teacherId = 3L;

        String exected = "{2022-12-30={"
                + "1=["
                + "Lecture [serialNumberPerDay=1, date=2022-12-30, teacherId=1, roomId=1, courseId=2, groupId=1], "
                + "Lecture [serialNumberPerDay=1, date=2022-12-30, teacherId=3, roomId=4, courseId=3, groupId=2]], "
                + "3=["
                + "Lecture [serialNumberPerDay=3, date=2022-12-30, teacherId=2, roomId=1, courseId=2, groupId=2], "
                + "Lecture [serialNumberPerDay=3, date=2022-12-30, teacherId=3, roomId=1, courseId=1, groupId=1]]}, "
                + "2023-01-01={"
                + "3=["
                + "Lecture [serialNumberPerDay=3, date=2023-01-01, teacherId=1, roomId=1, courseId=2, groupId=3], "
                + "Lecture [serialNumberPerDay=3, date=2023-01-01, teacherId=2, roomId=3, courseId=2, groupId=2], "
                + "Lecture [serialNumberPerDay=3, date=2023-01-01, teacherId=3, roomId=3, courseId=2, groupId=2]], "
                + "5=["
                + "Lecture [serialNumberPerDay=5, date=2023-01-01, teacherId=3, roomId=1, courseId=1, groupId=1]]}}";

        when(lectureDao.findGroupSchedule(groupId, startDate, finishDate))
                .thenReturn(Arrays.asList(
                        new Lecture(1, twoDate, 4L, 3L, 3L, 2L),//
                        new Lecture(3, twoDate, 1L, 2L, 2L, 2L), //
                        new Lecture(3, fourDate, 3L, 2L, 2L, 2L)));
        when(lectureDao.findTeacherSchedule(teacherId, startDate, finishDate)).thenReturn(
                Arrays.asList(
                        new Lecture(1, twoDate, 4L, 3L, 3L, 2L), 
                        new Lecture(3, twoDate, 1L, 3L, 1L, 1L),//
                        new Lecture(3, fourDate, 3L, 3L, 2L, 2L), 
                        new Lecture(5, fourDate, 1L, 3L, 1L, 1L)));//
        when(lectureDao.findRoomSchedule(roomId, startDate, finishDate)).thenReturn(
                Arrays.asList(
                        new Lecture(1, twoDate, 1L, 1L, 2L, 1L), 
                        new Lecture(3, twoDate, 1L, 2L, 2L, 2L),
                        new Lecture(3, fourDate, 1L, 1L, 2L, 3L), 
                        new Lecture(5, fourDate, 1L, 3L, 1L, 1L)));

        assertEquals(exected, lectureServ
                .retrieveAllByGroupIdByTeacherIdByRoomId(groupId, teacherId, roomId, startDate, finishDate).toString());

        verify(lectureDao).findGroupSchedule(groupId, startDate, finishDate);
        verify(lectureDao).findTeacherSchedule(teacherId, startDate, finishDate);
        verify(lectureDao).findRoomSchedule(roomId, startDate, finishDate);
    }

}
