package ua.com.foxminded.asharov.universityschedule.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ua.com.foxminded.asharov.universityschedule.service.GroupService;
import ua.com.foxminded.asharov.universityschedule.service.LectureService;
import ua.com.foxminded.asharov.universityschedule.service.RoomService;
import ua.com.foxminded.asharov.universityschedule.service.TeacherService;

@SpringBootTest()
class AdapterTest {

    @Autowired
    Adapter adapter;
    @MockBean
    LectureService lectureServ;
    @MockBean
    GroupService groupServ;
    @MockBean
    TeacherService teacherServ;
    @MockBean
    RoomService roomServ;

    SortedMap<LocalDate, ArrayList<Lecture>> timetable;
    DateTimeFormatter pattern;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {

    }

    @BeforeEach
    void setUp() throws Exception {
        timetable = new TreeMap<>();
        pattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    @Test
    void testWrapForMonthView() {
        timetable.put(LocalDate.parse("30-12-2022", pattern),
                new ArrayList<>(Arrays.asList(new Lecture(0, LocalDate.parse("30-12-2022", pattern), 1L, 1L, 1L, 1L),
                        new Lecture(4, LocalDate.parse("30-12-2022", pattern), 4L, 4L, 4L, 4L))));
        timetable.put(LocalDate.parse("31-12-2022", pattern), new ArrayList<>(
                Arrays.asList(new Lecture(1, LocalDate.parse("31-12-2022", pattern), null, null, null, null))));
        timetable.put(LocalDate.parse("01-01-2023", pattern),
                new ArrayList<>(Arrays.asList(new Lecture(7, LocalDate.parse("01-01-2023", pattern), 2L, 2L, 2L, 2L))));

        String expected = "[{2022-12-01=[Lecture [serialNumberPerDay=1, date=2022-12-01, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-01, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-01, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-01, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2022-12-01, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-01, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-02=[Lecture [serialNumberPerDay=1, date=2022-12-02, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-02, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-02, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-02, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2022-12-02, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-02, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-03=[Lecture [serialNumberPerDay=1, date=2022-12-03, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-03, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-03, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-03, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2022-12-03, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-03, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-04=[Lecture [serialNumberPerDay=1, date=2022-12-04, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-04, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-04, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-04, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2022-12-04, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-04, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-05=[Lecture [serialNumberPerDay=1, date=2022-12-05, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-05, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-05, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-05, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2022-12-05, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-05, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-06=[Lecture [serialNumberPerDay=1, date=2022-12-06, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-06, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-06, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-06, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2022-12-06, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-06, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-07=[Lecture [serialNumberPerDay=1, date=2022-12-07, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-07, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-07, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-07, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2022-12-07, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-07, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-08=[Lecture [serialNumberPerDay=1, date=2022-12-08, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-08, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-08, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-08, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2022-12-08, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-08, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-09=[Lecture [serialNumberPerDay=1, date=2022-12-09, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-09, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-09, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-09, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2022-12-09, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-09, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-10=[Lecture [serialNumberPerDay=1, date=2022-12-10, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-10, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-10, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-10, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2022-12-10, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-10, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-11=[Lecture [serialNumberPerDay=1, date=2022-12-11, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-11, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-11, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-11, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2022-12-11, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-11, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-12=[Lecture [serialNumberPerDay=1, date=2022-12-12, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-12, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-12, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-12, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2022-12-12, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-12, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-13=[Lecture [serialNumberPerDay=1, date=2022-12-13, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-13, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-13, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-13, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2022-12-13, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-13, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-14=[Lecture [serialNumberPerDay=1, date=2022-12-14, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-14, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-14, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-14, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2022-12-14, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-14, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-15=[Lecture [serialNumberPerDay=1, date=2022-12-15, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-15, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-15, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-15, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2022-12-15, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-15, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-16=[Lecture [serialNumberPerDay=1, date=2022-12-16, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-16, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-16, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-16, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2022-12-16, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-16, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-17=[Lecture [serialNumberPerDay=1, date=2022-12-17, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-17, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-17, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-17, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2022-12-17, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-17, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-18=[Lecture [serialNumberPerDay=1, date=2022-12-18, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-18, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-18, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-18, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2022-12-18, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-18, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-19=[Lecture [serialNumberPerDay=1, date=2022-12-19, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-19, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-19, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-19, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2022-12-19, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-19, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-20=[Lecture [serialNumberPerDay=1, date=2022-12-20, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-20, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-20, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-20, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2022-12-20, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-20, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-21=[Lecture [serialNumberPerDay=1, date=2022-12-21, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-21, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-21, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-21, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2022-12-21, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-21, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-22=[Lecture [serialNumberPerDay=1, date=2022-12-22, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-22, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-22, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-22, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2022-12-22, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-22, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-23=[Lecture [serialNumberPerDay=1, date=2022-12-23, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-23, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-23, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-23, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2022-12-23, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-23, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-24=[Lecture [serialNumberPerDay=1, date=2022-12-24, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-24, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-24, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-24, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2022-12-24, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-24, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-25=[Lecture [serialNumberPerDay=1, date=2022-12-25, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-25, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-25, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-25, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2022-12-25, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-25, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-26=[Lecture [serialNumberPerDay=1, date=2022-12-26, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-26, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-26, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-26, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2022-12-26, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-26, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-27=[Lecture [serialNumberPerDay=1, date=2022-12-27, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-27, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-27, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-27, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2022-12-27, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-27, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-28=[Lecture [serialNumberPerDay=1, date=2022-12-28, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-28, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-28, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-28, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2022-12-28, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-28, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-29=[Lecture [serialNumberPerDay=1, date=2022-12-29, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-29, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-29, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-29, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2022-12-29, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-29, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-30=[Lecture [serialNumberPerDay=0, date=2022-12-30, teacherId=1, roomId=1, courseId=1, groupId=1], Lecture [serialNumberPerDay=1, date=2022-12-30, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-30, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-30, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-30, teacherId=4, roomId=4, courseId=4, groupId=4], Lecture [serialNumberPerDay=5, date=2022-12-30, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-30, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-31=[Lecture [serialNumberPerDay=1, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null]]}, "
                + "{2023-01-01=[Lecture [serialNumberPerDay=1, date=2023-01-01, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-01, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-01, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-01, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-01, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-01, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=7, date=2023-01-01, teacherId=2, roomId=2, courseId=2, groupId=2]], "
                + "2023-01-02=[Lecture [serialNumberPerDay=1, date=2023-01-02, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-02, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-02, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-02, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-02, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-02, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-03=[Lecture [serialNumberPerDay=1, date=2023-01-03, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-03, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-03, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-03, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-03, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-03, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-04=[Lecture [serialNumberPerDay=1, date=2023-01-04, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-04, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-04, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-04, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-04, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-04, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-05=[Lecture [serialNumberPerDay=1, date=2023-01-05, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-05, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-05, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-05, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-05, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-05, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-06=[Lecture [serialNumberPerDay=1, date=2023-01-06, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-06, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-06, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-06, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-06, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-06, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-07=[Lecture [serialNumberPerDay=1, date=2023-01-07, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-07, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-07, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-07, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-07, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-07, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-08=[Lecture [serialNumberPerDay=1, date=2023-01-08, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-08, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-08, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-08, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-08, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-08, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-09=[Lecture [serialNumberPerDay=1, date=2023-01-09, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-09, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-09, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-09, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-09, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-09, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-10=[Lecture [serialNumberPerDay=1, date=2023-01-10, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-10, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-10, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-10, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-10, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-10, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-11=[Lecture [serialNumberPerDay=1, date=2023-01-11, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-11, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-11, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-11, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-11, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-11, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-12=[Lecture [serialNumberPerDay=1, date=2023-01-12, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-12, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-12, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-12, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-12, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-12, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-13=[Lecture [serialNumberPerDay=1, date=2023-01-13, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-13, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-13, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-13, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-13, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-13, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-14=[Lecture [serialNumberPerDay=1, date=2023-01-14, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-14, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-14, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-14, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-14, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-14, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-15=[Lecture [serialNumberPerDay=1, date=2023-01-15, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-15, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-15, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-15, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-15, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-15, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-16=[Lecture [serialNumberPerDay=1, date=2023-01-16, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-16, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-16, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-16, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-16, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-16, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-17=[Lecture [serialNumberPerDay=1, date=2023-01-17, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-17, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-17, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-17, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-17, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-17, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-18=[Lecture [serialNumberPerDay=1, date=2023-01-18, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-18, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-18, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-18, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-18, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-18, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-19=[Lecture [serialNumberPerDay=1, date=2023-01-19, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-19, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-19, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-19, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-19, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-19, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-20=[Lecture [serialNumberPerDay=1, date=2023-01-20, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-20, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-20, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-20, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-20, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-20, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-21=[Lecture [serialNumberPerDay=1, date=2023-01-21, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-21, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-21, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-21, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-21, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-21, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-22=[Lecture [serialNumberPerDay=1, date=2023-01-22, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-22, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-22, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-22, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-22, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-22, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-23=[Lecture [serialNumberPerDay=1, date=2023-01-23, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-23, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-23, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-23, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-23, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-23, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-24=[Lecture [serialNumberPerDay=1, date=2023-01-24, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-24, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-24, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-24, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-24, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-24, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-25=[Lecture [serialNumberPerDay=1, date=2023-01-25, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-25, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-25, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-25, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-25, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-25, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-26=[Lecture [serialNumberPerDay=1, date=2023-01-26, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-26, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-26, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-26, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-26, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-26, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-27=[Lecture [serialNumberPerDay=1, date=2023-01-27, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-27, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-27, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-27, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-27, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-27, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-28=[Lecture [serialNumberPerDay=1, date=2023-01-28, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-28, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-28, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-28, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-28, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-28, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-29=[Lecture [serialNumberPerDay=1, date=2023-01-29, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-29, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-29, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-29, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-29, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-29, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-30=[Lecture [serialNumberPerDay=1, date=2023-01-30, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-30, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-30, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-30, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-30, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-30, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-31=[Lecture [serialNumberPerDay=1, date=2023-01-31, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=2, date=2023-01-31, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=3, date=2023-01-31, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=4, date=2023-01-31, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=5, date=2023-01-31, teacherId=null, roomId=null, courseId=null, groupId=null], Lecture [serialNumberPerDay=6, date=2023-01-31, teacherId=null, roomId=null, courseId=null, groupId=null]]}]";

        assertEquals(expected, adapter.wrapForMonthView(timetable).toString());

    }

    @Test
    void testStuffTimetable() {
        List<Lecture> lectures = Arrays.asList(
                new Lecture(6L, 3, LocalDate.parse("30-12-2022", pattern), 4L, 2L, 2L, 2L),
                new Lecture(4L, 3, LocalDate.parse("01-01-2023", pattern), 3L, 2L, 2L, 2L),
                new Lecture(20L, 1, LocalDate.parse("30-12-2022", pattern), 4L, 3L, 3L, 2L),
                new Lecture(14L, 3, LocalDate.parse("02-01-2023", pattern), 3L, 1L, 2L, 2L));
        String expected = "{2022-12-30=["
                + "Lecture [serialNumberPerDay=1, date=2022-12-30, teacherId=3, roomId=4, courseId=3, groupId=2], "
                + "Lecture [serialNumberPerDay=2, date=2022-12-30, teacherId=null, roomId=null, courseId=null, groupId=null], "
                + "Lecture [serialNumberPerDay=3, date=2022-12-30, teacherId=2, roomId=4, courseId=2, groupId=2], "
                + "Lecture [serialNumberPerDay=4, date=2022-12-30, teacherId=null, roomId=null, courseId=null, groupId=null], "
                + "Lecture [serialNumberPerDay=5, date=2022-12-30, teacherId=null, roomId=null, courseId=null, groupId=null], "
                + "Lecture [serialNumberPerDay=6, date=2022-12-30, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-31=["
                + "Lecture [serialNumberPerDay=1, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null], "
                + "Lecture [serialNumberPerDay=2, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null], "
                + "Lecture [serialNumberPerDay=3, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null], "
                + "Lecture [serialNumberPerDay=4, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null], "
                + "Lecture [serialNumberPerDay=5, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null], "
                + "Lecture [serialNumberPerDay=6, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-01=["
                + "Lecture [serialNumberPerDay=1, date=2023-01-01, teacherId=null, roomId=null, courseId=null, groupId=null], "
                + "Lecture [serialNumberPerDay=2, date=2023-01-01, teacherId=null, roomId=null, courseId=null, groupId=null], "
                + "Lecture [serialNumberPerDay=3, date=2023-01-01, teacherId=2, roomId=3, courseId=2, groupId=2], "
                + "Lecture [serialNumberPerDay=4, date=2023-01-01, teacherId=null, roomId=null, courseId=null, groupId=null], "
                + "Lecture [serialNumberPerDay=5, date=2023-01-01, teacherId=null, roomId=null, courseId=null, groupId=null], "
                + "Lecture [serialNumberPerDay=6, date=2023-01-01, teacherId=null, roomId=null, courseId=null, groupId=null]]}";
        assertEquals(expected, adapter.stuffTimetable(LocalDate.parse("30-12-2022", pattern),
                LocalDate.parse("01-01-2023", pattern), lectures).toString());
    }

    @Test
    void testFillGapsWithBlankLecture() {
        timetable.put(LocalDate.parse("30-12-2022", pattern),
                new ArrayList<>(Arrays.asList(new Lecture(0, LocalDate.parse("30-12-2022", pattern), 1L, 1L, 1L, 1L),
                        new Lecture(4, LocalDate.parse("30-12-2022", pattern), 4L, 4L, 4L, 4L))));
        timetable.put(LocalDate.parse("31-12-2022", pattern), new ArrayList<>(
                Arrays.asList(new Lecture(1, LocalDate.parse("31-12-2022", pattern), null, null, null, null))));
        timetable.put(LocalDate.parse("01-01-2023", pattern),
                new ArrayList<>(Arrays.asList(new Lecture(7, LocalDate.parse("01-01-2023", pattern), 2L, 2L, 2L, 2L))));

        String exected = "{2022-12-30=["
                + "Lecture [serialNumberPerDay=0, date=2022-12-30, teacherId=1, roomId=1, courseId=1, groupId=1], "
                + "Lecture [serialNumberPerDay=1, date=2022-12-30, teacherId=null, roomId=null, courseId=null, groupId=null], "
                + "Lecture [serialNumberPerDay=2, date=2022-12-30, teacherId=null, roomId=null, courseId=null, groupId=null], "
                + "Lecture [serialNumberPerDay=3, date=2022-12-30, teacherId=null, roomId=null, courseId=null, groupId=null], "
                + "Lecture [serialNumberPerDay=4, date=2022-12-30, teacherId=4, roomId=4, courseId=4, groupId=4], "
                + "Lecture [serialNumberPerDay=5, date=2022-12-30, teacherId=null, roomId=null, courseId=null, groupId=null], "
                + "Lecture [serialNumberPerDay=6, date=2022-12-30, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2022-12-31=["
                + "Lecture [serialNumberPerDay=1, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null], "
                + "Lecture [serialNumberPerDay=2, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null], "
                + "Lecture [serialNumberPerDay=3, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null], "
                + "Lecture [serialNumberPerDay=4, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null], "
                + "Lecture [serialNumberPerDay=5, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null], "
                + "Lecture [serialNumberPerDay=6, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-01=["
                + "Lecture [serialNumberPerDay=1, date=2023-01-01, teacherId=null, roomId=null, courseId=null, groupId=null], "
                + "Lecture [serialNumberPerDay=2, date=2023-01-01, teacherId=null, roomId=null, courseId=null, groupId=null], "
                + "Lecture [serialNumberPerDay=3, date=2023-01-01, teacherId=null, roomId=null, courseId=null, groupId=null], "
                + "Lecture [serialNumberPerDay=4, date=2023-01-01, teacherId=null, roomId=null, courseId=null, groupId=null], "
                + "Lecture [serialNumberPerDay=5, date=2023-01-01, teacherId=null, roomId=null, courseId=null, groupId=null], "
                + "Lecture [serialNumberPerDay=6, date=2023-01-01, teacherId=null, roomId=null, courseId=null, groupId=null], "
                + "Lecture [serialNumberPerDay=7, date=2023-01-01, teacherId=2, roomId=2, courseId=2, groupId=2]]}";

        assertEquals(exected, adapter.fillGapsWithBlankLecture(timetable).toString());
    }

    @Test
    void testFillGapsWithBlankDay() {
        timetable.put(LocalDate.parse("30-12-2022", pattern),
                new ArrayList<>(Arrays.asList(new Lecture(2, LocalDate.parse("30-12-2022", pattern), 1L, 1L, 1L, 1L))));
        timetable.put(LocalDate.parse("01-01-2023", pattern),
                new ArrayList<>(Arrays.asList(new Lecture(2, LocalDate.parse("01-01-2023", pattern), 2L, 2L, 2L, 2L))));
        String exected = "{2022-12-30=[Lecture [serialNumberPerDay=2, date=2022-12-30, teacherId=1, roomId=1, courseId=1, groupId=1]], "
                + "2022-12-31=[Lecture [serialNumberPerDay=1, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2023-01-01=[Lecture [serialNumberPerDay=2, date=2023-01-01, teacherId=2, roomId=2, courseId=2, groupId=2]], "
                + "2023-01-02=[Lecture [serialNumberPerDay=1, date=2023-01-02, teacherId=null, roomId=null, courseId=null, groupId=null]]}";
        assertEquals(exected, adapter.fillGapsWithBlankDay(LocalDate.parse("30-12-2022", pattern),
                LocalDate.parse("02-01-2023", pattern), timetable).toString());
    }

    @Test
    void testStretchMonthTimeline() {
        String expected = "[[2022-12-01, 2022-12-02, 2022-12-03, 2022-12-04, 2022-12-05, 2022-12-06, 2022-12-07, 2022-12-08, 2022-12-09, 2022-12-10, 2022-12-11, 2022-12-12, 2022-12-13, 2022-12-14, 2022-12-15, 2022-12-16, 2022-12-17, 2022-12-18, 2022-12-19, 2022-12-20, 2022-12-21, 2022-12-22, 2022-12-23, 2022-12-24, 2022-12-25, 2022-12-26, 2022-12-27, 2022-12-28, 2022-12-29, 2022-12-30, 2022-12-31], [2023-01-01, 2023-01-02, 2023-01-03, 2023-01-04, 2023-01-05, 2023-01-06, 2023-01-07, 2023-01-08, 2023-01-09, 2023-01-10, 2023-01-11, 2023-01-12, 2023-01-13, 2023-01-14, 2023-01-15, 2023-01-16, 2023-01-17, 2023-01-18, 2023-01-19, 2023-01-20, 2023-01-21, 2023-01-22, 2023-01-23, 2023-01-24, 2023-01-25, 2023-01-26, 2023-01-27, 2023-01-28, 2023-01-29, 2023-01-30, 2023-01-31]]";

        assertEquals(expected, adapter
                .stretchMonthTimeline(LocalDate.parse("30-12-2022", pattern), LocalDate.parse("01-01-2023", pattern))
                .toString());
    }

    @Test
    void testGetTimetable() {
        LocalDate startDate = LocalDate.parse("29-12-2022", pattern);
        LocalDate twoDate = LocalDate.parse("30-12-2022", pattern);
        LocalDate fourDate = LocalDate.parse("01-01-2023", pattern);
        LocalDate finishDate = LocalDate.parse("02-01-2023", pattern);
        Long roomId = 1L;
        Long groupId = 2L;
        Long teacherId = 3L;
        
        String exected = "{2022-12-29={"
                + "1=[Lecture [serialNumberPerDay=1, date=2022-12-29, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2=[Lecture [serialNumberPerDay=2, date=2022-12-29, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "3=[Lecture [serialNumberPerDay=3, date=2022-12-29, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "4=[Lecture [serialNumberPerDay=4, date=2022-12-29, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "5=[Lecture [serialNumberPerDay=5, date=2022-12-29, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "6=[Lecture [serialNumberPerDay=6, date=2022-12-29, teacherId=null, roomId=null, courseId=null, groupId=null]]}, "
                + "2022-12-30={"
                + "1=[Lecture [serialNumberPerDay=1, date=2022-12-30, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2=[Lecture [serialNumberPerDay=2, date=2022-12-30, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "3=[Lecture [serialNumberPerDay=3, date=2022-12-30, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "4=[Lecture [serialNumberPerDay=4, date=2022-12-30, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "5=[Lecture [serialNumberPerDay=5, date=2022-12-30, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "6=[Lecture [serialNumberPerDay=6, date=2022-12-30, teacherId=null, roomId=null, courseId=null, groupId=null]]}, "
                + "2022-12-31={"
                + "1=[Lecture [serialNumberPerDay=1, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2=[Lecture [serialNumberPerDay=2, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "3=[Lecture [serialNumberPerDay=3, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "4=[Lecture [serialNumberPerDay=4, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "5=[Lecture [serialNumberPerDay=5, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "6=[Lecture [serialNumberPerDay=6, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null]]}, "
                + "2023-01-01={"
                + "1=[Lecture [serialNumberPerDay=1, date=2023-01-01, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2=[Lecture [serialNumberPerDay=2, date=2023-01-01, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "3=[Lecture [serialNumberPerDay=3, date=2023-01-01, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "4=[Lecture [serialNumberPerDay=4, date=2023-01-01, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "5=[Lecture [serialNumberPerDay=5, date=2023-01-01, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "6=[Lecture [serialNumberPerDay=6, date=2023-01-01, teacherId=null, roomId=null, courseId=null, groupId=null]]}}";

        when(lectureServ.retrieveGroupLectures(groupId, startDate, finishDate))
                .thenReturn(Arrays.asList(
                        new Lecture(1, twoDate, 4L, 3L, 3L, 2L), 
                        new Lecture(3, twoDate, 1L, 2L, 2L, 2L),
                        new Lecture(3, fourDate, 3L, 2L, 2L, 2L)));
        when(lectureServ.retrieveTeacherLectures(teacherId, startDate, finishDate))
                .thenReturn(Arrays.asList(
                        new Lecture(1, twoDate, 4L, 3L, 3L, 2L), 
                        new Lecture(3, twoDate, 1L, 3L, 1L, 1L),
                        new Lecture(3, fourDate, 3L, 3L, 2L, 2L), 
                        new Lecture(5, fourDate, 1L, 3L, 1L, 1L)));
        when(lectureServ.retrieveRoomLectures(roomId, startDate, finishDate))
                .thenReturn(Arrays.asList(
                        new Lecture(1, twoDate, 1L, 1L, 2L, 1L), 
                        new Lecture(3, twoDate, 1L, 2L, 2L, 2L),
                        new Lecture(3, fourDate, 1L, 1L, 2L, 3L), 
                        new Lecture(5, fourDate, 1L, 3L, 1L, 1L)));

        assertEquals(exected, adapter.getTimetable(roomId, groupId, teacherId, startDate, finishDate).toString());
    }

    @ParameterizedTest
    @CsvSource(value = { "'group', 'Group [name=nameGroup, id=1]', 1, 0, 0",
            "'teacher','Teacher [firstName=name, lastName=surname, Id=1]', 0, 1, 0",
            "'room','Room [address=address, capacity=10]', 0, 0, 1" })
    void testGetOwnerByIdByName(String ownername, String expected, int groupVerify, int teacherVerify, int roomVerify) {
        when(groupServ.retrieveById(1L)).thenReturn(new Group(1L, "nameGroup"));
        when(teacherServ.retrieveById(1L)).thenReturn(new Teacher(1L, "name", "surname"));
        when(roomServ.retrieveById(1L)).thenReturn(new Room(1L, "address", 10));

        assertEquals(expected, adapter.getOwnerByIdByName(1L, ownername).get().toString());
        verify(groupServ, times(groupVerify)).retrieveById(1L); 
        verify(teacherServ, times(teacherVerify)).retrieveById(1L); 
        verify(roomServ, times(roomVerify)).retrieveById(1L);
    }

    @Test
    void fillDayGapByBlank() {
        LocalDate sDate = LocalDate.parse("30-12-2022", pattern);
        LocalDate fDate = LocalDate.parse("01-01-2023", pattern);
        SortedMap<LocalDate, SortedMap<Integer, List<Lecture>>> timetable = new TreeMap<>();
        SortedMap<Integer, List<Lecture>> dayOne = new TreeMap<>();
        SortedMap<Integer, List<Lecture>> dayTwo = new TreeMap<>();
        SortedMap<Integer, List<Lecture>> dayThree = new TreeMap<>();

        dayOne.put(1, Arrays.asList(new Lecture(1, sDate, 1L, 1L, 2L, 1L), new Lecture(1, sDate, 4L, 3L, 3L, 2L)));
        dayOne.put(3, Arrays.asList(new Lecture(3, sDate, 1L, 2L, 2L, 2L), new Lecture(3, sDate, 1L, 3L, 1L, 1L)));
        dayTwo.put(3, Arrays.asList(new Lecture(3, fDate, 1L, 1L, 2L, 3L), new Lecture(3, fDate, 3L, 2L, 2L, 2L),
                new Lecture(3, fDate, 3L, 3L, 2L, 2L)));
        dayTwo.put(5, Arrays.asList(new Lecture(5, fDate, 1L, 3L, 1L, 1L)));
        timetable.put(sDate, dayOne);
        timetable.put(fDate, dayTwo);

        SortedMap<LocalDate, SortedMap<Integer, List<Lecture>>> expected = timetable;
        dayThree.put(1, Arrays.asList(new Lecture(1, LocalDate.parse("31-12-2022", pattern))));
        expected.put(LocalDate.parse("31-12-2022", pattern), dayThree);

        assertEquals(expected, adapter.fillDayGapByBlank(timetable, sDate, fDate));
    }
    
    @Test
    void testFillLecturesGapByBlank() {
        LocalDate sDate = LocalDate.parse("30-12-2022", pattern);
        LocalDate fDate = LocalDate.parse("01-01-2023", pattern);
        SortedMap<LocalDate, SortedMap<Integer, List<Lecture>>> timetable = new TreeMap<>();
        SortedMap<Integer, List<Lecture>> dayOne = new TreeMap<>();
        SortedMap<Integer, List<Lecture>> dayTwo = new TreeMap<>();
        SortedMap<Integer, List<Lecture>> dayThree = new TreeMap<>();

        dayOne.put(1, Arrays.asList(new Lecture(1, sDate, 1L, 1L, 2L, 1L), new Lecture(1, sDate, 4L, 3L, 3L, 2L)));
        dayOne.put(3, Arrays.asList(new Lecture(3, sDate, 1L, 2L, 2L, 2L), new Lecture(3, sDate, 1L, 3L, 1L, 1L)));
        dayTwo.put(3, Arrays.asList(new Lecture(3, fDate, 1L, 1L, 2L, 3L), new Lecture(3, fDate, 3L, 2L, 2L, 2L),
                new Lecture(3, fDate, 3L, 3L, 2L, 2L)));
        dayTwo.put(5, Arrays.asList(new Lecture(5, fDate, 1L, 3L, 1L, 1L)));
        dayThree.put(1, Arrays.asList(new Lecture(1, LocalDate.parse("31-12-2022", pattern))));
        timetable.put(sDate, dayOne);
        timetable.put(fDate, dayTwo);
        timetable.put(LocalDate.parse("31-12-2022", pattern), dayThree);

        String expected = "{"
                + "2022-12-30={"
                + "1=["
                + "Lecture [serialNumberPerDay=1, date=2022-12-30, teacherId=1, roomId=1, courseId=2, groupId=1], "
                + "Lecture [serialNumberPerDay=1, date=2022-12-30, teacherId=3, roomId=4, courseId=3, groupId=2]], "
                + "2=[Lecture [serialNumberPerDay=2, date=2022-12-30, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "3=["
                + "Lecture [serialNumberPerDay=3, date=2022-12-30, teacherId=2, roomId=1, courseId=2, groupId=2], "
                + "Lecture [serialNumberPerDay=3, date=2022-12-30, teacherId=3, roomId=1, courseId=1, groupId=1]], "
                + "4=[Lecture [serialNumberPerDay=4, date=2022-12-30, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "5=[Lecture [serialNumberPerDay=5, date=2022-12-30, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "6=[Lecture [serialNumberPerDay=6, date=2022-12-30, teacherId=null, roomId=null, courseId=null, groupId=null]]}, "
                + "2022-12-31={"
                + "1=[Lecture [serialNumberPerDay=1, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2=[Lecture [serialNumberPerDay=2, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "3=[Lecture [serialNumberPerDay=3, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "4=[Lecture [serialNumberPerDay=4, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "5=[Lecture [serialNumberPerDay=5, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "6=[Lecture [serialNumberPerDay=6, date=2022-12-31, teacherId=null, roomId=null, courseId=null, groupId=null]]}, "
                + "2023-01-01={"
                + "1=[Lecture [serialNumberPerDay=1, date=2023-01-01, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "2=[Lecture [serialNumberPerDay=2, date=2023-01-01, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "3=["
                + "Lecture [serialNumberPerDay=3, date=2023-01-01, teacherId=1, roomId=1, courseId=2, groupId=3], "
                + "Lecture [serialNumberPerDay=3, date=2023-01-01, teacherId=2, roomId=3, courseId=2, groupId=2], "
                + "Lecture [serialNumberPerDay=3, date=2023-01-01, teacherId=3, roomId=3, courseId=2, groupId=2]], "
                + "4=[Lecture [serialNumberPerDay=4, date=2023-01-01, teacherId=null, roomId=null, courseId=null, groupId=null]], "
                + "5=[Lecture [serialNumberPerDay=5, date=2023-01-01, teacherId=3, roomId=1, courseId=1, groupId=1]], "
                + "6=[Lecture [serialNumberPerDay=6, date=2023-01-01, teacherId=null, roomId=null, courseId=null, groupId=null]]}}";
        
        assertEquals(expected, adapter.fillLecturesGapByBlank(timetable).toString());
    }

}
