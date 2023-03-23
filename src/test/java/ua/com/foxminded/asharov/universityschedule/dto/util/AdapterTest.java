package ua.com.foxminded.asharov.universityschedule.dto.util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ua.com.foxminded.asharov.universityschedule.dto.LectureDto;
import ua.com.foxminded.asharov.universityschedule.entity.Course;
import ua.com.foxminded.asharov.universityschedule.entity.Group;
import ua.com.foxminded.asharov.universityschedule.entity.Lecture;
import ua.com.foxminded.asharov.universityschedule.entity.Room;
import ua.com.foxminded.asharov.universityschedule.entity.Teacher;
import ua.com.foxminded.asharov.universityschedule.service.GroupService;
import ua.com.foxminded.asharov.universityschedule.service.LectureService;
import ua.com.foxminded.asharov.universityschedule.service.RoomService;
import ua.com.foxminded.asharov.universityschedule.service.TeacherService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = { Adapter.class })
class AdapterTest {

    @Autowired
    Adapter adapter;
    @MockBean
    MapperUtil mapperUtil;
    @MockBean
    LectureService lectureServ;
    @MockBean
    GroupService groupServ;
    @MockBean
    TeacherService teacherServ;
    @MockBean
    RoomService roomServ;

    SortedMap<LocalDate, ArrayList<LectureDto>> timetable;
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
                new ArrayList<>(Arrays.asList(new LectureDto(0, LocalDate.parse("30-12-2022", pattern), 1L, 1L, 1L, 1L),
                        new LectureDto(4, LocalDate.parse("30-12-2022", pattern), 4L, 4L, 4L, 4L))));
        timetable.put(LocalDate.parse("31-12-2022", pattern), new ArrayList<>(
                Arrays.asList(new LectureDto(1, LocalDate.parse("31-12-2022", pattern), null, null, null, null))));
        timetable.put(LocalDate.parse("01-01-2023", pattern), new ArrayList<>(
                Arrays.asList(new LectureDto(7, LocalDate.parse("01-01-2023", pattern), 2L, 2L, 2L, 2L))));

        String expected = "[{2022-12-01=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-01, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-01, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-01, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-01, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-01, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-01, roomId=null, teacherId=null, courseId=null, groupId=null)], "
                + "2022-12-02=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-02, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-02, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-02, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-02, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-02, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-02, roomId=null, teacherId=null, courseId=null, groupId=null)], "
                + "2022-12-03=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-03, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-03, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-03, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-03, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-03, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-03, roomId=null, teacherId=null, courseId=null, groupId=null)], "
                + "2022-12-04=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-04, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-04, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-04, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-04, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-04, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-04, roomId=null, teacherId=null, courseId=null, groupId=null)], "
                + "2022-12-05=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-05, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-05, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-05, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-05, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-05, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-05, roomId=null, teacherId=null, courseId=null, groupId=null)], "
                + "2022-12-06=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-06, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-06, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-06, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-06, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-06, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-06, roomId=null, teacherId=null, courseId=null, groupId=null)], "
                + "2022-12-07=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-07, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-07, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-07, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-07, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-07, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-07, roomId=null, teacherId=null, courseId=null, groupId=null)], "
                + "2022-12-08=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-08, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-08, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-08, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-08, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-08, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-08, roomId=null, teacherId=null, courseId=null, groupId=null)], "
                + "2022-12-09=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-09, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-09, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-09, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-09, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-09, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-09, roomId=null, teacherId=null, courseId=null, groupId=null)], "
                + "2022-12-10=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-10, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-10, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-10, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-10, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-10, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-10, roomId=null, teacherId=null, courseId=null, groupId=null)], "
                + "2022-12-11=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-11, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-11, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-11, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-11, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-11, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-11, roomId=null, teacherId=null, courseId=null, groupId=null)], "
                + "2022-12-12=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-12, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-12, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-12, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-12, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-12, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-12, roomId=null, teacherId=null, courseId=null, groupId=null)], "
                + "2022-12-13=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-13, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-13, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-13, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-13, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-13, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-13, roomId=null, teacherId=null, courseId=null, groupId=null)], "
                + "2022-12-14=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-14, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-14, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-14, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-14, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-14, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-14, roomId=null, teacherId=null, courseId=null, groupId=null)], "
                + "2022-12-15=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-15, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-15, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-15, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-15, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-15, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-15, roomId=null, teacherId=null, courseId=null, groupId=null)], "
                + "2022-12-16=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-16, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-16, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-16, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-16, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-16, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-16, roomId=null, teacherId=null, courseId=null, groupId=null)], "
                + "2022-12-17=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-17, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-17, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-17, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-17, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-17, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-17, roomId=null, teacherId=null, courseId=null, groupId=null)], "
                + "2022-12-18=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-18, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-18, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-18, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-18, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-18, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-18, roomId=null, teacherId=null, courseId=null, groupId=null)], "
                + "2022-12-19=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-19, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-19, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-19, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-19, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-19, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-19, roomId=null, teacherId=null, courseId=null, groupId=null)], "
                + "2022-12-20=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-20, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-20, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-20, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-20, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-20, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-20, roomId=null, teacherId=null, courseId=null, groupId=null)], "
                + "2022-12-21=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-21, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-21, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-21, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-21, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-21, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-21, roomId=null, teacherId=null, courseId=null, groupId=null)], "
                + "2022-12-22=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-22, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-22, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-22, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-22, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-22, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-22, roomId=null, teacherId=null, courseId=null, groupId=null)], "
                + "2022-12-23=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-23, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-23, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-23, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-23, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-23, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-23, roomId=null, teacherId=null, courseId=null, groupId=null)], "
                + "2022-12-24=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-24, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-24, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-24, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-24, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-24, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-24, roomId=null, teacherId=null, courseId=null, groupId=null)], "
                + "2022-12-25=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-25, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-25, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-25, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-25, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-25, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-25, roomId=null, teacherId=null, courseId=null, groupId=null)], "
                + "2022-12-26=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-26, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-26, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-26, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-26, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-26, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-26, roomId=null, teacherId=null, courseId=null, groupId=null)], "
                + "2022-12-27=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-27, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-27, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-27, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-27, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-27, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-27, roomId=null, teacherId=null, courseId=null, groupId=null)], "
                + "2022-12-28=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-28, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-28, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-28, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-28, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-28, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-28, roomId=null, teacherId=null, courseId=null, groupId=null)], "
                + "2022-12-29=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-29, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-29, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-29, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-29, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-29, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-29, roomId=null, teacherId=null, courseId=null, groupId=null)], "
                + "2022-12-30=["
                + "LectureDto(id=null, serialNumberPerDay=0, date=2022-12-30, roomId=1, teacherId=1, courseId=1, groupId=1), "
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-30, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-30, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-30, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-30, roomId=4, teacherId=4, courseId=4, groupId=4), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-30, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-30, roomId=null, teacherId=null, courseId=null, groupId=null)], "
                + "2022-12-31=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=3, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=4, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=5, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null)]}, "
                + "" + "{2023-01-01=["
                + "LectureDto(id=null, serialNumberPerDay=1, date=2023-01-01, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-01, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-01, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-01, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-01, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-01, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=7, date=2023-01-01, roomId=2, teacherId=2, courseId=2, groupId=2)], 2023-01-02=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-02, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-02, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-02, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-02, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-02, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-02, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-03=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-03, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-03, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-03, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-03, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-03, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-03, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-04=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-04, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-04, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-04, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-04, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-04, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-04, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-05=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-05, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-05, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-05, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-05, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-05, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-05, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-06=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-06, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-06, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-06, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-06, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-06, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-06, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-07=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-07, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-07, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-07, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-07, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-07, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-07, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-08=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-08, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-08, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-08, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-08, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-08, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-08, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-09=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-09, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-09, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-09, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-09, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-09, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-09, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-10=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-10, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-10, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-10, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-10, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-10, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-10, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-11=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-11, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-11, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-11, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-11, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-11, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-11, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-12=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-12, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-12, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-12, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-12, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-12, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-12, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-13=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-13, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-13, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-13, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-13, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-13, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-13, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-14=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-14, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-14, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-14, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-14, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-14, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-14, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-15=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-15, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-15, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-15, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-15, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-15, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-15, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-16=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-16, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-16, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-16, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-16, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-16, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-16, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-17=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-17, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-17, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-17, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-17, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-17, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-17, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-18=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-18, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-18, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-18, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-18, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-18, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-18, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-19=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-19, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-19, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-19, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-19, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-19, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-19, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-20=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-20, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-20, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-20, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-20, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-20, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-20, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-21=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-21, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-21, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-21, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-21, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-21, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-21, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-22=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-22, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-22, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-22, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-22, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-22, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-22, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-23=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-23, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-23, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-23, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-23, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-23, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-23, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-24=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-24, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-24, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-24, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-24, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-24, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-24, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-25=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-25, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-25, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-25, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-25, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-25, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-25, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-26=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-26, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-26, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-26, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-26, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-26, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-26, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-27=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-27, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-27, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-27, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-27, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-27, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-27, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-28=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-28, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-28, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-28, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-28, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-28, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-28, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-29=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-29, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-29, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-29, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-29, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-29, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-29, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-30=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-30, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-30, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-30, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-30, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-30, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-30, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-31=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-31, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-31, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-31, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-31, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-31, roomId=null, teacherId=null, courseId=null, groupId=null), "
                + "LectureDto(id=null, serialNumberPerDay=6, date=2023-01-31, roomId=null, teacherId=null, courseId=null, groupId=null)]}]";

        assertEquals(expected, adapter.wrapForMonthView(timetable).toString());

    }

    @Test
    void testStuffTimetable() {
        List<LectureDto> LectureDtos = Arrays.asList(
                new LectureDto(6L, 3, LocalDate.parse("30-12-2022", pattern), 4L, 2L, 2L, 2L),
                new LectureDto(4L, 3, LocalDate.parse("01-01-2023", pattern), 3L, 2L, 2L, 2L),
                new LectureDto(20L, 1, LocalDate.parse("30-12-2022", pattern), 4L, 3L, 3L, 2L),
                new LectureDto(14L, 3, LocalDate.parse("02-01-2023", pattern), 3L, 1L, 2L, 2L));
        String expected = "{" + "2022-12-30=["
                + "LectureDto(id=20, serialNumberPerDay=1, date=2022-12-30, roomId=4, teacherId=3, courseId=3, groupId=2), "
                + "LectureDto(id=null, serialNumberPerDay=2, date=2022-12-30, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=6, serialNumberPerDay=3, date=2022-12-30, roomId=4, teacherId=2, courseId=2, groupId=2), LectureDto(id=null, serialNumberPerDay=4, date=2022-12-30, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2022-12-30, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2022-12-30, roomId=null, teacherId=null, courseId=null, groupId=null)], 2022-12-31=[LectureDto(id=null, serialNumberPerDay=1, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-01=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-01, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-01, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=4, serialNumberPerDay=3, date=2023-01-01, roomId=3, teacherId=2, courseId=2, groupId=2), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-01, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-01, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-01, roomId=null, teacherId=null, courseId=null, groupId=null)]}";
        assertEquals(expected, adapter.stuffTimetable(LocalDate.parse("30-12-2022", pattern),
                LocalDate.parse("01-01-2023", pattern), LectureDtos).toString());
    }

    @Test
    void testFillGapsWithBlankLectureDto() {
        timetable.put(LocalDate.parse("30-12-2022", pattern),
                new ArrayList<>(Arrays.asList(new LectureDto(0, LocalDate.parse("30-12-2022", pattern), 1L, 1L, 1L, 1L),
                        new LectureDto(4, LocalDate.parse("30-12-2022", pattern), 4L, 4L, 4L, 4L))));
        timetable.put(LocalDate.parse("31-12-2022", pattern), new ArrayList<>(
                Arrays.asList(new LectureDto(1, LocalDate.parse("31-12-2022", pattern), null, null, null, null))));
        timetable.put(LocalDate.parse("01-01-2023", pattern), new ArrayList<>(
                Arrays.asList(new LectureDto(7, LocalDate.parse("01-01-2023", pattern), 2L, 2L, 2L, 2L))));

        String exected = "{2022-12-30=[LectureDto(id=null, serialNumberPerDay=0, date=2022-12-30, roomId=1, teacherId=1, courseId=1, groupId=1), LectureDto(id=null, serialNumberPerDay=1, date=2022-12-30, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2022-12-30, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2022-12-30, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2022-12-30, roomId=4, teacherId=4, courseId=4, groupId=4), LectureDto(id=null, serialNumberPerDay=5, date=2022-12-30, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2022-12-30, roomId=null, teacherId=null, courseId=null, groupId=null)], 2022-12-31=[LectureDto(id=null, serialNumberPerDay=1, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-01=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-01, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=2, date=2023-01-01, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-01, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=4, date=2023-01-01, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=5, date=2023-01-01, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=6, date=2023-01-01, roomId=null, teacherId=null, courseId=null, groupId=null), LectureDto(id=null, serialNumberPerDay=7, date=2023-01-01, roomId=2, teacherId=2, courseId=2, groupId=2)]}";

        assertEquals(exected, adapter.fillGapsWithBlankLecture(timetable).toString());
    }

    @Test
    void testFillGapsWithBlankDay() {
        timetable.put(LocalDate.parse("30-12-2022", pattern), new ArrayList<>(
                Arrays.asList(new LectureDto(2, LocalDate.parse("30-12-2022", pattern), 1L, 1L, 1L, 1L))));
        timetable.put(LocalDate.parse("01-01-2023", pattern), new ArrayList<>(
                Arrays.asList(new LectureDto(2, LocalDate.parse("01-01-2023", pattern), 2L, 2L, 2L, 2L))));
        String exected = "{2022-12-30=[LectureDto(id=null, serialNumberPerDay=2, date=2022-12-30, roomId=1, teacherId=1, courseId=1, groupId=1)], 2022-12-31=[LectureDto(id=null, serialNumberPerDay=1, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null)], 2023-01-01=[LectureDto(id=null, serialNumberPerDay=2, date=2023-01-01, roomId=2, teacherId=2, courseId=2, groupId=2)], 2023-01-02=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-02, roomId=null, teacherId=null, courseId=null, groupId=null)]}";
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

        String exected = "{2022-12-29={1=[LectureDto(id=null, serialNumberPerDay=1, date=2022-12-29, roomId=null, teacherId=null, courseId=null, groupId=null)], 2=[LectureDto(id=null, serialNumberPerDay=2, date=2022-12-29, roomId=null, teacherId=null, courseId=null, groupId=null)], 3=[LectureDto(id=null, serialNumberPerDay=3, date=2022-12-29, roomId=null, teacherId=null, courseId=null, groupId=null)], 4=[LectureDto(id=null, serialNumberPerDay=4, date=2022-12-29, roomId=null, teacherId=null, courseId=null, groupId=null)], 5=[LectureDto(id=null, serialNumberPerDay=5, date=2022-12-29, roomId=null, teacherId=null, courseId=null, groupId=null)], 6=[LectureDto(id=null, serialNumberPerDay=6, date=2022-12-29, roomId=null, teacherId=null, courseId=null, groupId=null)]}, 2022-12-30={1=[LectureDto(id=null, serialNumberPerDay=1, date=2022-12-30, roomId=null, teacherId=null, courseId=null, groupId=null)], 2=[LectureDto(id=null, serialNumberPerDay=2, date=2022-12-30, roomId=null, teacherId=null, courseId=null, groupId=null)], 3=[LectureDto(id=null, serialNumberPerDay=3, date=2022-12-30, roomId=null, teacherId=null, courseId=null, groupId=null)], 4=[LectureDto(id=null, serialNumberPerDay=4, date=2022-12-30, roomId=null, teacherId=null, courseId=null, groupId=null)], 5=[LectureDto(id=null, serialNumberPerDay=5, date=2022-12-30, roomId=null, teacherId=null, courseId=null, groupId=null)], 6=[LectureDto(id=null, serialNumberPerDay=6, date=2022-12-30, roomId=null, teacherId=null, courseId=null, groupId=null)]}, 2022-12-31={1=[LectureDto(id=null, serialNumberPerDay=1, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null)], 2=[LectureDto(id=null, serialNumberPerDay=2, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null)], 3=[LectureDto(id=null, serialNumberPerDay=3, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null)], 4=[LectureDto(id=null, serialNumberPerDay=4, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null)], 5=[LectureDto(id=null, serialNumberPerDay=5, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null)], 6=[LectureDto(id=null, serialNumberPerDay=6, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null)]}, 2023-01-01={1=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-01, roomId=null, teacherId=null, courseId=null, groupId=null)], 2=[LectureDto(id=null, serialNumberPerDay=2, date=2023-01-01, roomId=null, teacherId=null, courseId=null, groupId=null)], 3=[LectureDto(id=null, serialNumberPerDay=3, date=2023-01-01, roomId=null, teacherId=null, courseId=null, groupId=null)], 4=[LectureDto(id=null, serialNumberPerDay=4, date=2023-01-01, roomId=null, teacherId=null, courseId=null, groupId=null)], 5=[LectureDto(id=null, serialNumberPerDay=5, date=2023-01-01, roomId=null, teacherId=null, courseId=null, groupId=null)], 6=[LectureDto(id=null, serialNumberPerDay=6, date=2023-01-01, roomId=null, teacherId=null, courseId=null, groupId=null)]}}";

        Room[] rooms = { new Room(1L, "address1", 10), new Room(2L, "address2", 20), new Room(3L, "address3", 30),
                new Room(4L, "address4", 40) };
        Teacher[] teachers = { new Teacher(1L, "teacherFirstName1", "teacherLastName1"),
                new Teacher(2L, "teacherFirstName2", "teacherLastName2"),
                new Teacher(3L, "teacherFirstName3", "teacherLastName3") };
        Course[] courses = { new Course(1L, "Course1", "Description1"), new Course(2L, "Course2", "Description2"),
                new Course(3L, "Course3", "Description3") };
        Group[] groups = { new Group(1L, "AA-11"), new Group(2L, "AA-22"), new Group(3L, "AA-33") };

        when(lectureServ.retrieveGroupLectures(groups[1].getId(), startDate, finishDate))
                .thenReturn(Arrays.asList(new Lecture(1, twoDate, rooms[3], teachers[2], courses[2], groups[1]),
                        new Lecture(3, twoDate, rooms[0], teachers[1], courses[1], groups[1]),
                        new Lecture(3, fourDate, rooms[2], teachers[1], courses[1], groups[1])));
        when(lectureServ.retrieveTeacherLectures(teachers[2].getId(), startDate, finishDate))
                .thenReturn(Arrays.asList(new Lecture(1, twoDate, rooms[3], teachers[2], courses[2], groups[1]),
                        new Lecture(3, twoDate, rooms[0], teachers[2], courses[0], groups[0]),
                        new Lecture(3, fourDate, rooms[2], teachers[2], courses[1], groups[1]),
                        new Lecture(5, fourDate, rooms[0], teachers[2], courses[0], groups[0])));
        when(lectureServ.retrieveRoomLectures(rooms[0].getId(), startDate, finishDate))
                .thenReturn(Arrays.asList(new Lecture(1, twoDate, rooms[0], teachers[0], courses[1], groups[0]),
                        new Lecture(3, twoDate, rooms[0], teachers[1], courses[1], groups[1]),
                        new Lecture(3, fourDate, rooms[0], teachers[0], courses[1], groups[2]),
                        new Lecture(5, fourDate, rooms[0], teachers[2], courses[0], groups[0])));

        assertEquals(exected,
                adapter.getTimetable(rooms[0].getId(), groups[1].getId(), teachers[2].getId(), startDate, finishDate)
                        .toString());
    }

    @ParameterizedTest
    @CsvSource(value = { "'group', 'Group(id=1, name=nameGroup)', 1, 0, 0",
            "'teacher','Teacher(id=1, firstName=name, lastName=surname)', 0, 1, 0",
            "'room','Room(id=1, address=address, capacity=10)', 0, 0, 1" })
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
        SortedMap<LocalDate, SortedMap<Integer, List<LectureDto>>> timetable = new TreeMap<>();
        SortedMap<Integer, List<LectureDto>> dayOne = new TreeMap<>();
        SortedMap<Integer, List<LectureDto>> dayTwo = new TreeMap<>();
        SortedMap<Integer, List<LectureDto>> dayThree = new TreeMap<>();

        dayOne.put(1,
                Arrays.asList(new LectureDto(1, sDate, 1L, 1L, 2L, 1L), new LectureDto(1, sDate, 4L, 3L, 3L, 2L)));
        dayOne.put(3,
                Arrays.asList(new LectureDto(3, sDate, 1L, 2L, 2L, 2L), new LectureDto(3, sDate, 1L, 3L, 1L, 1L)));
        dayTwo.put(3, Arrays.asList(new LectureDto(3, fDate, 1L, 1L, 2L, 3L), new LectureDto(3, fDate, 3L, 2L, 2L, 2L),
                new LectureDto(3, fDate, 3L, 3L, 2L, 2L)));
        dayTwo.put(5, Arrays.asList(new LectureDto(5, fDate, 1L, 3L, 1L, 1L)));
        timetable.put(sDate, dayOne);
        timetable.put(fDate, dayTwo);

        SortedMap<LocalDate, SortedMap<Integer, List<LectureDto>>> expected = timetable;
        dayThree.put(1, Arrays.asList(new LectureDto(1, LocalDate.parse("31-12-2022", pattern))));
        expected.put(LocalDate.parse("31-12-2022", pattern), dayThree);

        assertEquals(expected, adapter.fillDayGapByBlank(timetable, sDate, fDate));
    }

    @Test
    void testFillLectureDtosGapByBlank() {
        LocalDate sDate = LocalDate.parse("30-12-2022", pattern);
        LocalDate fDate = LocalDate.parse("01-01-2023", pattern);
        SortedMap<LocalDate, SortedMap<Integer, List<LectureDto>>> timetable = new TreeMap<>();
        SortedMap<Integer, List<LectureDto>> dayOne = new TreeMap<>();
        SortedMap<Integer, List<LectureDto>> dayTwo = new TreeMap<>();
        SortedMap<Integer, List<LectureDto>> dayThree = new TreeMap<>();

        dayOne.put(1,
                Arrays.asList(new LectureDto(1, sDate, 1L, 1L, 2L, 1L), new LectureDto(1, sDate, 4L, 3L, 3L, 2L)));
        dayOne.put(3,
                Arrays.asList(new LectureDto(3, sDate, 1L, 2L, 2L, 2L), new LectureDto(3, sDate, 1L, 3L, 1L, 1L)));
        dayTwo.put(3, Arrays.asList(new LectureDto(3, fDate, 1L, 1L, 2L, 3L), new LectureDto(3, fDate, 3L, 2L, 2L, 2L),
                new LectureDto(3, fDate, 3L, 3L, 2L, 2L)));
        dayTwo.put(5, Arrays.asList(new LectureDto(5, fDate, 1L, 3L, 1L, 1L)));
        dayThree.put(1, Arrays.asList(new LectureDto(1, LocalDate.parse("31-12-2022", pattern))));
        timetable.put(sDate, dayOne);
        timetable.put(fDate, dayTwo);
        timetable.put(LocalDate.parse("31-12-2022", pattern), dayThree);

        String expected = "{2022-12-30={1=[LectureDto(id=null, serialNumberPerDay=1, date=2022-12-30, roomId=1, teacherId=1, courseId=2, groupId=1), LectureDto(id=null, serialNumberPerDay=1, date=2022-12-30, roomId=4, teacherId=3, courseId=3, groupId=2)], 2=[LectureDto(id=null, serialNumberPerDay=2, date=2022-12-30, roomId=null, teacherId=null, courseId=null, groupId=null)], 3=[LectureDto(id=null, serialNumberPerDay=3, date=2022-12-30, roomId=1, teacherId=2, courseId=2, groupId=2), LectureDto(id=null, serialNumberPerDay=3, date=2022-12-30, roomId=1, teacherId=3, courseId=1, groupId=1)], 4=[LectureDto(id=null, serialNumberPerDay=4, date=2022-12-30, roomId=null, teacherId=null, courseId=null, groupId=null)], 5=[LectureDto(id=null, serialNumberPerDay=5, date=2022-12-30, roomId=null, teacherId=null, courseId=null, groupId=null)], 6=[LectureDto(id=null, serialNumberPerDay=6, date=2022-12-30, roomId=null, teacherId=null, courseId=null, groupId=null)]}, 2022-12-31={1=[LectureDto(id=null, serialNumberPerDay=1, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null)], 2=[LectureDto(id=null, serialNumberPerDay=2, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null)], 3=[LectureDto(id=null, serialNumberPerDay=3, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null)], 4=[LectureDto(id=null, serialNumberPerDay=4, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null)], 5=[LectureDto(id=null, serialNumberPerDay=5, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null)], 6=[LectureDto(id=null, serialNumberPerDay=6, date=2022-12-31, roomId=null, teacherId=null, courseId=null, groupId=null)]}, 2023-01-01={1=[LectureDto(id=null, serialNumberPerDay=1, date=2023-01-01, roomId=null, teacherId=null, courseId=null, groupId=null)], 2=[LectureDto(id=null, serialNumberPerDay=2, date=2023-01-01, roomId=null, teacherId=null, courseId=null, groupId=null)], 3=[LectureDto(id=null, serialNumberPerDay=3, date=2023-01-01, roomId=1, teacherId=1, courseId=2, groupId=3), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-01, roomId=3, teacherId=2, courseId=2, groupId=2), LectureDto(id=null, serialNumberPerDay=3, date=2023-01-01, roomId=3, teacherId=3, courseId=2, groupId=2)], 4=[LectureDto(id=null, serialNumberPerDay=4, date=2023-01-01, roomId=null, teacherId=null, courseId=null, groupId=null)], 5=[LectureDto(id=null, serialNumberPerDay=5, date=2023-01-01, roomId=1, teacherId=3, courseId=1, groupId=1)], 6=[LectureDto(id=null, serialNumberPerDay=6, date=2023-01-01, roomId=null, teacherId=null, courseId=null, groupId=null)]}}";

        assertEquals(expected, adapter.fillLecturesGapByBlank(timetable).toString());
    }

}
