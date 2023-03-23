package ua.com.foxminded.asharov.universityschedule.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import ua.com.foxminded.asharov.universityschedule.dto.CourseDto;
import ua.com.foxminded.asharov.universityschedule.dto.GroupDto;
import ua.com.foxminded.asharov.universityschedule.dto.LectureDto;
import ua.com.foxminded.asharov.universityschedule.dto.RoomDto;
import ua.com.foxminded.asharov.universityschedule.dto.TeacherDto;
import ua.com.foxminded.asharov.universityschedule.dto.util.Adapter;
import ua.com.foxminded.asharov.universityschedule.dto.util.MapperUtil;
import ua.com.foxminded.asharov.universityschedule.entity.Course;
import ua.com.foxminded.asharov.universityschedule.entity.Group;
import ua.com.foxminded.asharov.universityschedule.entity.Lecture;
import ua.com.foxminded.asharov.universityschedule.entity.Room;
import ua.com.foxminded.asharov.universityschedule.entity.Teacher;
import ua.com.foxminded.asharov.universityschedule.exception.GlobalExceptionHandler;
import ua.com.foxminded.asharov.universityschedule.exception.UniversityException;
import ua.com.foxminded.asharov.universityschedule.secur.conf.SecurityConfigTest;
import ua.com.foxminded.asharov.universityschedule.service.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = { LecturesController.class, GlobalExceptionHandler.class })
@Import(SecurityConfigTest.class)
class LecturesControllerTest {

    @MockBean
    LectureService lectureServ;
    @MockBean
    TeacherService teacherServ;
    @MockBean
    GroupService groupServ;
    @MockBean
    RoomService roomServ;
    @MockBean
    CourseService courseServ;
    @MockBean
    StudentService studentServ;
    @MockBean
    Adapter adapter;
    @MockBean
    LectureDto lectureDto;
    @MockBean
    MapperUtil mapperUtil;

    @Captor
    ArgumentCaptor<Lecture> lectureCaptor;
    @Captor
    ArgumentCaptor<Long> idCaptor;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    LecturesController lecturesController;

    DateTimeFormatter pattern;
    static Group group;
    static Course course;
    static Teacher teacher;
    static Room room;

    @BeforeEach
    void setUp() throws Exception {
        pattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        group = new Group(1L, "group");
        course = new Course(1L, "course", "description");
        teacher = new Teacher(1L, "name", "surname");
        room = new Room(1L, "address", 10);
    }

    @ParameterizedTest
    @WithUserDetails("testAdmin")
    @MethodSource("provideShowTimetable_shouldComeBack_whenDateNotValid")
    void testShowTimetable_shouldComeBack_whenDateNotValidAndAdmin(Long ownerId, String sDate, String fDate,
            String ownername, @SuppressWarnings("rawtypes") Optional owner, String infoAboutOner) throws Exception {
        when(adapter.getOwnerByIdByName(ownerId, ownername)).thenReturn(owner);

        mockMvc.perform(get("/lectures/{ownername}/{id}/timetable?startDate={sDate}&finishDate={fDate}", ownername,
                ownerId, sDate, fDate).with(csrf())).andDo(print()).andExpect(status().is(200))
                .andExpect(view().name("/lectures/timetable"))
                .andExpect(content().string(containsString(infoAboutOner)));

        verify(adapter, times(1)).getOwnerByIdByName(ownerId, ownername);
    }

    @ParameterizedTest
    @WithUserDetails("testStaff")
    @MethodSource("provideShowTimetable_shouldComeBack_whenDateNotValid")
    void testShowTimetable_shouldComeBack_whenDateNotValidAndStaff(Long ownerId, String sDate, String fDate,
            String ownername, @SuppressWarnings("rawtypes") Optional owner, String infoAboutOner) throws Exception {
        when(adapter.getOwnerByIdByName(ownerId, ownername)).thenReturn(owner);

        mockMvc.perform(get("/lectures/{ownername}/{id}/timetable?startDate={sDate}&finishDate={fDate}", ownername,
                ownerId, sDate, fDate).with(csrf())).andDo(print()).andExpect(status().is(200))
                .andExpect(view().name("/lectures/timetable"))
                .andExpect(content().string(containsString(infoAboutOner)));

        verify(adapter, times(1)).getOwnerByIdByName(ownerId, ownername);
    }

    @ParameterizedTest
    @WithUserDetails("testStudent")
    @MethodSource("provideShowTimetable_shouldComeBack_whenDateNotValid")
    void testShowTimetable_shouldComeBack_whenDateNotValidAndStudent(Long ownerId, String sDate, String fDate,
            String ownername, @SuppressWarnings("rawtypes") Optional owner, String infoAboutOner) throws Exception {
        when(adapter.getOwnerByIdByName(ownerId, ownername)).thenReturn(owner);

        mockMvc.perform(get("/lectures/{ownername}/{id}/timetable?startDate={sDate}&finishDate={fDate}", ownername,
                ownerId, sDate, fDate).with(csrf())).andDo(print()).andExpect(status().is(200))
                .andExpect(view().name("/lectures/timetable"))
                .andExpect(content().string(containsString(infoAboutOner)));

        verify(adapter, times(1)).getOwnerByIdByName(ownerId, ownername);
    }

    @ParameterizedTest
    @WithAnonymousUser
    @MethodSource("provideShowTimetable_shouldComeBack_whenDateNotValid")
    void testShowTimetable_shouldComeBack_whenDateNotValidAndAnonymous(Long ownerId, String sDate, String fDate,
            String ownername, @SuppressWarnings("rawtypes") Optional owner, String infoAboutOner) throws Exception {
        when(adapter.getOwnerByIdByName(ownerId, ownername)).thenReturn(owner);

        mockMvc.perform(get("/lectures/{ownername}/{id}/timetable?startDate={sDate}&finishDate={fDate}", ownername,
                ownerId, sDate, fDate).with(csrf())).andDo(print()).andExpect(status().is(401));

        verify(adapter, times(0)).getOwnerByIdByName(ownerId, ownername);
    }

    private static Stream<Arguments> provideShowTimetable_shouldComeBack_whenDateNotValid() {
//TODO Long ownerId, String sDate, String fDate, String ownername, @SuppressWarnings("rawtypes") Optional owner, String infoAboutOner
        return Stream.of(Arguments.of(10001L, "", "", "group", Optional.of(new Group(10001L, "AA-11")), "AA-11"),
                Arguments.of(10001L, "0001-01-01", "", "group", Optional.of(new Group(10001L, "AA-11")), "AA-11"),
                Arguments.of(10001L, "", "0001-01-01", "group", Optional.of(new Group(10001L, "AA-11")), "AA-11"),
                Arguments.of(10001L, "0002-02-02", "0001-01-01", "group", Optional.of(new Group(10001L, "AA-11")),
                        "AA-11"),
                Arguments.of(10001L, "", "", "room", Optional.of(new Room(10001L, "address", 100)), "address/100"),
                Arguments.of(10001L, "0001-01-01", "", "room", Optional.of(new Room(10001L, "address", 100)),
                        "address/100"),
                Arguments.of(10001L, "", "0001-01-01", "room", Optional.of(new Room(10001L, "address", 100)),
                        "address/100"),
                Arguments.of(10001L, "0002-02-02", "0001-01-01", "room", Optional.of(new Room(10001L, "address", 100)),
                        "address/100"),
                Arguments.of(10001L, "", "", "teacher", Optional.of(new Teacher(10001L, "name", "sername")),
                        "n.sername"),
                Arguments.of(10001L, "0001-01-01", "", "teacher", Optional.of(new Teacher(10001L, "name", "sername")),
                        "n.sername"),
                Arguments.of(10001L, "", "0001-01-01", "teacher", Optional.of(new Teacher(10001L, "name", "sername")),
                        "n.sername"),
                Arguments.of(10001L, "0002-02-02", "0001-01-01", "teacher",
                        Optional.of(new Teacher(10001L, "name", "sername")), "n.sername"));
    }

    @ParameterizedTest
    @WithUserDetails("testAdmin")
    @MethodSource("provideshouldErrorPage_whenSomethingWrongHappens")
    void testShowTimetable_shouldErrorPage_whenSomethingWrongHappensAndAdmin(Long ownerId, String sDate, String fDate,
            String ownername, @SuppressWarnings("rawtypes") Optional owner, String infoAboutOner) throws Exception {
        when(adapter.getOwnerByIdByName(ownerId, ownername)).thenReturn(owner);

        mockMvc.perform(get("/lectures/{ownername}/{id}/timetable?startDate={sDate}&finishDate={fDate}", ownername,
                ownerId, sDate, fDate).with(csrf())).andDo(print()).andExpect(status().is(400))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UniversityException))
                .andExpect(
                        result -> assertEquals("something happend with treatment " + ownername + ", try again please",
                                result.getResolvedException().getMessage()));

        verify(adapter, times(1)).getOwnerByIdByName(ownerId, ownername);
    }

    @ParameterizedTest
    @WithUserDetails("testStaff")
    @MethodSource("provideshouldErrorPage_whenSomethingWrongHappens")
    void testShowTimetable_shouldErrorPage_whenSomethingWrongHappensAndStaff(Long ownerId, String sDate, String fDate,
            String ownername, @SuppressWarnings("rawtypes") Optional owner, String infoAboutOner) throws Exception {
        when(adapter.getOwnerByIdByName(ownerId, ownername)).thenReturn(owner);

        mockMvc.perform(get("/lectures/{ownername}/{id}/timetable?startDate={sDate}&finishDate={fDate}", ownername,
                ownerId, sDate, fDate).with(csrf())).andDo(print()).andExpect(status().is(400))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UniversityException))
                .andExpect(
                        result -> assertEquals("something happend with treatment " + ownername + ", try again please",
                                result.getResolvedException().getMessage()));

        verify(adapter, times(1)).getOwnerByIdByName(ownerId, ownername);
    }

    @ParameterizedTest
    @WithUserDetails("testStudent")
    @MethodSource("provideshouldErrorPage_whenSomethingWrongHappens")
    void testShowTimetable_shouldErrorPage_whenSomethingWrongHappensAndStudent(Long ownerId, String sDate, String fDate,
            String ownername, @SuppressWarnings("rawtypes") Optional owner, String infoAboutOner) throws Exception {
        when(adapter.getOwnerByIdByName(ownerId, ownername)).thenReturn(owner);

        mockMvc.perform(get("/lectures/{ownername}/{id}/timetable?startDate={sDate}&finishDate={fDate}", ownername,
                ownerId, sDate, fDate).with(csrf())).andDo(print()).andExpect(status().is(400))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UniversityException))
                .andExpect(
                        result -> assertEquals("something happend with treatment " + ownername + ", try again please",
                                result.getResolvedException().getMessage()));

        verify(adapter, times(1)).getOwnerByIdByName(ownerId, ownername);
    }

    @ParameterizedTest
    @WithAnonymousUser
    @MethodSource("provideshouldErrorPage_whenSomethingWrongHappens")
    void testShowTimetable_shouldErrorPage_whenSomethingWrongHappensAndAnonymous(Long ownerId, String sDate,
            String fDate, String ownername, @SuppressWarnings("rawtypes") Optional owner, String infoAboutOner)
            throws Exception {
        when(adapter.getOwnerByIdByName(ownerId, ownername)).thenReturn(owner);

        mockMvc.perform(get("/lectures/{ownername}/{id}/timetable?startDate={sDate}&finishDate={fDate}", ownername,
                ownerId, sDate, fDate).with(csrf())).andDo(print()).andExpect(status().is(401));

        verify(adapter, times(0)).getOwnerByIdByName(ownerId, ownername);
    }

    private static Stream<Arguments> provideshouldErrorPage_whenSomethingWrongHappens() {
//TODO Long ownerId, String sDate, String fDate, String ownername, @SuppressWarnings("rawtypes") Optional owner, String infoAboutOner
        return Stream.of(Arguments.of(10001L, "", "", "group", Optional.empty(), "AA-11"),
                Arguments.of(10001L, "", "", "room", Optional.empty(), "address/100"),
                Arguments.of(10001L, "", "", "teacher", Optional.empty(), "n.sername"));
    }

    @SuppressWarnings("rawtypes")
    @ParameterizedTest
    @WithUserDetails("testAdmin")
    @MethodSource("provideShowTimetable_shouldRun_whenFinishDateAndThanStartDateIsOk")
    void testShowTimetable_shouldRun_whenFinishDateAndThanStartDateIsOkAndAdmin(Long ownerId, String ownername,
            Optional owner, String showcase, String page, String timeview, String mode, List<String> result)
            throws Exception {
        String startDate = "29-12-2022";
        String firstDate = "30-12-2022";
        String secondDate = "01-01-2023";
        String finishDate = "02-01-2023";

        SortedMap<LocalDate, ArrayList<LectureDto>> timetablePerDay = new TreeMap<LocalDate, ArrayList<LectureDto>>();
        timetablePerDay.put(LocalDate.parse(startDate, pattern), makeBlankDay(startDate));
        timetablePerDay.put(LocalDate.parse(firstDate, pattern),
                new ArrayList<>(Arrays.asList(new LectureDto(1, LocalDate.parse(firstDate, pattern), 4L, 2L, 3L, 2L),
                        new LectureDto(2, LocalDate.parse(firstDate, pattern), null, null, null, null),
                        new LectureDto(3, LocalDate.parse(firstDate, pattern), 4L, 2L, 3L, 2L),
                        new LectureDto(4, LocalDate.parse(firstDate, pattern), null, null, null, null),
                        new LectureDto(5, LocalDate.parse(firstDate, pattern), null, null, null, null),
                        new LectureDto(6, LocalDate.parse(firstDate, pattern), null, null, null, null))));
        timetablePerDay.put(LocalDate.parse("31-12-2022", pattern), makeBlankDay("31-12-2022"));
        timetablePerDay.put(LocalDate.parse(secondDate, pattern),
                new ArrayList<>(
                        Arrays.asList(new LectureDto(1, LocalDate.parse(secondDate, pattern), null, null, null, null),
                                new LectureDto(2, LocalDate.parse(secondDate, pattern), null, null, null, null),
                                new LectureDto(3, LocalDate.parse(secondDate, pattern), null, null, null, null),
                                new LectureDto(4, LocalDate.parse(secondDate, pattern), null, null, null, null),
                                new LectureDto(5, LocalDate.parse(secondDate, pattern), 4L, 2L, 3L, 2L),
                                new LectureDto(6, LocalDate.parse(secondDate, pattern), null, null, null, null))));
        timetablePerDay.put(LocalDate.parse(finishDate, pattern), makeBlankDay(finishDate));

        when(adapter.getOwnerByIdByName(ownerId, ownername)).thenReturn(owner);

        when(groupServ.retrieveGroupByStudentId(ownerId)).thenReturn(new Group(2L, "groupName2"));
        when(teacherServ.retrieveAll()).thenReturn(Arrays.asList(new Teacher(1L, "Name1", "Surname1"),
                new Teacher(2L, "Name2", "Surname2"), new Teacher(3L, "Name3", "Surname3")));
        when(groupServ.retrieveAll()).thenReturn(
                Arrays.asList(new Group(1L, "groupName1"), new Group(2L, "groupName2"), new Group(3L, "groupName3")));
        when(courseServ.retrieveAll()).thenReturn(Arrays.asList(new Course(1L, "Course1", "Description1"),
                new Course(2L, "Course2", "Description2"), new Course(3L, "Course3", "Description3"),
                new Course(4L, "Course4", "Description4"), new Course(5L, "Course5", "Description5")));
        when(roomServ.retrieveAll())
                .thenReturn(Arrays.asList(new Room(1L, "address1", 10), new Room(2L, "address2", 20),
                        new Room(3L, "address3", 30), new Room(4L, "address4", 40), new Room(5L, "address5", 50)));
        when(mapperUtil.toDto(new Teacher(1L, "Name1", "Surname1")))
                .thenReturn(new TeacherDto(1L, "Name1", "Surname1"));
        when(mapperUtil.toDto(new Teacher(2L, "Name2", "Surname2")))
                .thenReturn(new TeacherDto(2L, "Name2", "Surname2"));
        when(mapperUtil.toDto(new Teacher(3L, "Name3", "Surname3")))
                .thenReturn(new TeacherDto(3L, "Name3", "Surname3"));
        when(mapperUtil.toDto(new Group(1L, "groupName1"))).thenReturn(new GroupDto(1L, "groupName1"));
        when(mapperUtil.toDto(new Group(2L, "groupName2"))).thenReturn(new GroupDto(2L, "groupName2"));
        when(mapperUtil.toDto(new Group(3L, "groupName3"))).thenReturn(new GroupDto(3L, "groupName3"));
        when(mapperUtil.toDto(new Course(1L, "Course1", "Description1")))
                .thenReturn(new CourseDto(1L, "Course1", "Description1"));
        when(mapperUtil.toDto(new Course(2L, "Course2", "Description2")))
                .thenReturn(new CourseDto(2L, "Course2", "Description2"));
        when(mapperUtil.toDto(new Course(3L, "Course3", "Description3")))
                .thenReturn(new CourseDto(3L, "Course3", "Description3"));
        when(mapperUtil.toDto(new Course(4L, "Course4", "Description4")))
                .thenReturn(new CourseDto(4L, "Course4", "Description4"));
        when(mapperUtil.toDto(new Course(5L, "Course5", "Description5")))
                .thenReturn(new CourseDto(5L, "Course5", "Description5"));
        when(mapperUtil.toDto(new Room(1L, "address1", 10))).thenReturn(new RoomDto(1L, "address1", 10));
        when(mapperUtil.toDto(new Room(2L, "address2", 20))).thenReturn(new RoomDto(2L, "address2", 20));
        when(mapperUtil.toDto(new Room(3L, "address3", 30))).thenReturn(new RoomDto(3L, "address3", 30));
        when(mapperUtil.toDto(new Room(4L, "address4", 40))).thenReturn(new RoomDto(4L, "address4", 40));
        when(mapperUtil.toDto(new Room(5L, "address5", 50))).thenReturn(new RoomDto(5L, "address5", 50));

// new Lecture(null, finishDate, room, teacher, course, group)
        SortedMap<LocalDate, ArrayList<LectureDto>> monthOne = new TreeMap<>();
        SortedMap<LocalDate, ArrayList<LectureDto>> monthTwo = new TreeMap<>();
        List<SortedMap<LocalDate, ArrayList<LectureDto>>> timetablePerMonth = Arrays.asList(monthOne, monthTwo);
        monthOne.put(LocalDate.parse("01-12-2022", pattern), makeBlankDay("01-12-2022"));
        monthOne.put(LocalDate.parse("02-12-2022", pattern), makeBlankDay("02-12-2022"));
        monthOne.put(LocalDate.parse("03-12-2022", pattern), makeBlankDay("03-12-2022"));
        monthOne.put(LocalDate.parse("04-12-2022", pattern), makeBlankDay("04-12-2022"));
        monthOne.put(LocalDate.parse("05-12-2022", pattern), makeBlankDay("05-12-2022"));
        monthOne.put(LocalDate.parse("06-12-2022", pattern), makeBlankDay("06-12-2022"));
        monthOne.put(LocalDate.parse("07-12-2022", pattern), makeBlankDay("07-12-2022"));
        monthOne.put(LocalDate.parse("08-12-2022", pattern), makeBlankDay("08-12-2022"));
        monthOne.put(LocalDate.parse("09-12-2022", pattern), makeBlankDay("09-12-2022"));
        monthOne.put(LocalDate.parse("10-12-2022", pattern), makeBlankDay("10-12-2022"));
        monthOne.put(LocalDate.parse("11-12-2022", pattern), makeBlankDay("11-12-2022"));
        monthOne.put(LocalDate.parse("12-12-2022", pattern), makeBlankDay("12-12-2022"));
        monthOne.put(LocalDate.parse("13-12-2022", pattern), makeBlankDay("13-12-2022"));
        monthOne.put(LocalDate.parse("14-12-2022", pattern), makeBlankDay("14-12-2022"));
        monthOne.put(LocalDate.parse("15-12-2022", pattern), makeBlankDay("15-12-2022"));
        monthOne.put(LocalDate.parse("16-12-2022", pattern), makeBlankDay("16-12-2022"));
        monthOne.put(LocalDate.parse("17-12-2022", pattern), makeBlankDay("17-12-2022"));
        monthOne.put(LocalDate.parse("18-12-2022", pattern), makeBlankDay("18-12-2022"));
        monthOne.put(LocalDate.parse("19-12-2022", pattern), makeBlankDay("19-12-2022"));
        monthOne.put(LocalDate.parse("20-12-2022", pattern), makeBlankDay("20-12-2022"));
        monthOne.put(LocalDate.parse("21-12-2022", pattern), makeBlankDay("21-12-2022"));
        monthOne.put(LocalDate.parse("22-12-2022", pattern), makeBlankDay("22-12-2022"));
        monthOne.put(LocalDate.parse("23-12-2022", pattern), makeBlankDay("23-12-2022"));
        monthOne.put(LocalDate.parse("24-12-2022", pattern), makeBlankDay("24-12-2022"));
        monthOne.put(LocalDate.parse("25-12-2022", pattern), makeBlankDay("25-12-2022"));
        monthOne.put(LocalDate.parse("26-12-2022", pattern), makeBlankDay("26-12-2022"));
        monthOne.put(LocalDate.parse("27-12-2022", pattern), makeBlankDay("27-12-2022"));
        monthOne.put(LocalDate.parse("28-12-2022", pattern), makeBlankDay("28-12-2022"));
        monthOne.put(LocalDate.parse(startDate, pattern), timetablePerDay.get(LocalDate.parse(startDate, pattern)));
        monthOne.put(LocalDate.parse(firstDate, pattern), timetablePerDay.get(LocalDate.parse(firstDate, pattern)));
        monthOne.put(LocalDate.parse("31-12-2022", pattern),
                timetablePerDay.get(LocalDate.parse("31-12-2022", pattern)));
        monthTwo.put(LocalDate.parse(secondDate, pattern), timetablePerDay.get(LocalDate.parse(secondDate, pattern)));
        monthTwo.put(LocalDate.parse(finishDate, pattern), timetablePerDay.get(LocalDate.parse(finishDate, pattern)));
        monthTwo.put(LocalDate.parse("03-01-2023", pattern), makeBlankDay("03-01-2023"));
        monthTwo.put(LocalDate.parse("04-01-2023", pattern), makeBlankDay("04-01-2023"));
        monthTwo.put(LocalDate.parse("05-01-2023", pattern), makeBlankDay("05-01-2023"));
        monthTwo.put(LocalDate.parse("06-01-2023", pattern), makeBlankDay("06-01-2023"));
        monthTwo.put(LocalDate.parse("07-01-2023", pattern), makeBlankDay("07-01-2023"));
        monthTwo.put(LocalDate.parse("08-01-2023", pattern), makeBlankDay("08-01-2023"));
        monthTwo.put(LocalDate.parse("09-01-2023", pattern), makeBlankDay("09-01-2023"));
        monthTwo.put(LocalDate.parse("10-01-2023", pattern), makeBlankDay("10-01-2023"));
        monthTwo.put(LocalDate.parse("11-01-2023", pattern), makeBlankDay("11-01-2023"));
        monthTwo.put(LocalDate.parse("12-01-2023", pattern), makeBlankDay("12-01-2023"));
        monthTwo.put(LocalDate.parse("13-01-2023", pattern), makeBlankDay("13-01-2023"));
        monthTwo.put(LocalDate.parse("14-01-2023", pattern), makeBlankDay("14-01-2023"));
        monthTwo.put(LocalDate.parse("15-01-2023", pattern), makeBlankDay("15-01-2023"));
        monthTwo.put(LocalDate.parse("16-01-2023", pattern), makeBlankDay("16-01-2023"));
        monthTwo.put(LocalDate.parse("17-01-2023", pattern), makeBlankDay("17-01-2023"));
        monthTwo.put(LocalDate.parse("18-01-2023", pattern), makeBlankDay("18-01-2023"));
        monthTwo.put(LocalDate.parse("19-01-2023", pattern), makeBlankDay("19-01-2023"));
        monthTwo.put(LocalDate.parse("20-01-2023", pattern), makeBlankDay("20-01-2023"));
        monthTwo.put(LocalDate.parse("21-01-2023", pattern), makeBlankDay("21-01-2023"));
        monthTwo.put(LocalDate.parse("22-01-2023", pattern), makeBlankDay("22-01-2023"));
        monthTwo.put(LocalDate.parse("23-01-2023", pattern), makeBlankDay("23-01-2023"));
        monthTwo.put(LocalDate.parse("24-01-2023", pattern), makeBlankDay("24-01-2023"));
        monthTwo.put(LocalDate.parse("25-01-2023", pattern), makeBlankDay("25-01-2023"));
        monthTwo.put(LocalDate.parse("26-01-2023", pattern), makeBlankDay("26-01-2023"));
        monthTwo.put(LocalDate.parse("27-01-2023", pattern), makeBlankDay("27-01-2023"));
        monthTwo.put(LocalDate.parse("28-01-2023", pattern), makeBlankDay("28-01-2023"));
        monthTwo.put(LocalDate.parse("29-01-2023", pattern), makeBlankDay("29-01-2023"));
        monthTwo.put(LocalDate.parse("30-01-2023", pattern), makeBlankDay("30-01-2023"));
        monthTwo.put(LocalDate.parse("31-01-2023", pattern), makeBlankDay("31-01-2023"));

        when(adapter.stuffTimetableByOwnername(LocalDate.parse(startDate, pattern),
                LocalDate.parse(finishDate, pattern), ownerId, ownername)).thenReturn(timetablePerDay);
        when(adapter.wrapForMonthView(timetablePerDay)).thenReturn(timetablePerMonth);

        mockMvc.perform(get("/lectures/{ownername}/{id}/{timeview}?startDate={startDate}&finishDate={finishDate}",
                ownername, ownerId, timeview, LocalDate.parse(startDate, pattern), LocalDate.parse(finishDate, pattern))
                .with(csrf()).queryParam("showcase", showcase).queryParam("page", page).queryParam("mode", mode))
                .andDo(print()).andExpect(status().is(200)).andExpect(view().name("/lectures/timetable"))
                .andExpect(content().string(containsString(result.get(0))))
                .andExpect(content().string(containsString("2022-12-29")))
                .andExpect(content().string(containsString("2023-01-02")))
                .andExpect(content().string(containsString(result.get(1))))
                .andExpect(content().string(containsString(result.get(2))))
                .andExpect(content().string(containsString(result.get(3))))
                .andExpect(content().string(containsString(result.get(4))))
                .andExpect(content().string(containsString(result.get(5))))
                .andExpect(content().string(containsString(result.get(6))))
                .andExpect(content().string(containsString(result.get(7))))
                .andExpect(content().string(containsString(result.get(8))))
                .andExpect(content().string(containsString(result.get(9))))
                .andExpect(content().string(containsString(result.get(10))))
                .andExpect(content().string(containsString(result.get(11))))
                .andExpect(content().string(containsString(result.get(12))))
                .andExpect(content().string(containsString(result.get(13))));
    }

    @SuppressWarnings("rawtypes")
    @ParameterizedTest
    @WithUserDetails("testStaff")
    @MethodSource("provideShowTimetable_shouldRun_whenFinishDateAndThanStartDateIsOk")
    void testShowTimetable_shouldRun_whenFinishDateAndThanStartDateIsOkAndStaff(Long ownerId, String ownername,
            Optional owner, String showcase, String page, String timeview, String mode, List<String> result)
            throws Exception {
        String startDate = "29-12-2022";
        String firstDate = "30-12-2022";
        String secondDate = "01-01-2023";
        String finishDate = "02-01-2023";

        SortedMap<LocalDate, ArrayList<LectureDto>> timetablePerDay = new TreeMap<LocalDate, ArrayList<LectureDto>>();
        timetablePerDay.put(LocalDate.parse(startDate, pattern), makeBlankDay(startDate));
        timetablePerDay.put(LocalDate.parse(firstDate, pattern),
                new ArrayList<>(Arrays.asList(new LectureDto(1, LocalDate.parse(firstDate, pattern), 4L, 2L, 3L, 2L),
                        new LectureDto(2, LocalDate.parse(firstDate, pattern), null, null, null, null),
                        new LectureDto(3, LocalDate.parse(firstDate, pattern), 4L, 2L, 3L, 2L),
                        new LectureDto(4, LocalDate.parse(firstDate, pattern), null, null, null, null),
                        new LectureDto(5, LocalDate.parse(firstDate, pattern), null, null, null, null),
                        new LectureDto(6, LocalDate.parse(firstDate, pattern), null, null, null, null))));
        timetablePerDay.put(LocalDate.parse("31-12-2022", pattern), makeBlankDay("31-12-2022"));
        timetablePerDay.put(LocalDate.parse(secondDate, pattern),
                new ArrayList<>(
                        Arrays.asList(new LectureDto(1, LocalDate.parse(secondDate, pattern), null, null, null, null),
                                new LectureDto(2, LocalDate.parse(secondDate, pattern), null, null, null, null),
                                new LectureDto(3, LocalDate.parse(secondDate, pattern), null, null, null, null),
                                new LectureDto(4, LocalDate.parse(secondDate, pattern), null, null, null, null),
                                new LectureDto(5, LocalDate.parse(secondDate, pattern), 4L, 2L, 3L, 2L),
                                new LectureDto(6, LocalDate.parse(secondDate, pattern), null, null, null, null))));
        timetablePerDay.put(LocalDate.parse(finishDate, pattern), makeBlankDay(finishDate));

        when(adapter.getOwnerByIdByName(ownerId, ownername)).thenReturn(owner);

        when(groupServ.retrieveGroupByStudentId(ownerId)).thenReturn(new Group(2L, "groupName2"));
        when(teacherServ.retrieveAll()).thenReturn(Arrays.asList(new Teacher(1L, "Name1", "Surname1"),
                new Teacher(2L, "Name2", "Surname2"), new Teacher(3L, "Name3", "Surname3")));
        when(groupServ.retrieveAll()).thenReturn(
                Arrays.asList(new Group(1L, "groupName1"), new Group(2L, "groupName2"), new Group(3L, "groupName3")));
        when(courseServ.retrieveAll()).thenReturn(Arrays.asList(new Course(1L, "Course1", "Description1"),
                new Course(2L, "Course2", "Description2"), new Course(3L, "Course3", "Description3"),
                new Course(4L, "Course4", "Description4"), new Course(5L, "Course5", "Description5")));
        when(roomServ.retrieveAll())
                .thenReturn(Arrays.asList(new Room(1L, "address1", 10), new Room(2L, "address2", 20),
                        new Room(3L, "address3", 30), new Room(4L, "address4", 40), new Room(5L, "address5", 50)));
        when(mapperUtil.toDto(new Teacher(1L, "Name1", "Surname1")))
                .thenReturn(new TeacherDto(1L, "Name1", "Surname1"));
        when(mapperUtil.toDto(new Teacher(2L, "Name2", "Surname2")))
                .thenReturn(new TeacherDto(2L, "Name2", "Surname2"));
        when(mapperUtil.toDto(new Teacher(3L, "Name3", "Surname3")))
                .thenReturn(new TeacherDto(3L, "Name3", "Surname3"));
        when(mapperUtil.toDto(new Group(1L, "groupName1"))).thenReturn(new GroupDto(1L, "groupName1"));
        when(mapperUtil.toDto(new Group(2L, "groupName2"))).thenReturn(new GroupDto(2L, "groupName2"));
        when(mapperUtil.toDto(new Group(3L, "groupName3"))).thenReturn(new GroupDto(3L, "groupName3"));
        when(mapperUtil.toDto(new Course(1L, "Course1", "Description1")))
                .thenReturn(new CourseDto(1L, "Course1", "Description1"));
        when(mapperUtil.toDto(new Course(2L, "Course2", "Description2")))
                .thenReturn(new CourseDto(2L, "Course2", "Description2"));
        when(mapperUtil.toDto(new Course(3L, "Course3", "Description3")))
                .thenReturn(new CourseDto(3L, "Course3", "Description3"));
        when(mapperUtil.toDto(new Course(4L, "Course4", "Description4")))
                .thenReturn(new CourseDto(4L, "Course4", "Description4"));
        when(mapperUtil.toDto(new Course(5L, "Course5", "Description5")))
                .thenReturn(new CourseDto(5L, "Course5", "Description5"));
        when(mapperUtil.toDto(new Room(1L, "address1", 10))).thenReturn(new RoomDto(1L, "address1", 10));
        when(mapperUtil.toDto(new Room(2L, "address2", 20))).thenReturn(new RoomDto(2L, "address2", 20));
        when(mapperUtil.toDto(new Room(3L, "address3", 30))).thenReturn(new RoomDto(3L, "address3", 30));
        when(mapperUtil.toDto(new Room(4L, "address4", 40))).thenReturn(new RoomDto(4L, "address4", 40));
        when(mapperUtil.toDto(new Room(5L, "address5", 50))).thenReturn(new RoomDto(5L, "address5", 50));

// new Lecture(null, finishDate, room, teacher, course, group)
        SortedMap<LocalDate, ArrayList<LectureDto>> monthOne = new TreeMap<>();
        SortedMap<LocalDate, ArrayList<LectureDto>> monthTwo = new TreeMap<>();
        List<SortedMap<LocalDate, ArrayList<LectureDto>>> timetablePerMonth = Arrays.asList(monthOne, monthTwo);
        monthOne.put(LocalDate.parse("01-12-2022", pattern), makeBlankDay("01-12-2022"));
        monthOne.put(LocalDate.parse("02-12-2022", pattern), makeBlankDay("02-12-2022"));
        monthOne.put(LocalDate.parse("03-12-2022", pattern), makeBlankDay("03-12-2022"));
        monthOne.put(LocalDate.parse("04-12-2022", pattern), makeBlankDay("04-12-2022"));
        monthOne.put(LocalDate.parse("05-12-2022", pattern), makeBlankDay("05-12-2022"));
        monthOne.put(LocalDate.parse("06-12-2022", pattern), makeBlankDay("06-12-2022"));
        monthOne.put(LocalDate.parse("07-12-2022", pattern), makeBlankDay("07-12-2022"));
        monthOne.put(LocalDate.parse("08-12-2022", pattern), makeBlankDay("08-12-2022"));
        monthOne.put(LocalDate.parse("09-12-2022", pattern), makeBlankDay("09-12-2022"));
        monthOne.put(LocalDate.parse("10-12-2022", pattern), makeBlankDay("10-12-2022"));
        monthOne.put(LocalDate.parse("11-12-2022", pattern), makeBlankDay("11-12-2022"));
        monthOne.put(LocalDate.parse("12-12-2022", pattern), makeBlankDay("12-12-2022"));
        monthOne.put(LocalDate.parse("13-12-2022", pattern), makeBlankDay("13-12-2022"));
        monthOne.put(LocalDate.parse("14-12-2022", pattern), makeBlankDay("14-12-2022"));
        monthOne.put(LocalDate.parse("15-12-2022", pattern), makeBlankDay("15-12-2022"));
        monthOne.put(LocalDate.parse("16-12-2022", pattern), makeBlankDay("16-12-2022"));
        monthOne.put(LocalDate.parse("17-12-2022", pattern), makeBlankDay("17-12-2022"));
        monthOne.put(LocalDate.parse("18-12-2022", pattern), makeBlankDay("18-12-2022"));
        monthOne.put(LocalDate.parse("19-12-2022", pattern), makeBlankDay("19-12-2022"));
        monthOne.put(LocalDate.parse("20-12-2022", pattern), makeBlankDay("20-12-2022"));
        monthOne.put(LocalDate.parse("21-12-2022", pattern), makeBlankDay("21-12-2022"));
        monthOne.put(LocalDate.parse("22-12-2022", pattern), makeBlankDay("22-12-2022"));
        monthOne.put(LocalDate.parse("23-12-2022", pattern), makeBlankDay("23-12-2022"));
        monthOne.put(LocalDate.parse("24-12-2022", pattern), makeBlankDay("24-12-2022"));
        monthOne.put(LocalDate.parse("25-12-2022", pattern), makeBlankDay("25-12-2022"));
        monthOne.put(LocalDate.parse("26-12-2022", pattern), makeBlankDay("26-12-2022"));
        monthOne.put(LocalDate.parse("27-12-2022", pattern), makeBlankDay("27-12-2022"));
        monthOne.put(LocalDate.parse("28-12-2022", pattern), makeBlankDay("28-12-2022"));
        monthOne.put(LocalDate.parse(startDate, pattern), timetablePerDay.get(LocalDate.parse(startDate, pattern)));
        monthOne.put(LocalDate.parse(firstDate, pattern), timetablePerDay.get(LocalDate.parse(firstDate, pattern)));
        monthOne.put(LocalDate.parse("31-12-2022", pattern),
                timetablePerDay.get(LocalDate.parse("31-12-2022", pattern)));
        monthTwo.put(LocalDate.parse(secondDate, pattern), timetablePerDay.get(LocalDate.parse(secondDate, pattern)));
        monthTwo.put(LocalDate.parse(finishDate, pattern), timetablePerDay.get(LocalDate.parse(finishDate, pattern)));
        monthTwo.put(LocalDate.parse("03-01-2023", pattern), makeBlankDay("03-01-2023"));
        monthTwo.put(LocalDate.parse("04-01-2023", pattern), makeBlankDay("04-01-2023"));
        monthTwo.put(LocalDate.parse("05-01-2023", pattern), makeBlankDay("05-01-2023"));
        monthTwo.put(LocalDate.parse("06-01-2023", pattern), makeBlankDay("06-01-2023"));
        monthTwo.put(LocalDate.parse("07-01-2023", pattern), makeBlankDay("07-01-2023"));
        monthTwo.put(LocalDate.parse("08-01-2023", pattern), makeBlankDay("08-01-2023"));
        monthTwo.put(LocalDate.parse("09-01-2023", pattern), makeBlankDay("09-01-2023"));
        monthTwo.put(LocalDate.parse("10-01-2023", pattern), makeBlankDay("10-01-2023"));
        monthTwo.put(LocalDate.parse("11-01-2023", pattern), makeBlankDay("11-01-2023"));
        monthTwo.put(LocalDate.parse("12-01-2023", pattern), makeBlankDay("12-01-2023"));
        monthTwo.put(LocalDate.parse("13-01-2023", pattern), makeBlankDay("13-01-2023"));
        monthTwo.put(LocalDate.parse("14-01-2023", pattern), makeBlankDay("14-01-2023"));
        monthTwo.put(LocalDate.parse("15-01-2023", pattern), makeBlankDay("15-01-2023"));
        monthTwo.put(LocalDate.parse("16-01-2023", pattern), makeBlankDay("16-01-2023"));
        monthTwo.put(LocalDate.parse("17-01-2023", pattern), makeBlankDay("17-01-2023"));
        monthTwo.put(LocalDate.parse("18-01-2023", pattern), makeBlankDay("18-01-2023"));
        monthTwo.put(LocalDate.parse("19-01-2023", pattern), makeBlankDay("19-01-2023"));
        monthTwo.put(LocalDate.parse("20-01-2023", pattern), makeBlankDay("20-01-2023"));
        monthTwo.put(LocalDate.parse("21-01-2023", pattern), makeBlankDay("21-01-2023"));
        monthTwo.put(LocalDate.parse("22-01-2023", pattern), makeBlankDay("22-01-2023"));
        monthTwo.put(LocalDate.parse("23-01-2023", pattern), makeBlankDay("23-01-2023"));
        monthTwo.put(LocalDate.parse("24-01-2023", pattern), makeBlankDay("24-01-2023"));
        monthTwo.put(LocalDate.parse("25-01-2023", pattern), makeBlankDay("25-01-2023"));
        monthTwo.put(LocalDate.parse("26-01-2023", pattern), makeBlankDay("26-01-2023"));
        monthTwo.put(LocalDate.parse("27-01-2023", pattern), makeBlankDay("27-01-2023"));
        monthTwo.put(LocalDate.parse("28-01-2023", pattern), makeBlankDay("28-01-2023"));
        monthTwo.put(LocalDate.parse("29-01-2023", pattern), makeBlankDay("29-01-2023"));
        monthTwo.put(LocalDate.parse("30-01-2023", pattern), makeBlankDay("30-01-2023"));
        monthTwo.put(LocalDate.parse("31-01-2023", pattern), makeBlankDay("31-01-2023"));

        when(adapter.stuffTimetableByOwnername(LocalDate.parse(startDate, pattern),
                LocalDate.parse(finishDate, pattern), ownerId, ownername)).thenReturn(timetablePerDay);
        when(adapter.wrapForMonthView(timetablePerDay)).thenReturn(timetablePerMonth);

        mockMvc.perform(get("/lectures/{ownername}/{id}/{timeview}?startDate={startDate}&finishDate={finishDate}",
                ownername, ownerId, timeview, LocalDate.parse(startDate, pattern), LocalDate.parse(finishDate, pattern))
                .with(csrf()).queryParam("showcase", showcase).queryParam("page", page).queryParam("mode", mode))
                .andDo(print()).andExpect(status().is(200)).andExpect(view().name("/lectures/timetable"))
                .andExpect(content().string(containsString(result.get(0))))
                .andExpect(content().string(containsString("2022-12-29")))
                .andExpect(content().string(containsString("2023-01-02")))
                .andExpect(content().string(containsString(result.get(1))))
                .andExpect(content().string(containsString(result.get(2))))
                .andExpect(content().string(containsString(result.get(3))))
                .andExpect(content().string(containsString(result.get(4))))
                .andExpect(content().string(containsString(result.get(5))))
                .andExpect(content().string(containsString(result.get(6))))
                .andExpect(content().string(containsString(result.get(7))))
                .andExpect(content().string(containsString(result.get(8))))
                .andExpect(content().string(containsString(result.get(9))))
                .andExpect(content().string(containsString(result.get(10))))
                .andExpect(content().string(containsString(result.get(11))))
                .andExpect(content().string(containsString(result.get(12))))
                .andExpect(content().string(containsString(result.get(13))));
    }

    @SuppressWarnings("rawtypes")
    @ParameterizedTest
    @WithUserDetails("testStudent")
    @MethodSource("provideShowTimetable_shouldRun_whenFinishDateAndThanStartDateIsOk")
    void testShowTimetable_shouldRun_whenFinishDateAndThanStartDateIsOkAndStudent(Long ownerId, String ownername,
            Optional owner, String showcase, String page, String timeview, String mode, List<String> result)
            throws Exception {
        String startDate = "29-12-2022";
        String firstDate = "30-12-2022";
        String secondDate = "01-01-2023";
        String finishDate = "02-01-2023";

        SortedMap<LocalDate, ArrayList<LectureDto>> timetablePerDay = new TreeMap<LocalDate, ArrayList<LectureDto>>();
        timetablePerDay.put(LocalDate.parse(startDate, pattern), makeBlankDay(startDate));
        timetablePerDay.put(LocalDate.parse(firstDate, pattern),
                new ArrayList<>(Arrays.asList(new LectureDto(1, LocalDate.parse(firstDate, pattern), 4L, 2L, 3L, 2L),
                        new LectureDto(2, LocalDate.parse(firstDate, pattern), null, null, null, null),
                        new LectureDto(3, LocalDate.parse(firstDate, pattern), 4L, 2L, 3L, 2L),
                        new LectureDto(4, LocalDate.parse(firstDate, pattern), null, null, null, null),
                        new LectureDto(5, LocalDate.parse(firstDate, pattern), null, null, null, null),
                        new LectureDto(6, LocalDate.parse(firstDate, pattern), null, null, null, null))));
        timetablePerDay.put(LocalDate.parse("31-12-2022", pattern), makeBlankDay("31-12-2022"));
        timetablePerDay.put(LocalDate.parse(secondDate, pattern),
                new ArrayList<>(
                        Arrays.asList(new LectureDto(1, LocalDate.parse(secondDate, pattern), null, null, null, null),
                                new LectureDto(2, LocalDate.parse(secondDate, pattern), null, null, null, null),
                                new LectureDto(3, LocalDate.parse(secondDate, pattern), null, null, null, null),
                                new LectureDto(4, LocalDate.parse(secondDate, pattern), null, null, null, null),
                                new LectureDto(5, LocalDate.parse(secondDate, pattern), 4L, 2L, 3L, 2L),
                                new LectureDto(6, LocalDate.parse(secondDate, pattern), null, null, null, null))));
        timetablePerDay.put(LocalDate.parse(finishDate, pattern), makeBlankDay(finishDate));

        when(adapter.getOwnerByIdByName(ownerId, ownername)).thenReturn(owner);

        when(groupServ.retrieveGroupByStudentId(ownerId)).thenReturn(new Group(2L, "groupName2"));
        when(teacherServ.retrieveAll()).thenReturn(Arrays.asList(new Teacher(1L, "Name1", "Surname1"),
                new Teacher(2L, "Name2", "Surname2"), new Teacher(3L, "Name3", "Surname3")));
        when(groupServ.retrieveAll()).thenReturn(
                Arrays.asList(new Group(1L, "groupName1"), new Group(2L, "groupName2"), new Group(3L, "groupName3")));
        when(courseServ.retrieveAll()).thenReturn(Arrays.asList(new Course(1L, "Course1", "Description1"),
                new Course(2L, "Course2", "Description2"), new Course(3L, "Course3", "Description3"),
                new Course(4L, "Course4", "Description4"), new Course(5L, "Course5", "Description5")));
        when(roomServ.retrieveAll())
                .thenReturn(Arrays.asList(new Room(1L, "address1", 10), new Room(2L, "address2", 20),
                        new Room(3L, "address3", 30), new Room(4L, "address4", 40), new Room(5L, "address5", 50)));
        when(mapperUtil.toDto(new Teacher(1L, "Name1", "Surname1")))
                .thenReturn(new TeacherDto(1L, "Name1", "Surname1"));
        when(mapperUtil.toDto(new Teacher(2L, "Name2", "Surname2")))
                .thenReturn(new TeacherDto(2L, "Name2", "Surname2"));
        when(mapperUtil.toDto(new Teacher(3L, "Name3", "Surname3")))
                .thenReturn(new TeacherDto(3L, "Name3", "Surname3"));
        when(mapperUtil.toDto(new Group(1L, "groupName1"))).thenReturn(new GroupDto(1L, "groupName1"));
        when(mapperUtil.toDto(new Group(2L, "groupName2"))).thenReturn(new GroupDto(2L, "groupName2"));
        when(mapperUtil.toDto(new Group(3L, "groupName3"))).thenReturn(new GroupDto(3L, "groupName3"));
        when(mapperUtil.toDto(new Course(1L, "Course1", "Description1")))
                .thenReturn(new CourseDto(1L, "Course1", "Description1"));
        when(mapperUtil.toDto(new Course(2L, "Course2", "Description2")))
                .thenReturn(new CourseDto(2L, "Course2", "Description2"));
        when(mapperUtil.toDto(new Course(3L, "Course3", "Description3")))
                .thenReturn(new CourseDto(3L, "Course3", "Description3"));
        when(mapperUtil.toDto(new Course(4L, "Course4", "Description4")))
                .thenReturn(new CourseDto(4L, "Course4", "Description4"));
        when(mapperUtil.toDto(new Course(5L, "Course5", "Description5")))
                .thenReturn(new CourseDto(5L, "Course5", "Description5"));
        when(mapperUtil.toDto(new Room(1L, "address1", 10))).thenReturn(new RoomDto(1L, "address1", 10));
        when(mapperUtil.toDto(new Room(2L, "address2", 20))).thenReturn(new RoomDto(2L, "address2", 20));
        when(mapperUtil.toDto(new Room(3L, "address3", 30))).thenReturn(new RoomDto(3L, "address3", 30));
        when(mapperUtil.toDto(new Room(4L, "address4", 40))).thenReturn(new RoomDto(4L, "address4", 40));
        when(mapperUtil.toDto(new Room(5L, "address5", 50))).thenReturn(new RoomDto(5L, "address5", 50));

// new Lecture(null, finishDate, room, teacher, course, group)
        SortedMap<LocalDate, ArrayList<LectureDto>> monthOne = new TreeMap<>();
        SortedMap<LocalDate, ArrayList<LectureDto>> monthTwo = new TreeMap<>();
        List<SortedMap<LocalDate, ArrayList<LectureDto>>> timetablePerMonth = Arrays.asList(monthOne, monthTwo);
        monthOne.put(LocalDate.parse("01-12-2022", pattern), makeBlankDay("01-12-2022"));
        monthOne.put(LocalDate.parse("02-12-2022", pattern), makeBlankDay("02-12-2022"));
        monthOne.put(LocalDate.parse("03-12-2022", pattern), makeBlankDay("03-12-2022"));
        monthOne.put(LocalDate.parse("04-12-2022", pattern), makeBlankDay("04-12-2022"));
        monthOne.put(LocalDate.parse("05-12-2022", pattern), makeBlankDay("05-12-2022"));
        monthOne.put(LocalDate.parse("06-12-2022", pattern), makeBlankDay("06-12-2022"));
        monthOne.put(LocalDate.parse("07-12-2022", pattern), makeBlankDay("07-12-2022"));
        monthOne.put(LocalDate.parse("08-12-2022", pattern), makeBlankDay("08-12-2022"));
        monthOne.put(LocalDate.parse("09-12-2022", pattern), makeBlankDay("09-12-2022"));
        monthOne.put(LocalDate.parse("10-12-2022", pattern), makeBlankDay("10-12-2022"));
        monthOne.put(LocalDate.parse("11-12-2022", pattern), makeBlankDay("11-12-2022"));
        monthOne.put(LocalDate.parse("12-12-2022", pattern), makeBlankDay("12-12-2022"));
        monthOne.put(LocalDate.parse("13-12-2022", pattern), makeBlankDay("13-12-2022"));
        monthOne.put(LocalDate.parse("14-12-2022", pattern), makeBlankDay("14-12-2022"));
        monthOne.put(LocalDate.parse("15-12-2022", pattern), makeBlankDay("15-12-2022"));
        monthOne.put(LocalDate.parse("16-12-2022", pattern), makeBlankDay("16-12-2022"));
        monthOne.put(LocalDate.parse("17-12-2022", pattern), makeBlankDay("17-12-2022"));
        monthOne.put(LocalDate.parse("18-12-2022", pattern), makeBlankDay("18-12-2022"));
        monthOne.put(LocalDate.parse("19-12-2022", pattern), makeBlankDay("19-12-2022"));
        monthOne.put(LocalDate.parse("20-12-2022", pattern), makeBlankDay("20-12-2022"));
        monthOne.put(LocalDate.parse("21-12-2022", pattern), makeBlankDay("21-12-2022"));
        monthOne.put(LocalDate.parse("22-12-2022", pattern), makeBlankDay("22-12-2022"));
        monthOne.put(LocalDate.parse("23-12-2022", pattern), makeBlankDay("23-12-2022"));
        monthOne.put(LocalDate.parse("24-12-2022", pattern), makeBlankDay("24-12-2022"));
        monthOne.put(LocalDate.parse("25-12-2022", pattern), makeBlankDay("25-12-2022"));
        monthOne.put(LocalDate.parse("26-12-2022", pattern), makeBlankDay("26-12-2022"));
        monthOne.put(LocalDate.parse("27-12-2022", pattern), makeBlankDay("27-12-2022"));
        monthOne.put(LocalDate.parse("28-12-2022", pattern), makeBlankDay("28-12-2022"));
        monthOne.put(LocalDate.parse(startDate, pattern), timetablePerDay.get(LocalDate.parse(startDate, pattern)));
        monthOne.put(LocalDate.parse(firstDate, pattern), timetablePerDay.get(LocalDate.parse(firstDate, pattern)));
        monthOne.put(LocalDate.parse("31-12-2022", pattern),
                timetablePerDay.get(LocalDate.parse("31-12-2022", pattern)));
        monthTwo.put(LocalDate.parse(secondDate, pattern), timetablePerDay.get(LocalDate.parse(secondDate, pattern)));
        monthTwo.put(LocalDate.parse(finishDate, pattern), timetablePerDay.get(LocalDate.parse(finishDate, pattern)));
        monthTwo.put(LocalDate.parse("03-01-2023", pattern), makeBlankDay("03-01-2023"));
        monthTwo.put(LocalDate.parse("04-01-2023", pattern), makeBlankDay("04-01-2023"));
        monthTwo.put(LocalDate.parse("05-01-2023", pattern), makeBlankDay("05-01-2023"));
        monthTwo.put(LocalDate.parse("06-01-2023", pattern), makeBlankDay("06-01-2023"));
        monthTwo.put(LocalDate.parse("07-01-2023", pattern), makeBlankDay("07-01-2023"));
        monthTwo.put(LocalDate.parse("08-01-2023", pattern), makeBlankDay("08-01-2023"));
        monthTwo.put(LocalDate.parse("09-01-2023", pattern), makeBlankDay("09-01-2023"));
        monthTwo.put(LocalDate.parse("10-01-2023", pattern), makeBlankDay("10-01-2023"));
        monthTwo.put(LocalDate.parse("11-01-2023", pattern), makeBlankDay("11-01-2023"));
        monthTwo.put(LocalDate.parse("12-01-2023", pattern), makeBlankDay("12-01-2023"));
        monthTwo.put(LocalDate.parse("13-01-2023", pattern), makeBlankDay("13-01-2023"));
        monthTwo.put(LocalDate.parse("14-01-2023", pattern), makeBlankDay("14-01-2023"));
        monthTwo.put(LocalDate.parse("15-01-2023", pattern), makeBlankDay("15-01-2023"));
        monthTwo.put(LocalDate.parse("16-01-2023", pattern), makeBlankDay("16-01-2023"));
        monthTwo.put(LocalDate.parse("17-01-2023", pattern), makeBlankDay("17-01-2023"));
        monthTwo.put(LocalDate.parse("18-01-2023", pattern), makeBlankDay("18-01-2023"));
        monthTwo.put(LocalDate.parse("19-01-2023", pattern), makeBlankDay("19-01-2023"));
        monthTwo.put(LocalDate.parse("20-01-2023", pattern), makeBlankDay("20-01-2023"));
        monthTwo.put(LocalDate.parse("21-01-2023", pattern), makeBlankDay("21-01-2023"));
        monthTwo.put(LocalDate.parse("22-01-2023", pattern), makeBlankDay("22-01-2023"));
        monthTwo.put(LocalDate.parse("23-01-2023", pattern), makeBlankDay("23-01-2023"));
        monthTwo.put(LocalDate.parse("24-01-2023", pattern), makeBlankDay("24-01-2023"));
        monthTwo.put(LocalDate.parse("25-01-2023", pattern), makeBlankDay("25-01-2023"));
        monthTwo.put(LocalDate.parse("26-01-2023", pattern), makeBlankDay("26-01-2023"));
        monthTwo.put(LocalDate.parse("27-01-2023", pattern), makeBlankDay("27-01-2023"));
        monthTwo.put(LocalDate.parse("28-01-2023", pattern), makeBlankDay("28-01-2023"));
        monthTwo.put(LocalDate.parse("29-01-2023", pattern), makeBlankDay("29-01-2023"));
        monthTwo.put(LocalDate.parse("30-01-2023", pattern), makeBlankDay("30-01-2023"));
        monthTwo.put(LocalDate.parse("31-01-2023", pattern), makeBlankDay("31-01-2023"));

        when(adapter.stuffTimetableByOwnername(LocalDate.parse(startDate, pattern),
                LocalDate.parse(finishDate, pattern), ownerId, ownername)).thenReturn(timetablePerDay);
        when(adapter.wrapForMonthView(timetablePerDay)).thenReturn(timetablePerMonth);

        mockMvc.perform(get("/lectures/{ownername}/{id}/{timeview}?startDate={startDate}&finishDate={finishDate}",
                ownername, ownerId, timeview, LocalDate.parse(startDate, pattern), LocalDate.parse(finishDate, pattern))
                .with(csrf()).queryParam("showcase", showcase).queryParam("page", page).queryParam("mode", mode))
                .andDo(print()).andExpect(status().is(200)).andExpect(view().name("/lectures/timetable"))
                .andExpect(content().string(containsString(result.get(0))))
                .andExpect(content().string(containsString("2022-12-29")))
                .andExpect(content().string(containsString("2023-01-02")))
                .andExpect(content().string(containsString(result.get(1))))
                .andExpect(content().string(containsString(result.get(2))))
                .andExpect(content().string(containsString(result.get(3))))
                .andExpect(content().string(containsString(result.get(4))))
                .andExpect(content().string(containsString(result.get(5))))
                .andExpect(content().string(containsString(result.get(6))))
                .andExpect(content().string(containsString(result.get(7))))
                .andExpect(content().string(containsString(result.get(8))))
                .andExpect(content().string(containsString(result.get(9))))
                .andExpect(content().string(containsString(result.get(10))))
                .andExpect(content().string(containsString(result.get(11))))
                .andExpect(content().string(containsString(result.get(12))))
                .andExpect(content().string(containsString(result.get(13))));
    }

    @SuppressWarnings("rawtypes")
    @ParameterizedTest
    @WithAnonymousUser
    @MethodSource("provideShowTimetable_shouldRun_whenFinishDateAndThanStartDateIsOk")
    void testShowTimetable_shouldRun_whenFinishDateAndThanStartDateIsOkAndAnonymous(Long ownerId, String ownername,
            Optional owner, String showcase, String page, String timeview, String mode, List<String> result)
            throws Exception {
        String startDate = "29-12-2022";
        String firstDate = "30-12-2022";
        String secondDate = "01-01-2023";
        String finishDate = "02-01-2023";

        SortedMap<LocalDate, ArrayList<LectureDto>> timetablePerDay = new TreeMap<LocalDate, ArrayList<LectureDto>>();
        timetablePerDay.put(LocalDate.parse(startDate, pattern), makeBlankDay(startDate));
        timetablePerDay.put(LocalDate.parse(firstDate, pattern),
                new ArrayList<>(Arrays.asList(new LectureDto(1, LocalDate.parse(firstDate, pattern), 4L, 2L, 3L, 2L),
                        new LectureDto(2, LocalDate.parse(firstDate, pattern), null, null, null, null),
                        new LectureDto(3, LocalDate.parse(firstDate, pattern), 4L, 2L, 3L, 2L),
                        new LectureDto(4, LocalDate.parse(firstDate, pattern), null, null, null, null),
                        new LectureDto(5, LocalDate.parse(firstDate, pattern), null, null, null, null),
                        new LectureDto(6, LocalDate.parse(firstDate, pattern), null, null, null, null))));
        timetablePerDay.put(LocalDate.parse("31-12-2022", pattern), makeBlankDay("31-12-2022"));
        timetablePerDay.put(LocalDate.parse(secondDate, pattern),
                new ArrayList<>(
                        Arrays.asList(new LectureDto(1, LocalDate.parse(secondDate, pattern), null, null, null, null),
                                new LectureDto(2, LocalDate.parse(secondDate, pattern), null, null, null, null),
                                new LectureDto(3, LocalDate.parse(secondDate, pattern), null, null, null, null),
                                new LectureDto(4, LocalDate.parse(secondDate, pattern), null, null, null, null),
                                new LectureDto(5, LocalDate.parse(secondDate, pattern), 4L, 2L, 3L, 2L),
                                new LectureDto(6, LocalDate.parse(secondDate, pattern), null, null, null, null))));
        timetablePerDay.put(LocalDate.parse(finishDate, pattern), makeBlankDay(finishDate));

        when(adapter.getOwnerByIdByName(ownerId, ownername)).thenReturn(owner);

        when(groupServ.retrieveGroupByStudentId(ownerId)).thenReturn(new Group(2L, "groupName2"));
        when(teacherServ.retrieveAll()).thenReturn(Arrays.asList(new Teacher(1L, "Name1", "Surname1"),
                new Teacher(2L, "Name2", "Surname2"), new Teacher(3L, "Name3", "Surname3")));
        when(groupServ.retrieveAll()).thenReturn(
                Arrays.asList(new Group(1L, "groupName1"), new Group(2L, "groupName2"), new Group(3L, "groupName3")));
        when(courseServ.retrieveAll()).thenReturn(Arrays.asList(new Course(1L, "Course1", "Description1"),
                new Course(2L, "Course2", "Description2"), new Course(3L, "Course3", "Description3"),
                new Course(4L, "Course4", "Description4"), new Course(5L, "Course5", "Description5")));
        when(roomServ.retrieveAll())
                .thenReturn(Arrays.asList(new Room(1L, "address1", 10), new Room(2L, "address2", 20),
                        new Room(3L, "address3", 30), new Room(4L, "address4", 40), new Room(5L, "address5", 50)));
        when(mapperUtil.toDto(new Teacher(1L, "Name1", "Surname1")))
                .thenReturn(new TeacherDto(1L, "Name1", "Surname1"));
        when(mapperUtil.toDto(new Teacher(2L, "Name2", "Surname2")))
                .thenReturn(new TeacherDto(2L, "Name2", "Surname2"));
        when(mapperUtil.toDto(new Teacher(3L, "Name3", "Surname3")))
                .thenReturn(new TeacherDto(3L, "Name3", "Surname3"));
        when(mapperUtil.toDto(new Group(1L, "groupName1"))).thenReturn(new GroupDto(1L, "groupName1"));
        when(mapperUtil.toDto(new Group(2L, "groupName2"))).thenReturn(new GroupDto(2L, "groupName2"));
        when(mapperUtil.toDto(new Group(3L, "groupName3"))).thenReturn(new GroupDto(3L, "groupName3"));
        when(mapperUtil.toDto(new Course(1L, "Course1", "Description1")))
                .thenReturn(new CourseDto(1L, "Course1", "Description1"));
        when(mapperUtil.toDto(new Course(2L, "Course2", "Description2")))
                .thenReturn(new CourseDto(2L, "Course2", "Description2"));
        when(mapperUtil.toDto(new Course(3L, "Course3", "Description3")))
                .thenReturn(new CourseDto(3L, "Course3", "Description3"));
        when(mapperUtil.toDto(new Course(4L, "Course4", "Description4")))
                .thenReturn(new CourseDto(4L, "Course4", "Description4"));
        when(mapperUtil.toDto(new Course(5L, "Course5", "Description5")))
                .thenReturn(new CourseDto(5L, "Course5", "Description5"));
        when(mapperUtil.toDto(new Room(1L, "address1", 10))).thenReturn(new RoomDto(1L, "address1", 10));
        when(mapperUtil.toDto(new Room(2L, "address2", 20))).thenReturn(new RoomDto(2L, "address2", 20));
        when(mapperUtil.toDto(new Room(3L, "address3", 30))).thenReturn(new RoomDto(3L, "address3", 30));
        when(mapperUtil.toDto(new Room(4L, "address4", 40))).thenReturn(new RoomDto(4L, "address4", 40));
        when(mapperUtil.toDto(new Room(5L, "address5", 50))).thenReturn(new RoomDto(5L, "address5", 50));

// new Lecture(null, finishDate, room, teacher, course, group)
        SortedMap<LocalDate, ArrayList<LectureDto>> monthOne = new TreeMap<>();
        SortedMap<LocalDate, ArrayList<LectureDto>> monthTwo = new TreeMap<>();
        List<SortedMap<LocalDate, ArrayList<LectureDto>>> timetablePerMonth = Arrays.asList(monthOne, monthTwo);
        monthOne.put(LocalDate.parse("01-12-2022", pattern), makeBlankDay("01-12-2022"));
        monthOne.put(LocalDate.parse("02-12-2022", pattern), makeBlankDay("02-12-2022"));
        monthOne.put(LocalDate.parse("03-12-2022", pattern), makeBlankDay("03-12-2022"));
        monthOne.put(LocalDate.parse("04-12-2022", pattern), makeBlankDay("04-12-2022"));
        monthOne.put(LocalDate.parse("05-12-2022", pattern), makeBlankDay("05-12-2022"));
        monthOne.put(LocalDate.parse("06-12-2022", pattern), makeBlankDay("06-12-2022"));
        monthOne.put(LocalDate.parse("07-12-2022", pattern), makeBlankDay("07-12-2022"));
        monthOne.put(LocalDate.parse("08-12-2022", pattern), makeBlankDay("08-12-2022"));
        monthOne.put(LocalDate.parse("09-12-2022", pattern), makeBlankDay("09-12-2022"));
        monthOne.put(LocalDate.parse("10-12-2022", pattern), makeBlankDay("10-12-2022"));
        monthOne.put(LocalDate.parse("11-12-2022", pattern), makeBlankDay("11-12-2022"));
        monthOne.put(LocalDate.parse("12-12-2022", pattern), makeBlankDay("12-12-2022"));
        monthOne.put(LocalDate.parse("13-12-2022", pattern), makeBlankDay("13-12-2022"));
        monthOne.put(LocalDate.parse("14-12-2022", pattern), makeBlankDay("14-12-2022"));
        monthOne.put(LocalDate.parse("15-12-2022", pattern), makeBlankDay("15-12-2022"));
        monthOne.put(LocalDate.parse("16-12-2022", pattern), makeBlankDay("16-12-2022"));
        monthOne.put(LocalDate.parse("17-12-2022", pattern), makeBlankDay("17-12-2022"));
        monthOne.put(LocalDate.parse("18-12-2022", pattern), makeBlankDay("18-12-2022"));
        monthOne.put(LocalDate.parse("19-12-2022", pattern), makeBlankDay("19-12-2022"));
        monthOne.put(LocalDate.parse("20-12-2022", pattern), makeBlankDay("20-12-2022"));
        monthOne.put(LocalDate.parse("21-12-2022", pattern), makeBlankDay("21-12-2022"));
        monthOne.put(LocalDate.parse("22-12-2022", pattern), makeBlankDay("22-12-2022"));
        monthOne.put(LocalDate.parse("23-12-2022", pattern), makeBlankDay("23-12-2022"));
        monthOne.put(LocalDate.parse("24-12-2022", pattern), makeBlankDay("24-12-2022"));
        monthOne.put(LocalDate.parse("25-12-2022", pattern), makeBlankDay("25-12-2022"));
        monthOne.put(LocalDate.parse("26-12-2022", pattern), makeBlankDay("26-12-2022"));
        monthOne.put(LocalDate.parse("27-12-2022", pattern), makeBlankDay("27-12-2022"));
        monthOne.put(LocalDate.parse("28-12-2022", pattern), makeBlankDay("28-12-2022"));
        monthOne.put(LocalDate.parse(startDate, pattern), timetablePerDay.get(LocalDate.parse(startDate, pattern)));
        monthOne.put(LocalDate.parse(firstDate, pattern), timetablePerDay.get(LocalDate.parse(firstDate, pattern)));
        monthOne.put(LocalDate.parse("31-12-2022", pattern),
                timetablePerDay.get(LocalDate.parse("31-12-2022", pattern)));
        monthTwo.put(LocalDate.parse(secondDate, pattern), timetablePerDay.get(LocalDate.parse(secondDate, pattern)));
        monthTwo.put(LocalDate.parse(finishDate, pattern), timetablePerDay.get(LocalDate.parse(finishDate, pattern)));
        monthTwo.put(LocalDate.parse("03-01-2023", pattern), makeBlankDay("03-01-2023"));
        monthTwo.put(LocalDate.parse("04-01-2023", pattern), makeBlankDay("04-01-2023"));
        monthTwo.put(LocalDate.parse("05-01-2023", pattern), makeBlankDay("05-01-2023"));
        monthTwo.put(LocalDate.parse("06-01-2023", pattern), makeBlankDay("06-01-2023"));
        monthTwo.put(LocalDate.parse("07-01-2023", pattern), makeBlankDay("07-01-2023"));
        monthTwo.put(LocalDate.parse("08-01-2023", pattern), makeBlankDay("08-01-2023"));
        monthTwo.put(LocalDate.parse("09-01-2023", pattern), makeBlankDay("09-01-2023"));
        monthTwo.put(LocalDate.parse("10-01-2023", pattern), makeBlankDay("10-01-2023"));
        monthTwo.put(LocalDate.parse("11-01-2023", pattern), makeBlankDay("11-01-2023"));
        monthTwo.put(LocalDate.parse("12-01-2023", pattern), makeBlankDay("12-01-2023"));
        monthTwo.put(LocalDate.parse("13-01-2023", pattern), makeBlankDay("13-01-2023"));
        monthTwo.put(LocalDate.parse("14-01-2023", pattern), makeBlankDay("14-01-2023"));
        monthTwo.put(LocalDate.parse("15-01-2023", pattern), makeBlankDay("15-01-2023"));
        monthTwo.put(LocalDate.parse("16-01-2023", pattern), makeBlankDay("16-01-2023"));
        monthTwo.put(LocalDate.parse("17-01-2023", pattern), makeBlankDay("17-01-2023"));
        monthTwo.put(LocalDate.parse("18-01-2023", pattern), makeBlankDay("18-01-2023"));
        monthTwo.put(LocalDate.parse("19-01-2023", pattern), makeBlankDay("19-01-2023"));
        monthTwo.put(LocalDate.parse("20-01-2023", pattern), makeBlankDay("20-01-2023"));
        monthTwo.put(LocalDate.parse("21-01-2023", pattern), makeBlankDay("21-01-2023"));
        monthTwo.put(LocalDate.parse("22-01-2023", pattern), makeBlankDay("22-01-2023"));
        monthTwo.put(LocalDate.parse("23-01-2023", pattern), makeBlankDay("23-01-2023"));
        monthTwo.put(LocalDate.parse("24-01-2023", pattern), makeBlankDay("24-01-2023"));
        monthTwo.put(LocalDate.parse("25-01-2023", pattern), makeBlankDay("25-01-2023"));
        monthTwo.put(LocalDate.parse("26-01-2023", pattern), makeBlankDay("26-01-2023"));
        monthTwo.put(LocalDate.parse("27-01-2023", pattern), makeBlankDay("27-01-2023"));
        monthTwo.put(LocalDate.parse("28-01-2023", pattern), makeBlankDay("28-01-2023"));
        monthTwo.put(LocalDate.parse("29-01-2023", pattern), makeBlankDay("29-01-2023"));
        monthTwo.put(LocalDate.parse("30-01-2023", pattern), makeBlankDay("30-01-2023"));
        monthTwo.put(LocalDate.parse("31-01-2023", pattern), makeBlankDay("31-01-2023"));

        when(adapter.stuffTimetableByOwnername(LocalDate.parse(startDate, pattern),
                LocalDate.parse(finishDate, pattern), ownerId, ownername)).thenReturn(timetablePerDay);
        when(adapter.wrapForMonthView(timetablePerDay)).thenReturn(timetablePerMonth);

        mockMvc.perform(get("/lectures/{ownername}/{id}/{timeview}?startDate={startDate}&finishDate={finishDate}",
                ownername, ownerId, timeview, LocalDate.parse(startDate, pattern), LocalDate.parse(finishDate, pattern))
                .with(csrf()).queryParam("showcase", showcase).queryParam("page", page).queryParam("mode", mode))
                .andDo(print()).andExpect(status().is(401));
    }

    private static Stream<Arguments> provideShowTimetable_shouldRun_whenFinishDateAndThanStartDateIsOk() {
        String invertStartDate = "2022-12-29";
        String invertFirstDate = "2022-12-30";
        String invertSecondDate = "2023-01-01";
        String invertFinishDate = "2023-01-02";
//(Long ownerId, String ownername, Optional owner, String showcase, String page, String timeview, String mode, List<String> result)
        return Stream.of(
                Arguments.of(2L, "group", Optional.of(new Group(2L, "groupName2")), "", "", "month", "",
                        Arrays.asList("groupName2", invertFirstDate, "1", "Course3", "2", "3", "Course3", "4", "5", "6",
                                "", "", "", "")),
                Arguments.of(2L, "group", Optional.of(new Group(2L, "groupName2")), "course", "", "month", "",
                        Arrays.asList("groupName2", invertFirstDate, "1", "Course3", "2", "3", "Course3", "4", "5", "6",
                                "", "", "", "")),
                Arguments.of(2L, "group", Optional.of(new Group(2L, "groupName2")), "room", "", "month", "",
                        Arrays.asList("groupName2", invertFirstDate, "1", "address4/40", "2", "3", "address4/40", "4",
                                "5", "6", "", "", "", "")),
                Arguments.of(2L, "group", Optional.of(new Group(2L, "groupName2")), "group", "", "month", "",
                        Arrays.asList("groupName2", invertFirstDate, "1", "groupName2", "2", "3", "groupName2", "4",
                                "5", "6", "", "", "", "")),
                Arguments.of(2L, "group", Optional.of(new Group(2L, "groupName2")), "teacher", "", "month", "",
                        Arrays.asList("groupName2", invertFirstDate, "1", "N.Surname2", "2", "3", "N.Surname2", "4",
                                "5", "6", "", "", "", "")),

                Arguments.of(2L, "group", Optional.of(new Group(2L, "groupName2")), "", "2", "month", "",
                        Arrays.asList("groupName2", invertSecondDate, "1", "2", "3", "4", "5", "Course3", "6", "", "",
                                "", "", "")),
                Arguments.of(2L, "group", Optional.of(new Group(2L, "groupName2")), "course", "2", "month", "",
                        Arrays.asList("groupName2", invertSecondDate, "1", "2", "3", "4", "5", "Course3", "6", "", "",
                                "", "", "")),
                Arguments.of(2L, "group", Optional.of(new Group(2L, "groupName2")), "room", "2", "month", "",
                        Arrays.asList("groupName2", invertSecondDate, "1", "2", "3", "4", "5", "address4/40", "6", "",
                                "", "", "", "")),
                Arguments.of(2L, "group", Optional.of(new Group(2L, "groupName2")), "group", "2", "month", "",
                        Arrays.asList("groupName2", invertSecondDate, "1", "2", "3", "4", "5", "groupName2", "6", "",
                                "", "", "", "")),
                Arguments.of(2L, "group", Optional.of(new Group(2L, "groupName2")), "teacher", "2", "month", "",
                        Arrays.asList("groupName2", invertSecondDate, "1", "2", "3", "4", "5", "N.Surname2", "6", "",
                                "", "", "", "")),

                Arguments.of(2L, "group", Optional.of(new Group(2L, "groupName2")), "", "", "day", "viewer",
                        Arrays.asList("groupName2", invertStartDate, "1", "2", "3", "4", "5", "6", "", "", "", "", "",
                                "")),
                Arguments.of(2L, "group", Optional.of(new Group(2L, "groupName2")), "", "1", "day", "viewer",
                        Arrays.asList("groupName2", invertStartDate, "1", "2", "3", "4", "5", "6", "", "", "", "", "",
                                "")),
                Arguments.of(2L, "group", Optional.of(new Group(2L, "groupName2")), "", "2", "day", "viewer",
                        Arrays.asList("groupName2", invertFirstDate, "1", "Course3", "N.Surname2", "address4/40", "2",
                                "3", "Course3", "N.Surname2", "address4/40", "4", "5", "6")),
                Arguments.of(2L, "group", Optional.of(new Group(2L, "groupName2")), "", "3", "day", "viewer",
                        Arrays.asList("groupName2", "2022-12-31", "1", "2", "3", "4", "5", "6", "", "", "", "", "",
                                "")),
                Arguments.of(2L, "group", Optional.of(new Group(2L, "groupName2")), "", "4", "day", "viewer",
                        Arrays.asList("groupName2", invertSecondDate, "1", "2", "3", "4", "5", "Course3", "N.Surname2",
                                "address4/40", "6", "", "", "")),
                Arguments.of(2L, "group", Optional.of(new Group(2L, "groupName2")), "", "5", "day", "viewer",
                        Arrays.asList("groupName2", invertFinishDate, "1", "2", "3", "4", "5", "6", "", "", "", "", "",
                                "")),

                Arguments.of(2L, "group", Optional.of(new Group(2L, "groupName2")), "", "", "day", "creater",
                        Arrays.asList("groupName2", invertStartDate, "12/29/22 at 1 put your lecture here",
                                "12/29/22 at 2 put your lecture here", "12/29/22 at 3 put your lecture here",
                                "12/29/22 at 4 put your lecture here", "12/29/22 at 5 put your lecture here",
                                "12/29/22 at 6 put your lecture here", "", "", "", "", "", "")),
                Arguments.of(2L, "group", Optional.of(new Group(2L, "groupName2")), "", "1", "day", "creater",
                        Arrays.asList("groupName2", invertStartDate, "12/29/22 at 1 put your lecture here",
                                "12/29/22 at 2 put your lecture here", "12/29/22 at 3 put your lecture here",
                                "12/29/22 at 4 put your lecture here", "12/29/22 at 5 put your lecture here",
                                "12/29/22 at 6 put your lecture here", "", "", "", "", "", "")),
                Arguments.of(2L, "group", Optional.of(new Group(2L, "groupName2")), "", "2", "day", "creater",
                        Arrays.asList("groupName2", invertFirstDate, "12/30/22 at 1", "Course3", "N.Surname2",
                                "address4/40", "delete", "12/30/22 at 2 put your lecture here", "12/30/22 at 3",
                                "Course3", "N.Surname2", "address4/40", "delete",
                                "12/30/22 at 4 put your lecture here")),
                Arguments.of(2L, "group", Optional.of(new Group(2L, "groupName2")), "", "3", "day", "creater",
                        Arrays.asList("groupName2", "2022-12-31", "12/31/22 at 1 put your lecture here",
                                "12/31/22 at 2 put your lecture here", "12/31/22 at 3 put your lecture here",
                                "12/31/22 at 4 put your lecture here", "12/31/22 at 5 put your lecture here",
                                "12/31/22 at 6 put your lecture here", "", "", "", "", "", "")),
                Arguments.of(2L, "group", Optional.of(new Group(2L, "groupName2")), "", "4", "day", "creater",
                        Arrays.asList("groupName2", invertSecondDate, "1/1/23 at 1 put your lecture here",
                                "1/1/23 at 2 put your lecture here", "1/1/23 at 3 put your lecture here",
                                "1/1/23 at 4 put your lecture here", "1/1/23 at 5", "Course3", "N.Surname2",
                                "address4/40", "delete", "1/1/23 at 6 put your lecture here", "", "")),
                Arguments.of(2L, "group", Optional.of(new Group(2L, "groupName2")), "", "5", "day", "creater",
                        Arrays.asList("groupName2", invertFinishDate, "1/2/23 at 1 put your lecture here",
                                "1/2/23 at 2 put your lecture here", "1/2/23 at 3 put your lecture here",
                                "1/2/23 at 4 put your lecture here", "1/2/23 at 5 put your lecture here",
                                "1/2/23 at 6 put your lecture here", "", "", "", "", "", "")),

                Arguments.of(999L, "student", Optional.of(new Group(2L, "groupName2")), "", "", "month", "",
                        Arrays.asList("groupName2", invertFirstDate, "1", "Course3", "2", "3", "Course3", "4", "5", "6",
                                "", "", "", "")),
                Arguments.of(999L, "student", Optional.of(new Group(2L, "groupName2")), "course", "", "month", "",
                        Arrays.asList("groupName2", invertFirstDate, "1", "Course3", "2", "3", "Course3", "4", "5", "6",
                                "", "", "", "")),
                Arguments.of(999L, "student", Optional.of(new Group(2L, "groupName2")), "room", "", "month", "",
                        Arrays.asList("groupName2", invertFirstDate, "1", "address4/40", "2", "3", "address4/40", "4",
                                "5", "6", "", "", "", "")),
                Arguments.of(999L, "student", Optional.of(new Group(2L, "groupName2")), "group", "", "month", "",
                        Arrays.asList("groupName2", invertFirstDate, "1", "groupName2", "2", "3", "groupName2", "4",
                                "5", "6", "", "", "", "")),
                Arguments.of(999L, "student", Optional.of(new Group(2L, "groupName2")), "teacher", "", "month", "",
                        Arrays.asList("groupName2", invertFirstDate, "1", "N.Surname2", "2", "3", "N.Surname2", "4",
                                "5", "6", "", "", "", "")),

                Arguments.of(999L, "student", Optional.of(new Group(2L, "groupName2")), "", "2", "month", "",
                        Arrays.asList("groupName2", invertSecondDate, "1", "2", "3", "4", "5", "Course3", "6", "", "",
                                "", "", "")),
                Arguments.of(999L, "student", Optional.of(new Group(2L, "groupName2")), "course", "2", "month", "",
                        Arrays.asList("groupName2", invertSecondDate, "1", "2", "3", "4", "5", "Course3", "6", "", "",
                                "", "", "")),
                Arguments.of(999L, "student", Optional.of(new Group(2L, "groupName2")), "room", "2", "month", "",
                        Arrays.asList("groupName2", invertSecondDate, "1", "2", "3", "4", "5", "address4/40", "6", "",
                                "", "", "", "")),
                Arguments.of(999L, "student", Optional.of(new Group(2L, "groupName2")), "group", "2", "month", "",
                        Arrays.asList("groupName2", invertSecondDate, "1", "2", "3", "4", "5", "groupName2", "6", "",
                                "", "", "", "")),
                Arguments.of(999L, "student", Optional.of(new Group(2L, "groupName2")), "teacher", "2", "month", "",
                        Arrays.asList("groupName2", invertSecondDate, "1", "2", "3", "4", "5", "N.Surname2", "6", "",
                                "", "", "", "")),

                Arguments.of(999L, "student", Optional.of(new Group(2L, "groupName2")), "", "", "day", "viewer",
                        Arrays.asList("groupName2", invertStartDate, "1", "2", "3", "4", "5", "6", "", "", "", "", "",
                                "")),
                Arguments.of(999L, "student", Optional.of(new Group(2L, "groupName2")), "", "1", "day", "viewer",
                        Arrays.asList("groupName2", invertStartDate, "1", "2", "3", "4", "5", "6", "", "", "", "", "",
                                "")),
                Arguments.of(999L, "student", Optional.of(new Group(2L, "groupName2")), "", "2", "day", "viewer",
                        Arrays.asList("groupName2", invertFirstDate, "1", "Course3", "N.Surname2", "address4/40", "2",
                                "3", "Course3", "N.Surname2", "address4/40", "4", "5", "6")),
                Arguments.of(999L, "student", Optional.of(new Group(2L, "groupName2")), "", "3", "day", "viewer",
                        Arrays.asList("groupName2", "2022-12-31", "1", "2", "3", "4", "5", "6", "", "", "", "", "",
                                "")),
                Arguments.of(999L, "student", Optional.of(new Group(2L, "groupName2")), "", "4", "day", "viewer",
                        Arrays.asList("groupName2", invertSecondDate, "1", "2", "3", "4", "5", "Course3", "N.Surname2",
                                "address4/40", "6", "", "", "")),
                Arguments.of(999L, "student", Optional.of(new Group(2L, "groupName2")), "", "5", "day", "viewer",
                        Arrays.asList("groupName2", invertFinishDate, "1", "2", "3", "4", "5", "6", "", "", "", "", "",
                                "")),

                Arguments.of(999L, "student", Optional.of(new Group(2L, "groupName2")), "", "", "day", "creater",
                        Arrays.asList("groupName2", invertStartDate, "12/29/22 at 1 put your lecture here",
                                "12/29/22 at 2 put your lecture here", "12/29/22 at 3 put your lecture here",
                                "12/29/22 at 4 put your lecture here", "12/29/22 at 5 put your lecture here",
                                "12/29/22 at 6 put your lecture here", "", "", "", "", "", "")),
                Arguments.of(999L, "student", Optional.of(new Group(2L, "groupName2")), "", "1", "day", "creater",
                        Arrays.asList("groupName2", invertStartDate, "12/29/22 at 1 put your lecture here",
                                "12/29/22 at 2 put your lecture here", "12/29/22 at 3 put your lecture here",
                                "12/29/22 at 4 put your lecture here", "12/29/22 at 5 put your lecture here",
                                "12/29/22 at 6 put your lecture here", "", "", "", "", "", "")),
                Arguments.of(999L, "student", Optional.of(new Group(2L, "groupName2")), "", "2", "day", "creater",
                        Arrays.asList("groupName2", invertFirstDate, "12/30/22 at 1", "Course3", "N.Surname2",
                                "address4/40", "delete", "12/30/22 at 2 put your lecture here", "12/30/22 at 3",
                                "Course3", "N.Surname2", "address4/40", "delete",
                                "12/30/22 at 4 put your lecture here")),
                Arguments.of(999L, "student", Optional.of(new Group(2L, "groupName2")), "", "3", "day", "creater",
                        Arrays.asList("groupName2", "2022-12-31", "12/31/22 at 1 put your lecture here",
                                "12/31/22 at 2 put your lecture here", "12/31/22 at 3 put your lecture here",
                                "12/31/22 at 4 put your lecture here", "12/31/22 at 5 put your lecture here",
                                "12/31/22 at 6 put your lecture here", "", "", "", "", "", "")),
                Arguments.of(999L, "student", Optional.of(new Group(2L, "groupName2")), "", "4", "day", "creater",
                        Arrays.asList("groupName2", invertSecondDate, "1/1/23 at 1 put your lecture here",
                                "1/1/23 at 2 put your lecture here", "1/1/23 at 3 put your lecture here",
                                "1/1/23 at 4 put your lecture here", "1/1/23 at 5", "Course3", "N.Surname2",
                                "address4/40", "delete", "1/1/23 at 6 put your lecture here", "", "")),
                Arguments.of(999L, "student", Optional.of(new Group(2L, "groupName2")), "", "5", "day", "creater",
                        Arrays.asList("groupName2", invertFinishDate, "1/2/23 at 1 put your lecture here",
                                "1/2/23 at 2 put your lecture here", "1/2/23 at 3 put your lecture here",
                                "1/2/23 at 4 put your lecture here", "1/2/23 at 5 put your lecture here",
                                "1/2/23 at 6 put your lecture here", "", "", "", "", "", "")),

                Arguments.of(2L, "teacher", Optional.of(new Teacher(2L, "Name2", "Surname2")), "", "", "month", "",
                        Arrays.asList("N.Surname2", invertFirstDate, "1", "Course3", "2", "3", "Course3", "4", "5", "6",
                                "", "", "", "")),
                Arguments.of(2L, "teacher", Optional.of(new Teacher(2L, "Name2", "Surname2")), "course", "", "month",
                        "",
                        Arrays.asList("N.Surname2", invertFirstDate, "1", "Course3", "2", "3", "Course3", "4", "5", "6",
                                "", "", "", "")),
                Arguments.of(2L, "teacher", Optional.of(new Teacher(2L, "Name2", "Surname2")), "room", "", "month", "",
                        Arrays.asList("N.Surname2", invertFirstDate, "1", "address4/40", "2", "3", "address4/40", "4",
                                "5", "6", "", "", "", "")),
                Arguments.of(2L, "teacher", Optional.of(new Teacher(2L, "Name2", "Surname2")), "group", "", "month", "",
                        Arrays.asList("N.Surname2", invertFirstDate, "1", "groupName2", "2", "3", "groupName2", "4",
                                "5", "6", "", "", "", "")),
                Arguments.of(2L, "teacher", Optional.of(new Teacher(2L, "Name2", "Surname2")), "teacher", "", "month",
                        "",
                        Arrays.asList("N.Surname2", invertFirstDate, "1", "N.Surname2", "2", "3", "N.Surname2", "4",
                                "5", "6", "", "", "", "")),

                Arguments.of(2L, "teacher", Optional.of(new Teacher(2L, "Name2", "Surname2")), "", "2", "month", "",
                        Arrays.asList("N.Surname2", invertSecondDate, "1", "2", "3", "4", "5", "Course3", "6", "", "",
                                "", "", "")),
                Arguments.of(2L, "teacher", Optional.of(new Teacher(2L, "Name2", "Surname2")), "course", "2", "month",
                        "",
                        Arrays.asList("N.Surname2", invertSecondDate, "1", "2", "3", "4", "5", "Course3", "6", "", "",
                                "", "", "")),
                Arguments.of(2L, "teacher", Optional.of(new Teacher(2L, "Name2", "Surname2")), "room", "2", "month", "",
                        Arrays.asList("N.Surname2", invertSecondDate, "1", "2", "3", "4", "5", "address4/40", "6", "",
                                "", "", "", "")),
                Arguments.of(2L, "teacher", Optional.of(new Teacher(2L, "Name2", "Surname2")), "group", "2", "month",
                        "",
                        Arrays.asList("N.Surname2", invertSecondDate, "1", "2", "3", "4", "", "5", "groupName2", "6",
                                "", "", "", "", "")),
                Arguments.of(2L, "teacher", Optional.of(new Teacher(2L, "Name2", "Surname2")), "teacher", "2", "month",
                        "",
                        Arrays.asList("N.Surname2", invertSecondDate, "1", "2", "3", "4", "5", "N.Surname2", "6", "",
                                "", "", "", "")),

                Arguments.of(2L, "teacher", Optional.of(new Teacher(2L, "Name2", "Surname2")), "", "", "day", "viewer",
                        Arrays.asList("N.Surname2", invertStartDate, "1", "2", "3", "4", "5", "6", "", "", "", "", "",
                                "")),
                Arguments.of(2L, "teacher", Optional.of(new Teacher(2L, "Name2", "Surname2")), "", "1", "day", "viewer",
                        Arrays.asList("N.Surname2", invertStartDate, "1", "2", "3", "4", "5", "6", "", "", "", "", "",
                                "")),
                Arguments.of(2L, "teacher", Optional.of(new Teacher(2L, "Name2", "Surname2")), "", "2", "day", "viewer",
                        Arrays.asList("N.Surname2", invertFirstDate, "1", "Course3", "groupName2", "address4/40", "2",
                                "3", "Course3", "N.Surname2", "address4/40", "4", "5", "6")),
                Arguments.of(2L, "teacher", Optional.of(new Teacher(2L, "Name2", "Surname2")), "", "3", "day", "viewer",
                        Arrays.asList("N.Surname2", "2022-12-31", "1", "2", "3", "4", "5", "6", "", "", "", "", "",
                                "")),
                Arguments.of(2L, "teacher", Optional.of(new Teacher(2L, "Name2", "Surname2")), "", "4", "day", "viewer",
                        Arrays.asList("N.Surname2", invertSecondDate, "1", "2", "3", "4", "5", "Course3", "groupName2",
                                "address4/40", "6", "", "", "")),
                Arguments.of(2L, "teacher", Optional.of(new Teacher(2L, "Name2", "Surname2")), "", "5", "day", "viewer",
                        Arrays.asList("N.Surname2", invertFinishDate, "1", "2", "3", "4", "5", "6", "", "", "", "", "",
                                "")),

                Arguments.of(2L, "teacher", Optional.of(new Teacher(2L, "Name2", "Surname2")), "", "", "day", "creater",
                        Arrays.asList("N.Surname2", invertStartDate, "12/29/22 at 1 put your lecture here",
                                "12/29/22 at 2 put your lecture here", "12/29/22 at 3 put your lecture here",
                                "12/29/22 at 4 put your lecture here", "12/29/22 at 5 put your lecture here",
                                "12/29/22 at 6 put your lecture here", "", "", "", "", "", "")),
                Arguments.of(2L, "teacher", Optional.of(new Teacher(2L, "Name2", "Surname2")), "", "1", "day",
                        "creater",
                        Arrays.asList("N.Surname2", invertStartDate, "12/29/22 at 1 put your lecture here",
                                "12/29/22 at 2 put your lecture here", "12/29/22 at 3 put your lecture here",
                                "12/29/22 at 4 put your lecture here", "12/29/22 at 5 put your lecture here",
                                "12/29/22 at 6 put your lecture here", "", "", "", "", "", "")),
                Arguments.of(2L, "teacher", Optional.of(new Teacher(2L, "Name2", "Surname2")), "", "2", "day",
                        "creater",
                        Arrays.asList("N.Surname2", invertFirstDate, "12/30/22 at 1", "Course3", "N.Surname2",
                                "address4/40", "delete", "12/30/22 at 2 put your lecture here", "12/30/22 at 3",
                                "Course3", "N.Surname2", "address4/40", "delete",
                                "12/30/22 at 4 put your lecture here")),
                Arguments.of(2L, "teacher", Optional.of(new Teacher(2L, "Name2", "Surname2")), "", "3", "day",
                        "creater",
                        Arrays.asList("N.Surname2", "2022-12-31", "12/31/22 at 1 put your lecture here",
                                "12/31/22 at 2 put your lecture here", "12/31/22 at 3 put your lecture here",
                                "12/31/22 at 4 put your lecture here", "12/31/22 at 5 put your lecture here",
                                "12/31/22 at 6 put your lecture here", "", "", "", "", "", "")),
                Arguments.of(2L, "teacher", Optional.of(new Teacher(2L, "Name2", "Surname2")), "", "4", "day",
                        "creater",
                        Arrays.asList("N.Surname2", invertSecondDate, "1/1/23 at 1 put your lecture here",
                                "1/1/23 at 2 put your lecture here", "1/1/23 at 3 put your lecture here",
                                "1/1/23 at 4 put your lecture here", "1/1/23 at 5", "Course3", "N.Surname2",
                                "address4/40", "delete", "1/1/23 at 6 put your lecture here", "", "")),
                Arguments.of(2L, "teacher", Optional.of(new Teacher(2L, "Name2", "Surname2")), "", "5", "day",
                        "creater",
                        Arrays.asList("N.Surname2", invertFinishDate, "1/2/23 at 1 put your lecture here",
                                "1/2/23 at 2 put your lecture here", "1/2/23 at 3 put your lecture here",
                                "1/2/23 at 4 put your lecture here", "1/2/23 at 5 put your lecture here",
                                "1/2/23 at 6 put your lecture here", "", "", "", "", "", "")),

                Arguments.of(4L, "room", Optional.of(new Room(4L, "address4", 40)), "", "", "month", "",
                        Arrays.asList("address4/40", invertFirstDate, "1", "Course3", "2", "3", "Course3", "4", "5",
                                "6", "", "", "", "")),
                Arguments.of(4L, "room", Optional.of(new Room(4L, "address4", 40)), "course", "", "month", "",
                        Arrays.asList("address4/40", invertFirstDate, "1", "Course3", "2", "3", "Course3", "4", "5",
                                "6", "", "", "", "")),
                Arguments.of(4L, "room", Optional.of(new Room(4L, "address4", 40)), "room", "", "month", "",
                        Arrays.asList("address4/40", invertFirstDate, "1", "address4/40", "2", "3", "address4/40", "4",
                                "5", "6", "", "", "", "")),
                Arguments.of(4L, "room", Optional.of(new Room(4L, "address4", 40)), "group", "", "month", "",
                        Arrays.asList("address4/40", invertFirstDate, "1", "groupName2", "2", "3", "groupName2", "4",
                                "5", "6", "", "", "", "")),
                Arguments.of(4L, "room", Optional.of(new Room(4L, "address4", 40)), "teacher", "", "month", "",
                        Arrays.asList("address4/40", invertFirstDate, "1", "N.Surname2", "2", "3", "N.Surname2", "4",
                                "5", "6", "", "", "", "")),

                Arguments.of(4L, "room", Optional.of(new Room(4L, "address4", 40)), "", "2", "month", "",
                        Arrays.asList("address4/40", invertSecondDate, "1", "2", "3", "4", "5", "Course3", "6", "", "",
                                "", "", "")),
                Arguments.of(4L, "room", Optional.of(new Room(4L, "address4", 40)), "course", "2", "month", "",
                        Arrays.asList("address4/40", invertSecondDate, "1", "2", "3", "4", "5", "Course3", "6", "", "",
                                "", "", "")),
                Arguments.of(4L, "room", Optional.of(new Room(4L, "address4", 40)), "room", "2", "month", "",
                        Arrays.asList("address4/40", invertSecondDate, "1", "2", "3", "4", "5", "address4/40", "6", "",
                                "", "", "", "")),
                Arguments.of(4L, "room", Optional.of(new Room(4L, "address4", 40)), "group", "2", "month", "",
                        Arrays.asList("address4/40", invertSecondDate, "1", "2", "3", "4", "5", "groupName2", "6", "",
                                "", "", "", "")),
                Arguments.of(4L, "room", Optional.of(new Room(4L, "address4", 40)), "teacher", "2", "month", "",
                        Arrays.asList("address4/40", invertSecondDate, "1", "2", "3", "4", "5", "N.Surname2", "6", "",
                                "", "", "", "")),

                Arguments.of(4L, "room", Optional.of(new Room(4L, "address4", 40)), "", "", "day", "viewer",
                        Arrays.asList("address4/40", invertStartDate, "1", "2", "3", "4", "5", "6", "", "", "", "", "",
                                "")),
                Arguments.of(4L, "room", Optional.of(new Room(4L, "address4", 40)), "", "1", "day", "viewer",
                        Arrays.asList("address4/40", invertStartDate, "1", "2", "3", "4", "5", "6", "", "", "", "", "",
                                "")),
                Arguments.of(4L, "room", Optional.of(new Room(4L, "address4", 40)), "", "2", "day", "viewer",
                        Arrays.asList("address4/40", invertFirstDate, "1", "groupName2", "Course3", "N.Surname2", "2",
                                "3", "groupName2", "Course3", "N.Surname2", "4", "5", "6")),
                Arguments.of(4L, "room", Optional.of(new Room(4L, "address4", 40)), "", "3", "day", "viewer",
                        Arrays.asList("address4/40", "2022-12-31", "1", "2", "3", "4", "5", "6", "", "", "", "", "",
                                "")),
                Arguments.of(4L, "room", Optional.of(new Room(4L, "address4", 40)), "", "4", "day", "viewer",
                        Arrays.asList("address4/40", invertSecondDate, "1", "2", "3", "4", "5", "groupName2", "Course3",
                                "N.Surname2", "6", "", "", "")),
                Arguments.of(4L, "room", Optional.of(new Room(4L, "address4", 40)), "", "5", "day", "viewer",
                        Arrays.asList("address4/40", invertFinishDate, "1", "2", "3", "4", "5", "6", "", "", "", "", "",
                                "")),

                Arguments.of(4L, "room", Optional.of(new Room(4L, "address4", 40)), "", "", "day", "creater",
                        Arrays.asList("address4/40", invertStartDate, "12/29/22 at 1 put your lecture here",
                                "12/29/22 at 2 put your lecture here", "12/29/22 at 3 put your lecture here",
                                "12/29/22 at 4 put your lecture here", "12/29/22 at 5 put your lecture here",
                                "12/29/22 at 6 put your lecture here", "", "", "", "", "", "")),
                Arguments.of(4L, "room", Optional.of(new Room(4L, "address4", 40)), "", "1", "day", "creater",
                        Arrays.asList("address4/40", invertStartDate, "12/29/22 at 1 put your lecture here",
                                "12/29/22 at 2 put your lecture here", "12/29/22 at 3 put your lecture here",
                                "12/29/22 at 4 put your lecture here", "12/29/22 at 5 put your lecture here",
                                "12/29/22 at 6 put your lecture here", "", "", "", "", "", "")),
                Arguments.of(4L, "room", Optional.of(new Room(4L, "address4", 40)), "", "2", "day", "creater",
                        Arrays.asList("address4/40", invertFirstDate, "12/30/22 at 1", "Course3", "N.Surname2",
                                "address4/40", "delete", "12/30/22 at 2 put your lecture here", "12/30/22 at 3",
                                "Course3", "N.Surname2", "address4/40", "delete",
                                "12/30/22 at 4 put your lecture here")),
                Arguments.of(4L, "room", Optional.of(new Room(4L, "address4", 40)), "", "3", "day", "creater",
                        Arrays.asList("address4/40", "2022-12-31", "12/31/22 at 1 put your lecture here",
                                "12/31/22 at 2 put your lecture here", "12/31/22 at 3 put your lecture here",
                                "12/31/22 at 4 put your lecture here", "12/31/22 at 5 put your lecture here",
                                "12/31/22 at 6 put your lecture here", "", "", "", "", "", "")),
                Arguments.of(4L, "room", Optional.of(new Room(4L, "address4", 40)), "", "4", "day", "creater",
                        Arrays.asList("address4/40", invertSecondDate, "1/1/23 at 1 put your lecture here",
                                "1/1/23 at 2 put your lecture here", "1/1/23 at 3 put your lecture here",
                                "1/1/23 at 4 put your lecture here", "1/1/23 at 5", "Course3", "N.Surname2",
                                "address4/40", "delete", "1/1/23 at 6 put your lecture here", "", "")),
                Arguments.of(4L, "room", Optional.of(new Room(4L, "address4", 40)), "", "5", "day", "creater",
                        Arrays.asList("address4/40", invertFinishDate, "1/2/23 at 1 put your lecture here",
                                "1/2/23 at 2 put your lecture here", "1/2/23 at 3 put your lecture here",
                                "1/2/23 at 4 put your lecture here", "1/2/23 at 5 put your lecture here",
                                "1/2/23 at 6 put your lecture here", "", "", "", "", "", "")));
    }

    private ArrayList<LectureDto> makeBlankDay(String string) {
        return new ArrayList<>(
                Arrays.asList(new LectureDto(1, LocalDate.parse(string, pattern), null, null, null, null),
                        new LectureDto(2, LocalDate.parse(string, pattern), null, null, null, null),
                        new LectureDto(3, LocalDate.parse(string, pattern), null, null, null, null),
                        new LectureDto(4, LocalDate.parse(string, pattern), null, null, null, null),
                        new LectureDto(5, LocalDate.parse(string, pattern), null, null, null, null),
                        new LectureDto(6, LocalDate.parse(string, pattern), null, null, null, null)));
    }

    @ParameterizedTest
    @WithUserDetails("testAdmin")
    @MethodSource("provideStringLectureParts")
    void testCollectLectureParts_shouldAccessed_whenAdmin(String ownername, String substitute, String exit,
            String result) throws Exception {
        LocalDate currentDate = LocalDate.parse("02-01-2022", pattern);
        Long ownerId = 1L;
        Long lectureId = 99L;
        Integer serialNumberPerDay = 1;
        Long groupId = 1L;
        Long courseId = 1L;
        Long teacherId = 1L;
        Long roomId = 1L;

        when(roomServ.retrieveFreeByTime(currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Room(1L, "address", 0)));
        when(courseServ.retrieveFreeByTeacherWithFreeGroupsByTime(teacherId, currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Course(4L, "Course4", "Description4")));
        when(groupServ.retrieveFreeByTeacherByCourseByTime(teacherId, courseId, currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Group(4L, "Group4")));

        when(groupServ.retrieveFreeByTime(currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Group(3L, "Group3")));
        when(courseServ.retrieveFreeByGroupWithFreeTeachersByTime(groupId, currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Course(3L, "Course3", "Description3")));
        when(teacherServ.retrieveFreeByGroupByCourseByTime(groupId, courseId, currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Teacher(3L, "Name3", "Surname3")));

        mockMvc.perform(get("/lectures/new").with(csrf()).queryParam("lectureId", lectureId.toString())
                .queryParam("serialNumberPerDay", serialNumberPerDay.toString())
                .queryParam("teacherId", teacherId.toString()).queryParam("roomId", roomId.toString())
                .queryParam("courseId", courseId.toString()).queryParam("groupId", groupId.toString())
                .queryParam("ownername", ownername).queryParam("ownerId", ownerId.toString())
                .queryParam("startDate", "2022-01-01").queryParam("date", "2022-01-02")
                .queryParam("finishDate", "2022-01-03").queryParam("substitute", substitute)).andDo(print())
                .andExpect(status().is(200)).andExpect(view().name("lectures/" + exit))
                .andExpect(content().string(containsString(result)));
    }

    @ParameterizedTest
    @WithUserDetails("testStaff")
    @MethodSource("provideStringLectureParts")
    void testCollectLectureParts_shouldAccessed_whenStaff(String ownername, String substitute, String exit,
            String result) throws Exception {
        LocalDate currentDate = LocalDate.parse("02-01-2022", pattern);
        Long ownerId = 1L;
        Long lectureId = 99L;
        Integer serialNumberPerDay = 1;
        Long groupId = 1L;
        Long courseId = 1L;
        Long teacherId = 1L;
        Long roomId = 1L;

        when(roomServ.retrieveFreeByTime(currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Room(1L, "address", 0)));
        when(courseServ.retrieveFreeByTeacherWithFreeGroupsByTime(teacherId, currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Course(4L, "Course4", "Description4")));
        when(groupServ.retrieveFreeByTeacherByCourseByTime(teacherId, courseId, currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Group(4L, "Group4")));

        when(groupServ.retrieveFreeByTime(currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Group(3L, "Group3")));
        when(courseServ.retrieveFreeByGroupWithFreeTeachersByTime(groupId, currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Course(3L, "Course3", "Description3")));
        when(teacherServ.retrieveFreeByGroupByCourseByTime(groupId, courseId, currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Teacher(3L, "Name3", "Surname3")));

        mockMvc.perform(get("/lectures/new").with(csrf()).queryParam("lectureId", lectureId.toString())
                .queryParam("serialNumberPerDay", serialNumberPerDay.toString())
                .queryParam("teacherId", teacherId.toString()).queryParam("roomId", roomId.toString())
                .queryParam("courseId", courseId.toString()).queryParam("groupId", groupId.toString())
                .queryParam("ownername", ownername).queryParam("ownerId", ownerId.toString())
                .queryParam("startDate", "2022-01-01").queryParam("date", "2022-01-02")
                .queryParam("finishDate", "2022-01-03").queryParam("substitute", substitute)).andDo(print())
                .andExpect(status().is(200)).andExpect(view().name("lectures/" + exit))
                .andExpect(content().string(containsString(result)));
    }

    @ParameterizedTest
    @WithUserDetails("testStudent")
    @MethodSource("provideStringLectureParts")
    void testCollectLectureParts_shouldDenied_whenStudent(String ownername, String substitute, String exit,
            String result) throws Exception {
        LocalDate currentDate = LocalDate.parse("02-01-2022", pattern);
        Long ownerId = 1L;
        Long lectureId = 99L;
        Integer serialNumberPerDay = 1;
        Long groupId = 1L;
        Long courseId = 1L;
        Long teacherId = 1L;
        Long roomId = 1L;

        when(roomServ.retrieveFreeByTime(currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Room(1L, "address", 0)));
        when(courseServ.retrieveFreeByTeacherWithFreeGroupsByTime(teacherId, currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Course(4L, "Course4", "Description4")));
        when(groupServ.retrieveFreeByTeacherByCourseByTime(teacherId, courseId, currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Group(4L, "Group4")));

        when(groupServ.retrieveFreeByTime(currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Group(3L, "Group3")));
        when(courseServ.retrieveFreeByGroupWithFreeTeachersByTime(groupId, currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Course(3L, "Course3", "Description3")));
        when(teacherServ.retrieveFreeByGroupByCourseByTime(groupId, courseId, currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Teacher(3L, "Name3", "Surname3")));

        mockMvc.perform(get("/lectures/new").with(csrf()).queryParam("lectureId", lectureId.toString())
                .queryParam("serialNumberPerDay", serialNumberPerDay.toString())
                .queryParam("teacherId", teacherId.toString()).queryParam("roomId", roomId.toString())
                .queryParam("courseId", courseId.toString()).queryParam("groupId", groupId.toString())
                .queryParam("ownername", ownername).queryParam("ownerId", ownerId.toString())
                .queryParam("startDate", "2022-01-01").queryParam("date", "2022-01-02")
                .queryParam("finishDate", "2022-01-03").queryParam("substitute", substitute)).andDo(print())
                .andExpect(status().is(403)).andExpect(view().name("error"));

        verify(roomServ, times(0)).retrieveFreeByTime(currentDate, serialNumberPerDay);
        verify(courseServ, times(0)).retrieveFreeByTeacherWithFreeGroupsByTime(teacherId, currentDate,
                serialNumberPerDay);
        verify(groupServ, times(0)).retrieveFreeByTeacherByCourseByTime(teacherId, courseId, currentDate,
                serialNumberPerDay);
        verify(groupServ, times(0)).retrieveFreeByTime(currentDate, serialNumberPerDay);
        verify(courseServ, times(0)).retrieveFreeByGroupWithFreeTeachersByTime(groupId, currentDate,
                serialNumberPerDay);
        verify(teacherServ, times(0)).retrieveFreeByGroupByCourseByTime(groupId, courseId, currentDate,
                serialNumberPerDay);
    }

    @ParameterizedTest
    @WithAnonymousUser
    @MethodSource("provideStringLectureParts")
    void testCollectLectureParts_shouldDenied_whenAnonymous(String ownername, String substitute, String exit,
            String result) throws Exception {
        LocalDate currentDate = LocalDate.parse("02-01-2022", pattern);
        Long ownerId = 1L;
        Long lectureId = 99L;
        Integer serialNumberPerDay = 1;
        Long groupId = 1L;
        Long courseId = 1L;
        Long teacherId = 1L;
        Long roomId = 1L;

        when(roomServ.retrieveFreeByTime(currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Room(1L, "address", 0)));
        when(courseServ.retrieveFreeByTeacherWithFreeGroupsByTime(teacherId, currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Course(4L, "Course4", "Description4")));
        when(groupServ.retrieveFreeByTeacherByCourseByTime(teacherId, courseId, currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Group(4L, "Group4")));

        when(groupServ.retrieveFreeByTime(currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Group(3L, "Group3")));
        when(courseServ.retrieveFreeByGroupWithFreeTeachersByTime(groupId, currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Course(3L, "Course3", "Description3")));
        when(teacherServ.retrieveFreeByGroupByCourseByTime(groupId, courseId, currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Teacher(3L, "Name3", "Surname3")));

        mockMvc.perform(get("/lectures/new").with(csrf()).queryParam("lectureId", lectureId.toString())
                .queryParam("serialNumberPerDay", serialNumberPerDay.toString())
                .queryParam("teacherId", teacherId.toString()).queryParam("roomId", roomId.toString())
                .queryParam("courseId", courseId.toString()).queryParam("groupId", groupId.toString())
                .queryParam("ownername", ownername).queryParam("ownerId", ownerId.toString())
                .queryParam("startDate", "2022-01-01").queryParam("date", "2022-01-02")
                .queryParam("finishDate", "2022-01-03").queryParam("substitute", substitute)).andDo(print())
                .andExpect(status().is(401));

        verify(roomServ, times(0)).retrieveFreeByTime(currentDate, serialNumberPerDay);
        verify(courseServ, times(0)).retrieveFreeByTeacherWithFreeGroupsByTime(teacherId, currentDate,
                serialNumberPerDay);
        verify(groupServ, times(0)).retrieveFreeByTeacherByCourseByTime(teacherId, courseId, currentDate,
                serialNumberPerDay);
        verify(groupServ, times(0)).retrieveFreeByTime(currentDate, serialNumberPerDay);
        verify(courseServ, times(0)).retrieveFreeByGroupWithFreeTeachersByTime(groupId, currentDate,
                serialNumberPerDay);
        verify(teacherServ, times(0)).retrieveFreeByGroupByCourseByTime(groupId, courseId, currentDate,
                serialNumberPerDay);
    }

    private static Stream<Arguments> provideStringLectureParts() {
        /*
         * String ownername, String substitute, String exit, String result) List<String>
         * sequenceSteps = "group".equals(ownername) ? Arrays.asList("dateAndTime",
         * "room", "course", "teacher") : "teacher".equals(ownername) ?
         * Arrays.asList("dateAndTime", "room", "course", "group" ) :
         * "room".equals(ownername) ? Arrays.asList("dateAndTime", "group", "course",
         * "teacher") 5 and 8
         */
        return Stream.of(Arguments.of("group", "dateAndTime", "stringLectureParts", "address/0"),
                Arguments.of("group", "room", "stringLectureParts", "Course3"),
                Arguments.of("group", "course", "modifyLectureParts", "N.Surname3"),

                Arguments.of("teacher", "dateAndTime", "stringLectureParts", "address/0"),
                Arguments.of("teacher", "room", "stringLectureParts", "Course4"),
                Arguments.of("teacher", "course", "modifyLectureParts", "Group4"),

                Arguments.of("room", "dateAndTime", "stringLectureParts", "Group3"),
                Arguments.of("room", "group", "stringLectureParts", "Course3"),
                Arguments.of("room", "course", "modifyLectureParts", "N.Surname3"));
    }

    @ParameterizedTest
    @WithUserDetails("testAdmin")
    @CsvSource({ "'group','course','stringLectureParts','Course3'",
            "'group','teacher','modifyLectureParts','N.Surname2'", "'group','room','modifyLectureParts','address3/30'",
            "'teacher','course','stringLectureParts','Course3'",
            "'teacher','teacher','modifyLectureParts','N.Surname2'",
            "'teacher','room','modifyLectureParts','address3/30'", "'room','course','stringLectureParts','Course3'",
            "'room','teacher','modifyLectureParts','N.Surname2'", "'room','room','modifyLectureParts','address3/30'" })
    void testModify_shouldAccessed_whenAdmin(String ownername, String substitute, String exit, String result)
            throws Exception {
        LocalDate currentDate = LocalDate.parse("02-01-2022", pattern);
        Long lectureId = 99L;
        Integer serialNumberPerDay = 5;
        Long ownerId = 1L;

        when(lectureServ.retrieveLectureById(lectureId))
                .thenReturn(new Lecture(99L, serialNumberPerDay, currentDate, room, teacher, course, group));
        when(courseServ.retrieveFreeByGroupWithFreeTeachersByTime(group.getId(), currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Course(3L, "Course3", "Description3")));
        when(teacherServ.retrieveFreeByTimeForCourse(course.getId(), currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Teacher(2L, "Name2", "Surname2")));
        when(roomServ.retrieveFreeByTime(currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Room(3L, "address3", 30)));
        when(mapperUtil.toDto(new Lecture(99L, serialNumberPerDay, currentDate, room, teacher, course, group)))
                .thenReturn(new LectureDto(99L, serialNumberPerDay, currentDate, room.getId(), teacher.getId(),
                        course.getId(), group.getId()));

        mockMvc.perform(get("/lectures/modify").with(csrf()).queryParam("substitute", substitute)
                .queryParam("lectureId", lectureId.toString())
                .queryParam("serialNumberPerDay", serialNumberPerDay.toString()).queryParam("date", "2022-01-02")
                .queryParam("teacherId", teacher.getId().toString()).queryParam("roomId", room.getId().toString())
                .queryParam("courseId", course.getId().toString()).queryParam("groupId", group.getId().toString())
                .queryParam("ownername", ownername).queryParam("ownerId", ownerId.toString())
                .queryParam("startDate", "2022-01-01").queryParam("finishDate", "2022-01-03")).andDo(print())
                .andExpect(status().is(200)).andExpect(view().name("lectures/" + exit))
                .andExpect(content().string(containsString(result)));
    }

    @ParameterizedTest
    @WithUserDetails("testStaff")
    @CsvSource({ "'group','course','stringLectureParts','Course3'",
            "'group','teacher','modifyLectureParts','N.Surname2'", "'group','room','modifyLectureParts','address3/30'",
            "'teacher','course','stringLectureParts','Course3'",
            "'teacher','teacher','modifyLectureParts','N.Surname2'",
            "'teacher','room','modifyLectureParts','address3/30'", "'room','course','stringLectureParts','Course3'",
            "'room','teacher','modifyLectureParts','N.Surname2'", "'room','room','modifyLectureParts','address3/30'" })
    void testModify_shouldAccessed_whenStaff(String ownername, String substitute, String exit, String result)
            throws Exception {
        LocalDate currentDate = LocalDate.parse("02-01-2022", pattern);
        Long lectureId = 99L;
        Integer serialNumberPerDay = 5;
        Long ownerId = 1L;

        when(lectureServ.retrieveLectureById(lectureId))
                .thenReturn(new Lecture(99L, serialNumberPerDay, currentDate, room, teacher, course, group));
        when(courseServ.retrieveFreeByGroupWithFreeTeachersByTime(group.getId(), currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Course(3L, "Course3", "Description3")));
        when(teacherServ.retrieveFreeByTimeForCourse(course.getId(), currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Teacher(2L, "Name2", "Surname2")));
        when(roomServ.retrieveFreeByTime(currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Room(3L, "address3", 30)));
        when(mapperUtil.toDto(new Lecture(99L, serialNumberPerDay, currentDate, room, teacher, course, group)))
                .thenReturn(new LectureDto(99L, serialNumberPerDay, currentDate, room.getId(), teacher.getId(),
                        course.getId(), group.getId()));

        mockMvc.perform(get("/lectures/modify").with(csrf()).queryParam("substitute", substitute)
                .queryParam("lectureId", lectureId.toString())
                .queryParam("serialNumberPerDay", serialNumberPerDay.toString()).queryParam("date", "2022-01-02")
                .queryParam("teacherId", teacher.getId().toString()).queryParam("roomId", room.getId().toString())
                .queryParam("courseId", course.getId().toString()).queryParam("groupId", group.getId().toString())
                .queryParam("ownername", ownername).queryParam("ownerId", ownerId.toString())
                .queryParam("startDate", "2022-01-01").queryParam("finishDate", "2022-01-03")).andDo(print())
                .andExpect(status().is(200)).andExpect(view().name("lectures/" + exit))
                .andExpect(content().string(containsString(result)));
    }

    @ParameterizedTest
    @WithUserDetails("testStudent")
    @CsvSource({ "'group','course','stringLectureParts','Course3'",
            "'group','teacher','modifyLectureParts','N.Surname2'", "'group','room','modifyLectureParts','address3/30'",
            "'teacher','course','stringLectureParts','Course3'",
            "'teacher','teacher','modifyLectureParts','N.Surname2'",
            "'teacher','room','modifyLectureParts','address3/30'", "'room','course','stringLectureParts','Course3'",
            "'room','teacher','modifyLectureParts','N.Surname2'", "'room','room','modifyLectureParts','address3/30'" })
    void testModify_shouldDenied_whenStudent(String ownername, String substitute, String exit, String result)
            throws Exception {
        LocalDate currentDate = LocalDate.parse("02-01-2022", pattern);
        Long lectureId = 99L;
        Integer serialNumberPerDay = 5;
        Long ownerId = 1L;

        when(lectureServ.retrieveLectureById(lectureId))
                .thenReturn(new Lecture(99L, serialNumberPerDay, currentDate, room, teacher, course, group));
        when(courseServ.retrieveFreeByGroupWithFreeTeachersByTime(group.getId(), currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Course(3L, "Course3", "Description3")));
        when(teacherServ.retrieveFreeByTimeForCourse(course.getId(), currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Teacher(2L, "Name2", "Surname2")));
        when(roomServ.retrieveFreeByTime(currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Room(3L, "address3", 30)));
        when(mapperUtil.toDto(new Lecture(99L, serialNumberPerDay, currentDate, room, teacher, course, group)))
                .thenReturn(new LectureDto(99L, serialNumberPerDay, currentDate, room.getId(), teacher.getId(),
                        course.getId(), group.getId()));

        mockMvc.perform(get("/lectures/modify").with(csrf()).queryParam("substitute", substitute)
                .queryParam("lectureId", lectureId.toString())
                .queryParam("serialNumberPerDay", serialNumberPerDay.toString()).queryParam("date", "2022-01-02")
                .queryParam("teacherId", teacher.getId().toString()).queryParam("roomId", room.getId().toString())
                .queryParam("courseId", course.getId().toString()).queryParam("groupId", group.getId().toString())
                .queryParam("ownername", ownername).queryParam("ownerId", ownerId.toString())
                .queryParam("startDate", "2022-01-01").queryParam("finishDate", "2022-01-03")).andDo(print())
                .andExpect(status().is(403)).andExpect(view().name("error"));

        verify(lectureServ, times(0)).retrieveLectureById(lectureId);
        verify(courseServ, times(0)).retrieveFreeByGroupWithFreeTeachersByTime(group.getId(), currentDate,
                serialNumberPerDay);
        verify(teacherServ, times(0)).retrieveFreeByTimeForCourse(course.getId(), currentDate, serialNumberPerDay);
        verify(roomServ, times(0)).retrieveFreeByTime(currentDate, serialNumberPerDay);
        verify(mapperUtil, times(0))
                .toDto(new Lecture(99L, serialNumberPerDay, currentDate, room, teacher, course, group));
    }

    @ParameterizedTest
    @WithAnonymousUser
    @CsvSource({ "'group','course','stringLectureParts','Course3'",
            "'group','teacher','modifyLectureParts','N.Surname2'", "'group','room','modifyLectureParts','address3/30'",
            "'teacher','course','stringLectureParts','Course3'",
            "'teacher','teacher','modifyLectureParts','N.Surname2'",
            "'teacher','room','modifyLectureParts','address3/30'", "'room','course','stringLectureParts','Course3'",
            "'room','teacher','modifyLectureParts','N.Surname2'", "'room','room','modifyLectureParts','address3/30'" })
    void testModify_shouldDenied_whenAnonymous(String ownername, String substitute, String exit, String result)
            throws Exception {
        LocalDate currentDate = LocalDate.parse("02-01-2022", pattern);
        Long lectureId = 99L;
        Integer serialNumberPerDay = 5;
        Long ownerId = 1L;

        when(lectureServ.retrieveLectureById(lectureId))
                .thenReturn(new Lecture(99L, serialNumberPerDay, currentDate, room, teacher, course, group));
        when(courseServ.retrieveFreeByGroupWithFreeTeachersByTime(group.getId(), currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Course(3L, "Course3", "Description3")));
        when(teacherServ.retrieveFreeByTimeForCourse(course.getId(), currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Teacher(2L, "Name2", "Surname2")));
        when(roomServ.retrieveFreeByTime(currentDate, serialNumberPerDay))
                .thenReturn(Arrays.asList(new Room(3L, "address3", 30)));
        when(mapperUtil.toDto(new Lecture(99L, serialNumberPerDay, currentDate, room, teacher, course, group)))
                .thenReturn(new LectureDto(99L, serialNumberPerDay, currentDate, room.getId(), teacher.getId(),
                        course.getId(), group.getId()));

        mockMvc.perform(get("/lectures/modify").with(csrf()).queryParam("substitute", substitute)
                .queryParam("lectureId", lectureId.toString())
                .queryParam("serialNumberPerDay", serialNumberPerDay.toString()).queryParam("date", "2022-01-02")
                .queryParam("teacherId", teacher.getId().toString()).queryParam("roomId", room.getId().toString())
                .queryParam("courseId", course.getId().toString()).queryParam("groupId", group.getId().toString())
                .queryParam("ownername", ownername).queryParam("ownerId", ownerId.toString())
                .queryParam("startDate", "2022-01-01").queryParam("finishDate", "2022-01-03")).andDo(print())
                .andExpect(status().is(401));

        verify(lectureServ, times(0)).retrieveLectureById(lectureId);
        verify(courseServ, times(0)).retrieveFreeByGroupWithFreeTeachersByTime(group.getId(), currentDate,
                serialNumberPerDay);
        verify(teacherServ, times(0)).retrieveFreeByTimeForCourse(course.getId(), currentDate, serialNumberPerDay);
        verify(roomServ, times(0)).retrieveFreeByTime(currentDate, serialNumberPerDay);
        verify(mapperUtil, times(0))
                .toDto(new Lecture(99L, serialNumberPerDay, currentDate, room, teacher, course, group));
    }

    @Test
    @WithUserDetails("testAdmin")
    void testReloadLecture_shouldAccessed_whenAdmin() throws Exception {
        String ownername = "group";
        Long ownerId = 18L;
        LectureDto lectureDto = new LectureDto(99L, 1, LocalDate.parse("2022-01-02"), room.getId(), teacher.getId(),
                course.getId(), group.getId());
        Lecture lecture = new Lecture(99L, 1, LocalDate.parse("2022-01-02"), room, teacher, course, group);

        when(lectureServ.enter(lecture)).thenReturn(lecture);
        when(mapperUtil.toEntity(lectureDto)).thenReturn(lecture);
        when(adapter.getPage("2022-01-01", LocalDate.parse("2022-01-02"))).thenReturn(2);

        mockMvc.perform(patch("/lectures").with(csrf()).flashAttr("lecture", lectureDto)
                .queryParam("ownername", ownername).queryParam("ownerId", ownerId.toString())
                .queryParam("startDate", "2022-01-01").queryParam("finishDate", "2022-01-03"))
                .andExpect(status().is(302)).andExpect(view().name(
                        "redirect:/lectures/group/18/day?startDate=2022-01-01&finishDate=2022-01-03&page=2&mode=creater"));

        verify(lectureServ, times(1)).enter(lecture);
        verify(mapperUtil, times(1)).toEntity(lectureDto);
        verify(adapter, times(1)).getPage("2022-01-01", LocalDate.parse("2022-01-02"));
    }

    @Test
    @WithUserDetails("testStaff")
    void testReloadLecture_shouldAccessed_whenStaff() throws Exception {
        String ownername = "group";
        Long ownerId = 18L;
        LectureDto lectureDto = new LectureDto(99L, 1, LocalDate.parse("2022-01-02"), room.getId(), teacher.getId(),
                course.getId(), group.getId());
        Lecture lecture = new Lecture(99L, 1, LocalDate.parse("2022-01-02"), room, teacher, course, group);

        when(lectureServ.enter(lecture)).thenReturn(lecture);
        when(mapperUtil.toEntity(lectureDto)).thenReturn(lecture);
        when(adapter.getPage("2022-01-01", LocalDate.parse("2022-01-02"))).thenReturn(2);

        mockMvc.perform(patch("/lectures").with(csrf()).flashAttr("lecture", lectureDto)
                .queryParam("ownername", ownername).queryParam("ownerId", ownerId.toString())
                .queryParam("startDate", "2022-01-01").queryParam("finishDate", "2022-01-03"))
                .andExpect(status().is(302)).andExpect(view().name(
                        "redirect:/lectures/group/18/day?startDate=2022-01-01&finishDate=2022-01-03&page=2&mode=creater"));

        verify(lectureServ, times(1)).enter(lecture);
        verify(mapperUtil, times(1)).toEntity(lectureDto);
        verify(adapter, times(1)).getPage("2022-01-01", LocalDate.parse("2022-01-02"));

    }

    @Test
    @WithUserDetails("testStudent")
    void testReloadLecture_shouldDenied_whenStudnet() throws Exception {
        String ownername = "group";
        Long ownerId = 18L;
        LectureDto lectureDto = new LectureDto(99L, 1, LocalDate.parse("2022-01-02"), room.getId(), teacher.getId(),
                course.getId(), group.getId());
        Lecture lecture = new Lecture(99L, 1, LocalDate.parse("2022-01-02"), room, teacher, course, group);

        when(lectureServ.enter(lecture)).thenReturn(lecture);
        when(mapperUtil.toEntity(lectureDto)).thenReturn(lecture);
        when(adapter.getPage("2022-01-01", LocalDate.parse("2022-01-02"))).thenReturn(2);

        mockMvc.perform(patch("/lectures").with(csrf()).flashAttr("lecture", lectureDto)
                .queryParam("ownername", ownername).queryParam("ownerId", ownerId.toString())
                .queryParam("startDate", "2022-01-01").queryParam("finishDate", "2022-01-03"))
                .andExpect(status().is(403)).andExpect(view().name("error"));

        verify(lectureServ, times(0)).enter(lecture);
        verify(mapperUtil, times(0)).toEntity(lectureDto);
        verify(adapter, times(0)).getPage("2022-01-01", LocalDate.parse("2022-01-02"));
    }

    @Test
    @WithAnonymousUser
    void testReloadLecture_shouldDenied_whenAnonymous() throws Exception {
        String ownername = "group";
        Long ownerId = 18L;
        LectureDto lectureDto = new LectureDto(99L, 1, LocalDate.parse("2022-01-02"), room.getId(), teacher.getId(),
                course.getId(), group.getId());
        Lecture lecture = new Lecture(99L, 1, LocalDate.parse("2022-01-02"), room, teacher, course, group);

        when(lectureServ.enter(lecture)).thenReturn(lecture);
        when(mapperUtil.toEntity(lectureDto)).thenReturn(lecture);
        when(adapter.getPage("2022-01-01", LocalDate.parse("2022-01-02"))).thenReturn(2);

        mockMvc.perform(patch("/lectures").with(csrf()).flashAttr("lecture", lectureDto)
                .queryParam("ownername", ownername).queryParam("ownerId", ownerId.toString())
                .queryParam("startDate", "2022-01-01").queryParam("finishDate", "2022-01-03"))
                .andExpect(status().is(401));

        verify(lectureServ, times(0)).enter(lecture);
        verify(mapperUtil, times(0)).toEntity(lectureDto);
        verify(adapter, times(0)).getPage("2022-01-01", LocalDate.parse("2022-01-02"));
    }

    @Test
    @WithUserDetails("testAdmin")
    void testDelete_shouldAccessed_when() throws Exception {
        String ownername = "group";
        Long ownerId = 18L;
        Long id = 1L;
        String sDate = "2022-01-01";
        String fDate = "2022-01-03";
        String cDate = "2022-01-02";

        when(adapter.getPage(sDate, LocalDate.parse(cDate))).thenReturn(2);

        mockMvc.perform(delete("/lectures/{id}", id).with(csrf()).queryParam("ownername", ownername)
                .queryParam("ownerId", ownerId.toString()).queryParam("startDate", sDate)
                .queryParam("finishDate", fDate).queryParam("date", cDate)).andExpect(status().is(302))
                .andExpect(view().name("redirect:/lectures/group/18/day?startDate=" + sDate + "&finishDate=" + fDate
                        + "&page=2&mode=creater"));

        verify(lectureServ, times(1)).remove(id);
        verify(adapter, times(1)).getPage(sDate, LocalDate.parse(cDate));
    }

    @Test
    @WithUserDetails("testStaff")
    void testDelete_shouldAccessed_whenStaff() throws Exception {
        String ownername = "group";
        Long ownerId = 18L;
        Long id = 1L;
        String sDate = "2022-01-01";
        String fDate = "2022-01-03";
        String cDate = "2022-01-02";

        when(adapter.getPage(sDate, LocalDate.parse(cDate))).thenReturn(2);

        mockMvc.perform(delete("/lectures/{id}", id).with(csrf()).queryParam("ownername", ownername)
                .queryParam("ownerId", ownerId.toString()).queryParam("startDate", sDate)
                .queryParam("finishDate", fDate).queryParam("date", cDate)).andExpect(status().is(302))
                .andExpect(view().name("redirect:/lectures/group/18/day?startDate=" + sDate + "&finishDate=" + fDate
                        + "&page=2&mode=creater"));

        verify(lectureServ, times(1)).remove(id);
        verify(adapter, times(1)).getPage(sDate, LocalDate.parse(cDate));
    }

    @Test
    @WithUserDetails("testStudent")
    void testDelete_shouldDenied_whenStudent() throws Exception {
        String ownername = "group";
        Long ownerId = 18L;
        Long id = 1L;
        String sDate = "2022-01-01";
        String fDate = "2022-01-03";
        String cDate = "2022-01-02";

        when(adapter.getPage(sDate, LocalDate.parse(cDate))).thenReturn(2);

        mockMvc.perform(delete("/lectures/{id}", id).with(csrf()).queryParam("ownername", ownername)
                .queryParam("ownerId", ownerId.toString()).queryParam("startDate", sDate)
                .queryParam("finishDate", fDate).queryParam("date", cDate)).andExpect(status().is(403))
                .andExpect(view().name("error"));

        verify(lectureServ, times(0)).remove(id);
        verify(adapter, times(0)).getPage(sDate, LocalDate.parse(cDate));
    }

    @Test
    @WithAnonymousUser
    void testDelete_shouldDenied_whenAnonymous() throws Exception {
        String ownername = "group";
        Long ownerId = 18L;
        Long id = 1L;
        String sDate = "2022-01-01";
        String fDate = "2022-01-03";
        String cDate = "2022-01-02";

        when(adapter.getPage(sDate, LocalDate.parse(cDate))).thenReturn(2);

        mockMvc.perform(delete("/lectures/{id}", id).with(csrf()).queryParam("ownername", ownername)
                .queryParam("ownerId", ownerId.toString()).queryParam("startDate", sDate)
                .queryParam("finishDate", fDate).queryParam("date", cDate)).andExpect(status().is(401));

        verify(lectureServ, times(0)).remove(id);
        verify(adapter, times(0)).getPage(sDate, LocalDate.parse(cDate));
    }

    @ParameterizedTest
    @WithUserDetails("testAdmin")
    @MethodSource("provideLlookForUnoccupied")
    void lookForUnoccupied_shouldAccessed_whenAdmin(String sourcename, Long sourceId, String ownername, Long ownerId,
            String page, List<String> result) throws Exception {
        LocalDate startDate = LocalDate.parse("30-12-2022", pattern);
        LocalDate currentDate = LocalDate.parse("31-12-2022", pattern);
        LocalDate finishDate = LocalDate.parse("01-01-2023", pattern);

        Long lectureId = 1L;
        Integer serialNumberPerDay = 1;

        SortedMap<LocalDate, SortedMap<Integer, List<LectureDto>>> timetable = new TreeMap<>();
        SortedMap<Integer, List<LectureDto>> firstDayTimetable = new TreeMap<>();
        SortedMap<Integer, List<LectureDto>> secondDayTimetable = new TreeMap<>();
        SortedMap<Integer, List<LectureDto>> thirdDayTimetable = new TreeMap<>();

        firstDayTimetable.put(1, new ArrayList<>(Arrays.asList(new LectureDto(1, startDate))));
        firstDayTimetable.put(2, new ArrayList<>(Arrays.asList(new LectureDto(2, startDate))));
        firstDayTimetable.put(3, new ArrayList<>(Arrays.asList(new LectureDto(3, startDate))));
        firstDayTimetable.put(4, new ArrayList<>(Arrays.asList(new LectureDto(4, startDate))));
        firstDayTimetable.put(5, new ArrayList<>(Arrays.asList(new LectureDto(5, startDate))));
        firstDayTimetable.put(6, new ArrayList<>(Arrays.asList(new LectureDto(6, startDate))));
        timetable.put(startDate, firstDayTimetable);

        secondDayTimetable.put(1, new ArrayList<>(Arrays.asList(new LectureDto(1, currentDate, 1L, 1L, 3L, 1L))));
        secondDayTimetable.put(2, new ArrayList<>(Arrays.asList(new LectureDto(2, currentDate, 1L, 1L, 3L, 1L))));
        secondDayTimetable.put(3, new ArrayList<>(Arrays.asList(new LectureDto(3, currentDate, 1L, 1L, 3L, 2L))));
        secondDayTimetable.put(4, new ArrayList<>(Arrays.asList(new LectureDto(4, currentDate, 1L, 2L, 3L, 1L))));
        secondDayTimetable.put(5, new ArrayList<>(Arrays.asList(new LectureDto(5, currentDate, 2L, 1L, 3L, 1L))));
        secondDayTimetable.put(6, new ArrayList<>(Arrays.asList(new LectureDto(6, currentDate))));
        timetable.put(currentDate, secondDayTimetable);

        thirdDayTimetable.put(1, new ArrayList<>(Arrays.asList(new LectureDto(1, finishDate, 1L, 2L, 3L, 2L))));
        thirdDayTimetable.put(2, new ArrayList<>(Arrays.asList(new LectureDto(2, finishDate, 2L, 1L, 3L, 2L))));
        thirdDayTimetable.put(3, new ArrayList<>(Arrays.asList(new LectureDto(3, finishDate, 2L, 2L, 3L, 1L))));
        thirdDayTimetable.put(4, new ArrayList<>(Arrays.asList(new LectureDto(4, finishDate, 2L, 2L, 3L, 2L),
                new LectureDto(4, finishDate, 3L, 3L, 3L, 3L))));
        thirdDayTimetable.put(5, new ArrayList<>(Arrays.asList(new LectureDto(5, finishDate))));
        thirdDayTimetable.put(6, new ArrayList<>(Arrays.asList(new LectureDto(6, finishDate))));
        timetable.put(finishDate, thirdDayTimetable);

        List<List<LocalDate>> timeImage = Arrays.asList(
                Arrays.asList(LocalDate.parse("01-12-2022", pattern), LocalDate.parse("02-12-2022", pattern),
                        LocalDate.parse("03-12-2022", pattern), LocalDate.parse("04-12-2022", pattern),
                        LocalDate.parse("05-12-2022", pattern), LocalDate.parse("06-12-2022", pattern),
                        LocalDate.parse("07-12-2022", pattern), LocalDate.parse("08-12-2022", pattern),
                        LocalDate.parse("09-12-2022", pattern), LocalDate.parse("10-12-2022", pattern),
                        LocalDate.parse("11-12-2022", pattern), LocalDate.parse("12-12-2022", pattern),
                        LocalDate.parse("13-12-2022", pattern), LocalDate.parse("14-12-2022", pattern),
                        LocalDate.parse("15-12-2022", pattern), LocalDate.parse("16-12-2022", pattern),
                        LocalDate.parse("17-12-2022", pattern), LocalDate.parse("18-12-2022", pattern),
                        LocalDate.parse("19-12-2022", pattern), LocalDate.parse("20-12-2022", pattern),
                        LocalDate.parse("21-12-2022", pattern), LocalDate.parse("22-12-2022", pattern),
                        LocalDate.parse("23-12-2022", pattern), LocalDate.parse("24-12-2022", pattern),
                        LocalDate.parse("25-12-2022", pattern), LocalDate.parse("26-12-2022", pattern),
                        LocalDate.parse("27-12-2022", pattern), LocalDate.parse("28-12-2022", pattern),
                        LocalDate.parse("29-12-2022", pattern), LocalDate.parse("30-12-2022", pattern),
                        LocalDate.parse("31-12-2022", pattern)),
                Arrays.asList(LocalDate.parse("01-01-2023", pattern), LocalDate.parse("02-01-2023", pattern),
                        LocalDate.parse("03-01-2023", pattern), LocalDate.parse("04-01-2023", pattern),
                        LocalDate.parse("05-01-2023", pattern), LocalDate.parse("06-01-2023", pattern),
                        LocalDate.parse("07-01-2023", pattern), LocalDate.parse("08-01-2023", pattern),
                        LocalDate.parse("09-01-2023", pattern), LocalDate.parse("10-01-2023", pattern),
                        LocalDate.parse("11-01-2023", pattern), LocalDate.parse("12-01-2023", pattern),
                        LocalDate.parse("13-01-2023", pattern), LocalDate.parse("14-01-2023", pattern),
                        LocalDate.parse("15-01-2023", pattern), LocalDate.parse("16-01-2023", pattern),
                        LocalDate.parse("17-01-2023", pattern), LocalDate.parse("18-01-2023", pattern),
                        LocalDate.parse("19-01-2023", pattern), LocalDate.parse("20-01-2023", pattern),
                        LocalDate.parse("21-01-2023", pattern), LocalDate.parse("22-01-2023", pattern),
                        LocalDate.parse("23-01-2023", pattern), LocalDate.parse("24-01-2023", pattern),
                        LocalDate.parse("25-01-2023", pattern), LocalDate.parse("26-01-2023", pattern),
                        LocalDate.parse("27-01-2023", pattern), LocalDate.parse("28-01-2023", pattern),
                        LocalDate.parse("29-01-2023", pattern), LocalDate.parse("30-01-2023", pattern),
                        LocalDate.parse("31-01-2023", pattern)));

        when(teacherServ.retrieveAll()).thenReturn(Arrays.asList(new Teacher(1L, "Name1", "Surname1"),
                new Teacher(2L, "Name2", "Surname2"), new Teacher(3L, "Name3", "Surname3")));
        when(groupServ.retrieveAll()).thenReturn(
                Arrays.asList(new Group(1L, "groupName1"), new Group(2L, "groupName2"), new Group(3L, "groupName3")));
        when(roomServ.retrieveAll()).thenReturn(Arrays.asList(new Room(1L, "address1", 10),
                new Room(2L, "address2", 20), new Room(3L, "address3", 30)));
        when(courseServ.retrieveAll()).thenReturn(Arrays.asList(new Course(3L, "Course3", "Description3")));

        when(adapter.stretchMonthTimeline(startDate, finishDate)).thenReturn(timeImage);
        when(adapter.getTimetable(1L, 1L, 1L, startDate, finishDate)).thenReturn(timetable);

        mockMvc.perform(get("/lectures/lecture{lectureId}/clone", lectureId.toString()).with(csrf())
                .queryParam("page", page).queryParam("sourcename", sourcename)
                .queryParam("sourceId", sourceId.toString()).queryParam("ownername", ownername)
                .queryParam("ownerId", "").queryParam("startDate", "2022-12-30").queryParam("finishDate", "2023-01-01")
                .queryParam("serialNumberPerDay", serialNumberPerDay.toString()).queryParam("date", "2022-12-31")
                .queryParam("groupId", "1").queryParam("teacherId", "1").queryParam("courseId", "3")
                .queryParam("roomId", "1")

        ).andExpect(status().is(200)).andExpect(view().name("lectures/stringLectures"))
                .andExpect(content().string(containsString(lectureId.toString())))
                .andExpect(content().string(containsString("2022-12-30")))
                .andExpect(content().string(containsString("2023-01-01")))
                .andExpect(content().string(containsString(result.get(0))))
                .andExpect(content().string(containsString(result.get(1))))
                .andExpect(content().string(containsString(result.get(2))))
                .andExpect(content().string(containsString(result.get(3))))
                .andExpect(content().string(containsString(result.get(4))))
                .andExpect(content().string(containsString(result.get(5))))
                .andExpect(content().string(containsString(result.get(6))))
                .andExpect(content().string(containsString(result.get(7))))
                .andExpect(content().string(containsString(result.get(8))))
                .andExpect(content().string(containsString(result.get(9))))
                .andExpect(content().string(containsString(result.get(10))))
                .andExpect(content().string(containsString(result.get(11))))
                .andExpect(content().string(containsString(result.get(12))))
                .andExpect(content().string(containsString(result.get(13))))
                .andExpect(content().string(containsString(result.get(14))));

        verify(teacherServ, times(1)).retrieveAll();
        verify(groupServ, times(1)).retrieveAll();
        verify(roomServ, times(1)).retrieveAll();
        verify(courseServ, times(1)).retrieveAll();
        verify(adapter, times(1)).stretchMonthTimeline(startDate, finishDate);
        verify(adapter, times(1)).getTimetable(1L, 1L, 1L, startDate, finishDate);
    }

    @ParameterizedTest
    @WithUserDetails("testStaff")
    @MethodSource("provideLlookForUnoccupied")
    void lookForUnoccupied_shouldAccessed_whenStaff(String sourcename, Long sourceId, String ownername, Long ownerId,
            String page, List<String> result) throws Exception {
        LocalDate startDate = LocalDate.parse("30-12-2022", pattern);
        LocalDate currentDate = LocalDate.parse("31-12-2022", pattern);
        LocalDate finishDate = LocalDate.parse("01-01-2023", pattern);

        Long lectureId = 1L;
        Integer serialNumberPerDay = 1;

        SortedMap<LocalDate, SortedMap<Integer, List<LectureDto>>> timetable = new TreeMap<>();
        SortedMap<Integer, List<LectureDto>> firstDayTimetable = new TreeMap<>();
        SortedMap<Integer, List<LectureDto>> secondDayTimetable = new TreeMap<>();
        SortedMap<Integer, List<LectureDto>> thirdDayTimetable = new TreeMap<>();

        firstDayTimetable.put(1, new ArrayList<>(Arrays.asList(new LectureDto(1, startDate))));
        firstDayTimetable.put(2, new ArrayList<>(Arrays.asList(new LectureDto(2, startDate))));
        firstDayTimetable.put(3, new ArrayList<>(Arrays.asList(new LectureDto(3, startDate))));
        firstDayTimetable.put(4, new ArrayList<>(Arrays.asList(new LectureDto(4, startDate))));
        firstDayTimetable.put(5, new ArrayList<>(Arrays.asList(new LectureDto(5, startDate))));
        firstDayTimetable.put(6, new ArrayList<>(Arrays.asList(new LectureDto(6, startDate))));
        timetable.put(startDate, firstDayTimetable);

        secondDayTimetable.put(1, new ArrayList<>(Arrays.asList(new LectureDto(1, currentDate, 1L, 1L, 3L, 1L))));
        secondDayTimetable.put(2, new ArrayList<>(Arrays.asList(new LectureDto(2, currentDate, 1L, 1L, 3L, 1L))));
        secondDayTimetable.put(3, new ArrayList<>(Arrays.asList(new LectureDto(3, currentDate, 1L, 1L, 3L, 2L))));
        secondDayTimetable.put(4, new ArrayList<>(Arrays.asList(new LectureDto(4, currentDate, 1L, 2L, 3L, 1L))));
        secondDayTimetable.put(5, new ArrayList<>(Arrays.asList(new LectureDto(5, currentDate, 2L, 1L, 3L, 1L))));
        secondDayTimetable.put(6, new ArrayList<>(Arrays.asList(new LectureDto(6, currentDate))));
        timetable.put(currentDate, secondDayTimetable);

        thirdDayTimetable.put(1, new ArrayList<>(Arrays.asList(new LectureDto(1, finishDate, 1L, 2L, 3L, 2L))));
        thirdDayTimetable.put(2, new ArrayList<>(Arrays.asList(new LectureDto(2, finishDate, 2L, 1L, 3L, 2L))));
        thirdDayTimetable.put(3, new ArrayList<>(Arrays.asList(new LectureDto(3, finishDate, 2L, 2L, 3L, 1L))));
        thirdDayTimetable.put(4, new ArrayList<>(Arrays.asList(new LectureDto(4, finishDate, 2L, 2L, 3L, 2L),
                new LectureDto(4, finishDate, 3L, 3L, 3L, 3L))));
        thirdDayTimetable.put(5, new ArrayList<>(Arrays.asList(new LectureDto(5, finishDate))));
        thirdDayTimetable.put(6, new ArrayList<>(Arrays.asList(new LectureDto(6, finishDate))));
        timetable.put(finishDate, thirdDayTimetable);

        List<List<LocalDate>> timeImage = Arrays.asList(
                Arrays.asList(LocalDate.parse("01-12-2022", pattern), LocalDate.parse("02-12-2022", pattern),
                        LocalDate.parse("03-12-2022", pattern), LocalDate.parse("04-12-2022", pattern),
                        LocalDate.parse("05-12-2022", pattern), LocalDate.parse("06-12-2022", pattern),
                        LocalDate.parse("07-12-2022", pattern), LocalDate.parse("08-12-2022", pattern),
                        LocalDate.parse("09-12-2022", pattern), LocalDate.parse("10-12-2022", pattern),
                        LocalDate.parse("11-12-2022", pattern), LocalDate.parse("12-12-2022", pattern),
                        LocalDate.parse("13-12-2022", pattern), LocalDate.parse("14-12-2022", pattern),
                        LocalDate.parse("15-12-2022", pattern), LocalDate.parse("16-12-2022", pattern),
                        LocalDate.parse("17-12-2022", pattern), LocalDate.parse("18-12-2022", pattern),
                        LocalDate.parse("19-12-2022", pattern), LocalDate.parse("20-12-2022", pattern),
                        LocalDate.parse("21-12-2022", pattern), LocalDate.parse("22-12-2022", pattern),
                        LocalDate.parse("23-12-2022", pattern), LocalDate.parse("24-12-2022", pattern),
                        LocalDate.parse("25-12-2022", pattern), LocalDate.parse("26-12-2022", pattern),
                        LocalDate.parse("27-12-2022", pattern), LocalDate.parse("28-12-2022", pattern),
                        LocalDate.parse("29-12-2022", pattern), LocalDate.parse("30-12-2022", pattern),
                        LocalDate.parse("31-12-2022", pattern)),
                Arrays.asList(LocalDate.parse("01-01-2023", pattern), LocalDate.parse("02-01-2023", pattern),
                        LocalDate.parse("03-01-2023", pattern), LocalDate.parse("04-01-2023", pattern),
                        LocalDate.parse("05-01-2023", pattern), LocalDate.parse("06-01-2023", pattern),
                        LocalDate.parse("07-01-2023", pattern), LocalDate.parse("08-01-2023", pattern),
                        LocalDate.parse("09-01-2023", pattern), LocalDate.parse("10-01-2023", pattern),
                        LocalDate.parse("11-01-2023", pattern), LocalDate.parse("12-01-2023", pattern),
                        LocalDate.parse("13-01-2023", pattern), LocalDate.parse("14-01-2023", pattern),
                        LocalDate.parse("15-01-2023", pattern), LocalDate.parse("16-01-2023", pattern),
                        LocalDate.parse("17-01-2023", pattern), LocalDate.parse("18-01-2023", pattern),
                        LocalDate.parse("19-01-2023", pattern), LocalDate.parse("20-01-2023", pattern),
                        LocalDate.parse("21-01-2023", pattern), LocalDate.parse("22-01-2023", pattern),
                        LocalDate.parse("23-01-2023", pattern), LocalDate.parse("24-01-2023", pattern),
                        LocalDate.parse("25-01-2023", pattern), LocalDate.parse("26-01-2023", pattern),
                        LocalDate.parse("27-01-2023", pattern), LocalDate.parse("28-01-2023", pattern),
                        LocalDate.parse("29-01-2023", pattern), LocalDate.parse("30-01-2023", pattern),
                        LocalDate.parse("31-01-2023", pattern)));

        when(teacherServ.retrieveAll()).thenReturn(Arrays.asList(new Teacher(1L, "Name1", "Surname1"),
                new Teacher(2L, "Name2", "Surname2"), new Teacher(3L, "Name3", "Surname3")));
        when(groupServ.retrieveAll()).thenReturn(
                Arrays.asList(new Group(1L, "groupName1"), new Group(2L, "groupName2"), new Group(3L, "groupName3")));
        when(roomServ.retrieveAll()).thenReturn(Arrays.asList(new Room(1L, "address1", 10),
                new Room(2L, "address2", 20), new Room(3L, "address3", 30)));
        when(courseServ.retrieveAll()).thenReturn(Arrays.asList(new Course(3L, "Course3", "Description3")));

        when(adapter.stretchMonthTimeline(startDate, finishDate)).thenReturn(timeImage);
        when(adapter.getTimetable(1L, 1L, 1L, startDate, finishDate)).thenReturn(timetable);

        mockMvc.perform(get("/lectures/lecture{lectureId}/clone", lectureId.toString()).with(csrf())
                .queryParam("page", page).queryParam("sourcename", sourcename)
                .queryParam("sourceId", sourceId.toString()).queryParam("ownername", ownername)
                .queryParam("ownerId", "").queryParam("startDate", "2022-12-30").queryParam("finishDate", "2023-01-01")
                .queryParam("serialNumberPerDay", serialNumberPerDay.toString()).queryParam("date", "2022-12-31")
                .queryParam("groupId", "1").queryParam("teacherId", "1").queryParam("courseId", "3")
                .queryParam("roomId", "1")

        ).andExpect(status().is(200)).andExpect(view().name("lectures/stringLectures"))
                .andExpect(content().string(containsString(lectureId.toString())))
                .andExpect(content().string(containsString("2022-12-30")))
                .andExpect(content().string(containsString("2023-01-01")))
                .andExpect(content().string(containsString(result.get(0))))
                .andExpect(content().string(containsString(result.get(1))))
                .andExpect(content().string(containsString(result.get(2))))
                .andExpect(content().string(containsString(result.get(3))))
                .andExpect(content().string(containsString(result.get(4))))
                .andExpect(content().string(containsString(result.get(5))))
                .andExpect(content().string(containsString(result.get(6))))
                .andExpect(content().string(containsString(result.get(7))))
                .andExpect(content().string(containsString(result.get(8))))
                .andExpect(content().string(containsString(result.get(9))))
                .andExpect(content().string(containsString(result.get(10))))
                .andExpect(content().string(containsString(result.get(11))))
                .andExpect(content().string(containsString(result.get(12))))
                .andExpect(content().string(containsString(result.get(13))))
                .andExpect(content().string(containsString(result.get(14))));

        verify(teacherServ, times(1)).retrieveAll();
        verify(groupServ, times(1)).retrieveAll();
        verify(roomServ, times(1)).retrieveAll();
        verify(courseServ, times(1)).retrieveAll();
        verify(adapter, times(1)).stretchMonthTimeline(startDate, finishDate);
        verify(adapter, times(1)).getTimetable(1L, 1L, 1L, startDate, finishDate);
    }

    @ParameterizedTest
    @WithUserDetails("testStudent")
    @MethodSource("provideLlookForUnoccupied")
    void lookForUnoccupied_shouldDenied_whenStudent(String sourcename, Long sourceId, String ownername, Long ownerId,
            String page, List<String> result) throws Exception {
        LocalDate startDate = LocalDate.parse("30-12-2022", pattern);
        LocalDate currentDate = LocalDate.parse("31-12-2022", pattern);
        LocalDate finishDate = LocalDate.parse("01-01-2023", pattern);

        Long lectureId = 1L;
        Integer serialNumberPerDay = 1;

        SortedMap<LocalDate, SortedMap<Integer, List<LectureDto>>> timetable = new TreeMap<>();
        SortedMap<Integer, List<LectureDto>> firstDayTimetable = new TreeMap<>();
        SortedMap<Integer, List<LectureDto>> secondDayTimetable = new TreeMap<>();
        SortedMap<Integer, List<LectureDto>> thirdDayTimetable = new TreeMap<>();

        firstDayTimetable.put(1, new ArrayList<>(Arrays.asList(new LectureDto(1, startDate))));
        firstDayTimetable.put(2, new ArrayList<>(Arrays.asList(new LectureDto(2, startDate))));
        firstDayTimetable.put(3, new ArrayList<>(Arrays.asList(new LectureDto(3, startDate))));
        firstDayTimetable.put(4, new ArrayList<>(Arrays.asList(new LectureDto(4, startDate))));
        firstDayTimetable.put(5, new ArrayList<>(Arrays.asList(new LectureDto(5, startDate))));
        firstDayTimetable.put(6, new ArrayList<>(Arrays.asList(new LectureDto(6, startDate))));
        timetable.put(startDate, firstDayTimetable);

        secondDayTimetable.put(1, new ArrayList<>(Arrays.asList(new LectureDto(1, currentDate, 1L, 1L, 3L, 1L))));
        secondDayTimetable.put(2, new ArrayList<>(Arrays.asList(new LectureDto(2, currentDate, 1L, 1L, 3L, 1L))));
        secondDayTimetable.put(3, new ArrayList<>(Arrays.asList(new LectureDto(3, currentDate, 1L, 1L, 3L, 2L))));
        secondDayTimetable.put(4, new ArrayList<>(Arrays.asList(new LectureDto(4, currentDate, 1L, 2L, 3L, 1L))));
        secondDayTimetable.put(5, new ArrayList<>(Arrays.asList(new LectureDto(5, currentDate, 2L, 1L, 3L, 1L))));
        secondDayTimetable.put(6, new ArrayList<>(Arrays.asList(new LectureDto(6, currentDate))));
        timetable.put(currentDate, secondDayTimetable);

        thirdDayTimetable.put(1, new ArrayList<>(Arrays.asList(new LectureDto(1, finishDate, 1L, 2L, 3L, 2L))));
        thirdDayTimetable.put(2, new ArrayList<>(Arrays.asList(new LectureDto(2, finishDate, 2L, 1L, 3L, 2L))));
        thirdDayTimetable.put(3, new ArrayList<>(Arrays.asList(new LectureDto(3, finishDate, 2L, 2L, 3L, 1L))));
        thirdDayTimetable.put(4, new ArrayList<>(Arrays.asList(new LectureDto(4, finishDate, 2L, 2L, 3L, 2L),
                new LectureDto(4, finishDate, 3L, 3L, 3L, 3L))));
        thirdDayTimetable.put(5, new ArrayList<>(Arrays.asList(new LectureDto(5, finishDate))));
        thirdDayTimetable.put(6, new ArrayList<>(Arrays.asList(new LectureDto(6, finishDate))));
        timetable.put(finishDate, thirdDayTimetable);

        List<List<LocalDate>> timeImage = Arrays.asList(
                Arrays.asList(LocalDate.parse("01-12-2022", pattern), LocalDate.parse("02-12-2022", pattern),
                        LocalDate.parse("03-12-2022", pattern), LocalDate.parse("04-12-2022", pattern),
                        LocalDate.parse("05-12-2022", pattern), LocalDate.parse("06-12-2022", pattern),
                        LocalDate.parse("07-12-2022", pattern), LocalDate.parse("08-12-2022", pattern),
                        LocalDate.parse("09-12-2022", pattern), LocalDate.parse("10-12-2022", pattern),
                        LocalDate.parse("11-12-2022", pattern), LocalDate.parse("12-12-2022", pattern),
                        LocalDate.parse("13-12-2022", pattern), LocalDate.parse("14-12-2022", pattern),
                        LocalDate.parse("15-12-2022", pattern), LocalDate.parse("16-12-2022", pattern),
                        LocalDate.parse("17-12-2022", pattern), LocalDate.parse("18-12-2022", pattern),
                        LocalDate.parse("19-12-2022", pattern), LocalDate.parse("20-12-2022", pattern),
                        LocalDate.parse("21-12-2022", pattern), LocalDate.parse("22-12-2022", pattern),
                        LocalDate.parse("23-12-2022", pattern), LocalDate.parse("24-12-2022", pattern),
                        LocalDate.parse("25-12-2022", pattern), LocalDate.parse("26-12-2022", pattern),
                        LocalDate.parse("27-12-2022", pattern), LocalDate.parse("28-12-2022", pattern),
                        LocalDate.parse("29-12-2022", pattern), LocalDate.parse("30-12-2022", pattern),
                        LocalDate.parse("31-12-2022", pattern)),
                Arrays.asList(LocalDate.parse("01-01-2023", pattern), LocalDate.parse("02-01-2023", pattern),
                        LocalDate.parse("03-01-2023", pattern), LocalDate.parse("04-01-2023", pattern),
                        LocalDate.parse("05-01-2023", pattern), LocalDate.parse("06-01-2023", pattern),
                        LocalDate.parse("07-01-2023", pattern), LocalDate.parse("08-01-2023", pattern),
                        LocalDate.parse("09-01-2023", pattern), LocalDate.parse("10-01-2023", pattern),
                        LocalDate.parse("11-01-2023", pattern), LocalDate.parse("12-01-2023", pattern),
                        LocalDate.parse("13-01-2023", pattern), LocalDate.parse("14-01-2023", pattern),
                        LocalDate.parse("15-01-2023", pattern), LocalDate.parse("16-01-2023", pattern),
                        LocalDate.parse("17-01-2023", pattern), LocalDate.parse("18-01-2023", pattern),
                        LocalDate.parse("19-01-2023", pattern), LocalDate.parse("20-01-2023", pattern),
                        LocalDate.parse("21-01-2023", pattern), LocalDate.parse("22-01-2023", pattern),
                        LocalDate.parse("23-01-2023", pattern), LocalDate.parse("24-01-2023", pattern),
                        LocalDate.parse("25-01-2023", pattern), LocalDate.parse("26-01-2023", pattern),
                        LocalDate.parse("27-01-2023", pattern), LocalDate.parse("28-01-2023", pattern),
                        LocalDate.parse("29-01-2023", pattern), LocalDate.parse("30-01-2023", pattern),
                        LocalDate.parse("31-01-2023", pattern)));

        when(teacherServ.retrieveAll()).thenReturn(Arrays.asList(new Teacher(1L, "Name1", "Surname1"),
                new Teacher(2L, "Name2", "Surname2"), new Teacher(3L, "Name3", "Surname3")));
        when(groupServ.retrieveAll()).thenReturn(
                Arrays.asList(new Group(1L, "groupName1"), new Group(2L, "groupName2"), new Group(3L, "groupName3")));
        when(roomServ.retrieveAll()).thenReturn(Arrays.asList(new Room(1L, "address1", 10),
                new Room(2L, "address2", 20), new Room(3L, "address3", 30)));
        when(courseServ.retrieveAll()).thenReturn(Arrays.asList(new Course(3L, "Course3", "Description3")));

        when(adapter.stretchMonthTimeline(startDate, finishDate)).thenReturn(timeImage);
        when(adapter.getTimetable(1L, 1L, 1L, startDate, finishDate)).thenReturn(timetable);

        mockMvc.perform(get("/lectures/lecture{lectureId}/clone", lectureId.toString()).with(csrf())
                .queryParam("page", page).queryParam("sourcename", sourcename)
                .queryParam("sourceId", sourceId.toString()).queryParam("ownername", ownername)
                .queryParam("ownerId", "").queryParam("startDate", "2022-12-30").queryParam("finishDate", "2023-01-01")
                .queryParam("serialNumberPerDay", serialNumberPerDay.toString()).queryParam("date", "2022-12-31")
                .queryParam("groupId", "1").queryParam("teacherId", "1").queryParam("courseId", "3")
                .queryParam("roomId", "1")

        ).andExpect(status().is(403)).andExpect(view().name("error"));

        verify(teacherServ, times(0)).retrieveAll();
        verify(groupServ, times(0)).retrieveAll();
        verify(roomServ, times(0)).retrieveAll();
        verify(courseServ, times(0)).retrieveAll();
        verify(adapter, times(0)).stretchMonthTimeline(startDate, finishDate);
        verify(adapter, times(0)).getTimetable(1L, 1L, 1L, startDate, finishDate);
    }

    @ParameterizedTest
    @WithAnonymousUser
    @MethodSource("provideLlookForUnoccupied")
    void lookForUnoccupied_shouldDenied_whenAnonymous(String sourcename, Long sourceId, String ownername, Long ownerId,
            String page, List<String> result) throws Exception {
        LocalDate startDate = LocalDate.parse("30-12-2022", pattern);
        LocalDate currentDate = LocalDate.parse("31-12-2022", pattern);
        LocalDate finishDate = LocalDate.parse("01-01-2023", pattern);

        Long lectureId = 1L;
        Integer serialNumberPerDay = 1;

        SortedMap<LocalDate, SortedMap<Integer, List<LectureDto>>> timetable = new TreeMap<>();
        SortedMap<Integer, List<LectureDto>> firstDayTimetable = new TreeMap<>();
        SortedMap<Integer, List<LectureDto>> secondDayTimetable = new TreeMap<>();
        SortedMap<Integer, List<LectureDto>> thirdDayTimetable = new TreeMap<>();

        firstDayTimetable.put(1, new ArrayList<>(Arrays.asList(new LectureDto(1, startDate))));
        firstDayTimetable.put(2, new ArrayList<>(Arrays.asList(new LectureDto(2, startDate))));
        firstDayTimetable.put(3, new ArrayList<>(Arrays.asList(new LectureDto(3, startDate))));
        firstDayTimetable.put(4, new ArrayList<>(Arrays.asList(new LectureDto(4, startDate))));
        firstDayTimetable.put(5, new ArrayList<>(Arrays.asList(new LectureDto(5, startDate))));
        firstDayTimetable.put(6, new ArrayList<>(Arrays.asList(new LectureDto(6, startDate))));
        timetable.put(startDate, firstDayTimetable);

        secondDayTimetable.put(1, new ArrayList<>(Arrays.asList(new LectureDto(1, currentDate, 1L, 1L, 3L, 1L))));
        secondDayTimetable.put(2, new ArrayList<>(Arrays.asList(new LectureDto(2, currentDate, 1L, 1L, 3L, 1L))));
        secondDayTimetable.put(3, new ArrayList<>(Arrays.asList(new LectureDto(3, currentDate, 1L, 1L, 3L, 2L))));
        secondDayTimetable.put(4, new ArrayList<>(Arrays.asList(new LectureDto(4, currentDate, 1L, 2L, 3L, 1L))));
        secondDayTimetable.put(5, new ArrayList<>(Arrays.asList(new LectureDto(5, currentDate, 2L, 1L, 3L, 1L))));
        secondDayTimetable.put(6, new ArrayList<>(Arrays.asList(new LectureDto(6, currentDate))));
        timetable.put(currentDate, secondDayTimetable);

        thirdDayTimetable.put(1, new ArrayList<>(Arrays.asList(new LectureDto(1, finishDate, 1L, 2L, 3L, 2L))));
        thirdDayTimetable.put(2, new ArrayList<>(Arrays.asList(new LectureDto(2, finishDate, 2L, 1L, 3L, 2L))));
        thirdDayTimetable.put(3, new ArrayList<>(Arrays.asList(new LectureDto(3, finishDate, 2L, 2L, 3L, 1L))));
        thirdDayTimetable.put(4, new ArrayList<>(Arrays.asList(new LectureDto(4, finishDate, 2L, 2L, 3L, 2L),
                new LectureDto(4, finishDate, 3L, 3L, 3L, 3L))));
        thirdDayTimetable.put(5, new ArrayList<>(Arrays.asList(new LectureDto(5, finishDate))));
        thirdDayTimetable.put(6, new ArrayList<>(Arrays.asList(new LectureDto(6, finishDate))));
        timetable.put(finishDate, thirdDayTimetable);

        List<List<LocalDate>> timeImage = Arrays.asList(
                Arrays.asList(LocalDate.parse("01-12-2022", pattern), LocalDate.parse("02-12-2022", pattern),
                        LocalDate.parse("03-12-2022", pattern), LocalDate.parse("04-12-2022", pattern),
                        LocalDate.parse("05-12-2022", pattern), LocalDate.parse("06-12-2022", pattern),
                        LocalDate.parse("07-12-2022", pattern), LocalDate.parse("08-12-2022", pattern),
                        LocalDate.parse("09-12-2022", pattern), LocalDate.parse("10-12-2022", pattern),
                        LocalDate.parse("11-12-2022", pattern), LocalDate.parse("12-12-2022", pattern),
                        LocalDate.parse("13-12-2022", pattern), LocalDate.parse("14-12-2022", pattern),
                        LocalDate.parse("15-12-2022", pattern), LocalDate.parse("16-12-2022", pattern),
                        LocalDate.parse("17-12-2022", pattern), LocalDate.parse("18-12-2022", pattern),
                        LocalDate.parse("19-12-2022", pattern), LocalDate.parse("20-12-2022", pattern),
                        LocalDate.parse("21-12-2022", pattern), LocalDate.parse("22-12-2022", pattern),
                        LocalDate.parse("23-12-2022", pattern), LocalDate.parse("24-12-2022", pattern),
                        LocalDate.parse("25-12-2022", pattern), LocalDate.parse("26-12-2022", pattern),
                        LocalDate.parse("27-12-2022", pattern), LocalDate.parse("28-12-2022", pattern),
                        LocalDate.parse("29-12-2022", pattern), LocalDate.parse("30-12-2022", pattern),
                        LocalDate.parse("31-12-2022", pattern)),
                Arrays.asList(LocalDate.parse("01-01-2023", pattern), LocalDate.parse("02-01-2023", pattern),
                        LocalDate.parse("03-01-2023", pattern), LocalDate.parse("04-01-2023", pattern),
                        LocalDate.parse("05-01-2023", pattern), LocalDate.parse("06-01-2023", pattern),
                        LocalDate.parse("07-01-2023", pattern), LocalDate.parse("08-01-2023", pattern),
                        LocalDate.parse("09-01-2023", pattern), LocalDate.parse("10-01-2023", pattern),
                        LocalDate.parse("11-01-2023", pattern), LocalDate.parse("12-01-2023", pattern),
                        LocalDate.parse("13-01-2023", pattern), LocalDate.parse("14-01-2023", pattern),
                        LocalDate.parse("15-01-2023", pattern), LocalDate.parse("16-01-2023", pattern),
                        LocalDate.parse("17-01-2023", pattern), LocalDate.parse("18-01-2023", pattern),
                        LocalDate.parse("19-01-2023", pattern), LocalDate.parse("20-01-2023", pattern),
                        LocalDate.parse("21-01-2023", pattern), LocalDate.parse("22-01-2023", pattern),
                        LocalDate.parse("23-01-2023", pattern), LocalDate.parse("24-01-2023", pattern),
                        LocalDate.parse("25-01-2023", pattern), LocalDate.parse("26-01-2023", pattern),
                        LocalDate.parse("27-01-2023", pattern), LocalDate.parse("28-01-2023", pattern),
                        LocalDate.parse("29-01-2023", pattern), LocalDate.parse("30-01-2023", pattern),
                        LocalDate.parse("31-01-2023", pattern)));

        when(teacherServ.retrieveAll()).thenReturn(Arrays.asList(new Teacher(1L, "Name1", "Surname1"),
                new Teacher(2L, "Name2", "Surname2"), new Teacher(3L, "Name3", "Surname3")));
        when(groupServ.retrieveAll()).thenReturn(
                Arrays.asList(new Group(1L, "groupName1"), new Group(2L, "groupName2"), new Group(3L, "groupName3")));
        when(roomServ.retrieveAll()).thenReturn(Arrays.asList(new Room(1L, "address1", 10),
                new Room(2L, "address2", 20), new Room(3L, "address3", 30)));
        when(courseServ.retrieveAll()).thenReturn(Arrays.asList(new Course(3L, "Course3", "Description3")));

        when(adapter.stretchMonthTimeline(startDate, finishDate)).thenReturn(timeImage);
        when(adapter.getTimetable(1L, 1L, 1L, startDate, finishDate)).thenReturn(timetable);

        mockMvc.perform(get("/lectures/lecture{lectureId}/clone", lectureId.toString()).with(csrf())
                .queryParam("page", page).queryParam("sourcename", sourcename)
                .queryParam("sourceId", sourceId.toString()).queryParam("ownername", ownername)
                .queryParam("ownerId", "").queryParam("startDate", "2022-12-30").queryParam("finishDate", "2023-01-01")
                .queryParam("serialNumberPerDay", serialNumberPerDay.toString()).queryParam("date", "2022-12-31")
                .queryParam("groupId", "1").queryParam("teacherId", "1").queryParam("courseId", "3")
                .queryParam("roomId", "1")

        ).andExpect(status().is(401));

        verify(teacherServ, times(0)).retrieveAll();
        verify(groupServ, times(0)).retrieveAll();
        verify(roomServ, times(0)).retrieveAll();
        verify(courseServ, times(0)).retrieveAll();
        verify(adapter, times(0)).stretchMonthTimeline(startDate, finishDate);
        verify(adapter, times(0)).getTimetable(1L, 1L, 1L, startDate, finishDate);
    }

    private static Stream<Arguments> provideLlookForUnoccupied() {
//String sourcename, String ownername, Long ownerId, int page, List<String>result
        String sDate = "2022-12-30";
        String fDate = "2023-01-01";
        String cDate = "2022-12-31";
        return Stream.of(
                Arguments.of("group", 1L, "", null, "",
                        Arrays.asList("2022-12-01", "beyond a given time", "2022-12-29", "beyond a given time", sDate,
                                "eatMe", "eatMe", "eatMe", "eatMe", "eatMe", cDate, "it is taken", "it is taken",
                                "eatMe", "it is taken", "it is taken", "eatMe")),
                Arguments.of("group", 1L, "", null, "1",
                        Arrays.asList("2022-12-01", "beyond a given time", "2022-12-29", "beyond a given time", sDate,
                                "eatMe", "eatMe", "eatMe", "eatMe", "eatMe", cDate, "it is taken", "it is taken",
                                "eatMe", "it is taken", "it is taken", "eatMe")),
                Arguments.of("group", 1L, "", null, "2",
                        Arrays.asList(fDate, "eatMe", "eatMe", "it is taken", "eatMe", "eatMe", "eatMe", "2023-01-02",
                                "beyond a given time", "2023-01-31", "beyond a given time", "", "", "", "")),
                Arguments.of("something", 9999L, "group", 1L, "2",
                        Arrays.asList(fDate, "eatMe", "eatMe", "it is taken", "eatMe", "eatMe", "eatMe", "2023-01-02",
                                "beyond a given time", "2023-01-31", "beyond a given time", "", "", "", "")),

                Arguments.of("teacher", 1L, "", null, "",
                        Arrays.asList("2022-12-01", "beyond a given time", "2022-12-29", "beyond a given time", sDate,
                                "eatMe", "eatMe", "eatMe", "eatMe", "eatMe", cDate, "it is taken", "it is taken",
                                "it is taken", "eatMe", "it is taken", "eatMe")),
                Arguments.of("teacher", 1L, "", null, "1",
                        Arrays.asList("2022-12-01", "beyond a given time", "2022-12-29", "beyond a given time", sDate,
                                "eatMe", "eatMe", "eatMe", "eatMe", "eatMe", cDate, "it is taken", "it is taken",
                                "it is taken", "eatMe", "it is taken", "eatMe")),
                Arguments.of("teacher", 1L, "", null, "2",
                        Arrays.asList(fDate, "eatMe", "it is taken", "eatMe", "eatMe", "eatMe", "eatMe", "2023-01-02",
                                "beyond a given time", "2023-01-31", "beyond a given time", "", "", "", "")),
                Arguments.of("something", 9999L, "teacher", 1L, "2",
                        Arrays.asList(fDate, "eatMe", "it is taken", "eatMe", "eatMe", "eatMe", "eatMe", "2023-01-02",
                                "beyond a given time", "2023-01-31", "beyond a given time", "", "", "", "")),

                Arguments.of("room", 1L, "", null, "",
                        Arrays.asList("2022-12-01", "beyond a given time", "2022-12-29", "beyond a given time", sDate,
                                "eatMe", "eatMe", "eatMe", "eatMe", "eatMe", cDate, "it is taken", "it is taken",
                                "it is taken", "it is taken", "eatMe", "eatMe")),
                Arguments.of("room", 1L, "", null, "1",
                        Arrays.asList("2022-12-01", "beyond a given time", "2022-12-29", "beyond a given time", sDate,
                                "eatMe", "eatMe", "eatMe", "eatMe", "eatMe", cDate, "it is taken", "it is taken",
                                "it is taken", "it is taken", "eatMe", "eatMe")),
                Arguments.of("room", 1L, "", null, "2",
                        Arrays.asList(fDate, "it is taken", "eatMe", "eatMe", "eatMe", "eatMe", "eatMe", "2023-01-02",
                                "beyond a given time", "2023-01-31", "beyond a given time", "", "", "", "")),
                Arguments.of("something", 9999L, "room", 1L, "2",
                        Arrays.asList(fDate, "it is taken", "eatMe", "eatMe", "eatMe", "eatMe", "eatMe", "2023-01-02",
                                "beyond a given time", "2023-01-31", "beyond a given time", "", "", "", "")));

    }

    @ParameterizedTest
    @WithUserDetails("testAdmin")
    @MethodSource("provideMakeClone")
    void testMakeClone_shouldAccessed_whenAdmin(String markMoveOwner, String[] timeSpots, String sourcename,
            List<Lecture> expected) throws Exception {
        Course theCourse = new Course(3L, "course", "descriptopn");
        String startDate = "2022-12-31";
        String finishDate = "2023-01-02";
        String sourceId = "1";

        when(mapperUtil.toEntity(new LectureDto(1, LocalDate.parse("2022-12-31"), room.getId(), teacher.getId(),
                theCourse.getId(), group.getId())))
                .thenReturn(new Lecture(1, LocalDate.parse("2022-12-31"), room, teacher, theCourse, group));
        when(mapperUtil.toEntity(new LectureDto(1L, 1, LocalDate.parse("2022-12-31"), room.getId(), teacher.getId(),
                theCourse.getId(), group.getId())))
                .thenReturn(new Lecture(1L, 1, LocalDate.parse("2022-12-31"), room, teacher, theCourse, group));

        when(mapperUtil.toEntity(new LectureDto(2, LocalDate.parse("2022-01-01"), room.getId(), teacher.getId(),
                theCourse.getId(), group.getId())))
                .thenReturn(new Lecture(2, LocalDate.parse("2022-01-01"), room, teacher, theCourse, group));
        when(mapperUtil.toEntity(new LectureDto(4, LocalDate.parse("2022-01-02"), room.getId(), teacher.getId(),
                theCourse.getId(), group.getId())))
                .thenReturn(new Lecture(4, LocalDate.parse("2022-01-02"), room, teacher, theCourse, group));

        mockMvc.perform(post("/lectures/clone").with(csrf()).param("groupId", "1").param("teacherId", "1")
                .param("roomId", "1").param("courseId", "3").param("startDate", startDate)
                .param("finishDate", finishDate).param("markMoveOwner", markMoveOwner).param("timeSpots", timeSpots)
                .param("sourceId", sourceId).param("sourcename", sourcename)).andExpect(status().is(302))
                .andExpect(view().name("redirect:/lectures/" + sourcename + "/" + sourceId + "/month?startDate="
                        + startDate + "&finishDate=" + finishDate));

        verify(lectureServ, times(timeSpots.length)).enter(lectureCaptor.capture());
        assertEquals(expected, lectureCaptor.getAllValues());
    }

    @ParameterizedTest
    @WithUserDetails("testStaff")
    @MethodSource("provideMakeClone")
    void testMakeClone_shouldAccessed_whenStaff(String markMoveOwner, String[] timeSpots, String sourcename,
            List<Lecture> expected) throws Exception {
        Course theCourse = new Course(3L, "course", "descriptopn");
        String startDate = "2022-12-31";
        String finishDate = "2023-01-02";
        String sourceId = "1";

        when(mapperUtil.toEntity(new LectureDto(1, LocalDate.parse("2022-12-31"), room.getId(), teacher.getId(),
                theCourse.getId(), group.getId())))
                .thenReturn(new Lecture(1, LocalDate.parse("2022-12-31"), room, teacher, theCourse, group));
        when(mapperUtil.toEntity(new LectureDto(1L, 1, LocalDate.parse("2022-12-31"), room.getId(), teacher.getId(),
                theCourse.getId(), group.getId())))
                .thenReturn(new Lecture(1L, 1, LocalDate.parse("2022-12-31"), room, teacher, theCourse, group));

        when(mapperUtil.toEntity(new LectureDto(2, LocalDate.parse("2022-01-01"), room.getId(), teacher.getId(),
                theCourse.getId(), group.getId())))
                .thenReturn(new Lecture(2, LocalDate.parse("2022-01-01"), room, teacher, theCourse, group));
        when(mapperUtil.toEntity(new LectureDto(4, LocalDate.parse("2022-01-02"), room.getId(), teacher.getId(),
                theCourse.getId(), group.getId())))
                .thenReturn(new Lecture(4, LocalDate.parse("2022-01-02"), room, teacher, theCourse, group));

        mockMvc.perform(post("/lectures/clone").with(csrf()).param("groupId", "1").param("teacherId", "1")
                .param("roomId", "1").param("courseId", "3").param("startDate", startDate)
                .param("finishDate", finishDate).param("markMoveOwner", markMoveOwner).param("timeSpots", timeSpots)
                .param("sourceId", sourceId).param("sourcename", sourcename)).andExpect(status().is(302))
                .andExpect(view().name("redirect:/lectures/" + sourcename + "/" + sourceId + "/month?startDate="
                        + startDate + "&finishDate=" + finishDate));

        verify(lectureServ, times(timeSpots.length)).enter(lectureCaptor.capture());
        assertEquals(expected, lectureCaptor.getAllValues());
    }

    @ParameterizedTest
    @WithUserDetails("testStudent")
    @MethodSource("provideMakeClone")
    void testMakeClone_shouldDenied_whenStudent(String markMoveOwner, String[] timeSpots, String sourcename,
            List<Lecture> expected) throws Exception {
        Course theCourse = new Course(3L, "course", "descriptopn");
        String startDate = "2022-12-31";
        String finishDate = "2023-01-02";
        String sourceId = "1";

        when(mapperUtil.toEntity(new LectureDto(1, LocalDate.parse("2022-12-31"), room.getId(), teacher.getId(),
                theCourse.getId(), group.getId())))
                .thenReturn(new Lecture(1, LocalDate.parse("2022-12-31"), room, teacher, theCourse, group));
        when(mapperUtil.toEntity(new LectureDto(1L, 1, LocalDate.parse("2022-12-31"), room.getId(), teacher.getId(),
                theCourse.getId(), group.getId())))
                .thenReturn(new Lecture(1L, 1, LocalDate.parse("2022-12-31"), room, teacher, theCourse, group));

        when(mapperUtil.toEntity(new LectureDto(2, LocalDate.parse("2022-01-01"), room.getId(), teacher.getId(),
                theCourse.getId(), group.getId())))
                .thenReturn(new Lecture(2, LocalDate.parse("2022-01-01"), room, teacher, theCourse, group));
        when(mapperUtil.toEntity(new LectureDto(4, LocalDate.parse("2022-01-02"), room.getId(), teacher.getId(),
                theCourse.getId(), group.getId())))
                .thenReturn(new Lecture(4, LocalDate.parse("2022-01-02"), room, teacher, theCourse, group));

        mockMvc.perform(post("/lectures/clone").with(csrf()).param("groupId", "1").param("teacherId", "1")
                .param("roomId", "1").param("courseId", "3").param("startDate", startDate)
                .param("finishDate", finishDate).param("markMoveOwner", markMoveOwner).param("timeSpots", timeSpots)
                .param("sourceId", sourceId).param("sourcename", sourcename)).andExpect(status().is(403))
                .andExpect(view().name("error"));

        verify(lectureServ, times(0)).enter(lectureCaptor.capture());
    }

    @ParameterizedTest
    @WithAnonymousUser
    @MethodSource("provideMakeClone")
    void testMakeClone_shouldDenied_whenAnonymous(String markMoveOwner, String[] timeSpots, String sourcename,
            List<Lecture> expected) throws Exception {
        Course theCourse = new Course(3L, "course", "descriptopn");
        String startDate = "2022-12-31";
        String finishDate = "2023-01-02";
        String sourceId = "1";

        when(mapperUtil.toEntity(new LectureDto(1, LocalDate.parse("2022-12-31"), room.getId(), teacher.getId(),
                theCourse.getId(), group.getId())))
                .thenReturn(new Lecture(1, LocalDate.parse("2022-12-31"), room, teacher, theCourse, group));
        when(mapperUtil.toEntity(new LectureDto(1L, 1, LocalDate.parse("2022-12-31"), room.getId(), teacher.getId(),
                theCourse.getId(), group.getId())))
                .thenReturn(new Lecture(1L, 1, LocalDate.parse("2022-12-31"), room, teacher, theCourse, group));

        when(mapperUtil.toEntity(new LectureDto(2, LocalDate.parse("2022-01-01"), room.getId(), teacher.getId(),
                theCourse.getId(), group.getId())))
                .thenReturn(new Lecture(2, LocalDate.parse("2022-01-01"), room, teacher, theCourse, group));
        when(mapperUtil.toEntity(new LectureDto(4, LocalDate.parse("2022-01-02"), room.getId(), teacher.getId(),
                theCourse.getId(), group.getId())))
                .thenReturn(new Lecture(4, LocalDate.parse("2022-01-02"), room, teacher, theCourse, group));

        mockMvc.perform(post("/lectures/clone").with(csrf()).param("groupId", "1").param("teacherId", "1")
                .param("roomId", "1").param("courseId", "3").param("startDate", startDate)
                .param("finishDate", finishDate).param("markMoveOwner", markMoveOwner).param("timeSpots", timeSpots)
                .param("sourceId", sourceId).param("sourcename", sourcename)).andExpect(status().is(401));

        verify(lectureServ, times(0)).enter(lectureCaptor.capture());
    }

    private static Stream<Arguments> provideMakeClone() {
        String[] timeSpots = { "1 2022-12-31", "2 2022-01-01", "4 2022-01-02" };
        Course theCourse = new Course(3L, "course", "descriptopn");
        List<Lecture> addExpected = Arrays.asList(
                new Lecture(1, LocalDate.parse("2022-12-31"), room, teacher, theCourse, group),
                new Lecture(2, LocalDate.parse("2022-01-01"), room, teacher, theCourse, group),
                new Lecture(4, LocalDate.parse("2022-01-02"), room, teacher, theCourse, group));
        List<Lecture> moveExpected = Arrays.asList(
                new Lecture(1L, 1, LocalDate.parse("2022-12-31"), room, teacher, theCourse, group),
                new Lecture(2, LocalDate.parse("2022-01-01"), room, teacher, theCourse, group),
                new Lecture(4, LocalDate.parse("2022-01-02"), room, teacher, theCourse, group));

        // String markMoveOwner, String [] timeSpots, String sourcename, List<Lecture>
        // expected
        return Stream.of(Arguments.of("1", timeSpots, "group", moveExpected),
                Arguments.of("", timeSpots, "group", addExpected),
                Arguments.of("1", timeSpots, "teacher", moveExpected),
                Arguments.of("", timeSpots, "teacher", addExpected), Arguments.of("1", timeSpots, "room", moveExpected),
                Arguments.of("", timeSpots, "room", addExpected));
    }

}
