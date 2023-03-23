package ua.com.foxminded.asharov.universityschedule.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import ua.com.foxminded.asharov.universityschedule.config.Role;
import ua.com.foxminded.asharov.universityschedule.dto.CourseDto;
import ua.com.foxminded.asharov.universityschedule.dto.GroupDto;
import ua.com.foxminded.asharov.universityschedule.dto.LectureDto;
import ua.com.foxminded.asharov.universityschedule.dto.RoomDto;
import ua.com.foxminded.asharov.universityschedule.dto.TeacherDto;
import ua.com.foxminded.asharov.universityschedule.dto.util.Adapter;
import ua.com.foxminded.asharov.universityschedule.dto.util.MapperUtil;
import ua.com.foxminded.asharov.universityschedule.entity.Course;
import ua.com.foxminded.asharov.universityschedule.entity.Group;
import ua.com.foxminded.asharov.universityschedule.entity.Room;
import ua.com.foxminded.asharov.universityschedule.entity.Teacher;
import ua.com.foxminded.asharov.universityschedule.exception.UniversityException;
import ua.com.foxminded.asharov.universityschedule.service.*;

import static ua.com.foxminded.asharov.universityschedule.dto.util.Adapter.START_POINT_OF_PAGINATION;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/lectures")
public class LecturesController {
    private static final String DATE_AND_TIME = "dateAndTime";
    private static final String MAP_OF_COURSE = "coursesMap";
    private static final String MAP_OF_TEACHER = "teachersMap";
    private static final String MAP_OF_ROOM = "roomsMap";
    private static final String MAP_OF_GROUP = "groupsMap";
    private static final String START_DATE = "startDate";
    private static final String FINISH_DATE = "finishDate";
    private static final String LECTURE = "lecture";
    private static final String TEACHER = "teacher";
    private static final String GROUP = "group";
    private static final String ROOM = "room";
    private static final String COURSE = "course";
    private static final String TEACHER_ID = "teacherId";
    private static final String GROUP_ID = "groupId";
    private static final String ROOM_ID = "roomId";
    private static final String COURSE_ID = "courseId";
    private static final String LECTURE_ID = "lectureId";
    private static final String NUMBER_OF_DAY = "serialNumberPerDay";
    private static final String SELECTION = "selection";
    private static final String OWNERNAME = "ownername";
    private static final String OWNER_ID = "ownerId";
    private static final String MONTH = "month";
    private static final String REDIRECT = "redirect";
    private static final String TIMETABLE = "timetable";
    private static final String LECTURES = "lectures";

    private final LectureService lectureServ;
    private final TeacherService teacherServ;
    private final GroupService groupServ;
    private final RoomService roomServ;
    private final CourseService courseServ;
    private final Adapter adapter;
    private final MapperUtil mapperUtil;

    public LecturesController(LectureService lectureServ, TeacherService teacherServ, GroupService groupServ,
            RoomService roomServ, CourseService courseServ, Adapter adapter, MapperUtil mapperUtil) {
        this.lectureServ = lectureServ;
        this.teacherServ = teacherServ;
        this.groupServ = groupServ;
        this.roomServ = roomServ;
        this.courseServ = courseServ;
        this.adapter = adapter;
        this.mapperUtil = mapperUtil;
    }

    @Secured(Role.Recall.VIEWER)
    @SuppressWarnings("unchecked")
    @GetMapping(value = { "/{ownername}/{id}", "/{ownername}/{id}/{timeview}", "/{ownername}/{id}/{timeview}" })
    public String showTimetable(@PathVariable("id") Long ownerId, @PathVariable(OWNERNAME) String ownername,
            @PathVariable(value = "timeview", required = false) String timeview,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "currentDay", required = false) String currentDay,
            @RequestParam(value = START_DATE, required = false) String sDate,
            @RequestParam(value = FINISH_DATE, required = false) String fDate,
            @RequestParam(value = "showcase", required = false) String showcase,
            @RequestParam(value = "mode", required = false) String mode, Model model) throws Throwable {
        model.addAttribute("owner",
                adapter.getOwnerByIdByName(ownerId, ownername).orElseThrow(() -> new UniversityException(ownername)));
        model.addAttribute(OWNERNAME, ownername != null && ownername.equals("student") ? GROUP : ownername);

        if (sDate == null || fDate == null || sDate.length() == 0 || fDate.length() == 0) {
            return UriComponentsBuilder.newInstance().pathSegment(LECTURES).pathSegment(TIMETABLE).build().toString();
        }

        if (LocalDate.parse(sDate).isAfter(LocalDate.parse(fDate))) {
            return UriComponentsBuilder.newInstance().pathSegment(LECTURES).pathSegment(TIMETABLE).build().toString();
        }

        if (timeview == null) {
            timeview = MONTH;
            showcase = "";
        } else if (timeview.equals(MONTH)) {
            mode = "viewer";
        }

        if (showcase == null && timeview.equals("day")) {
            showcase = "";
        } else if (showcase == null) {
            showcase = COURSE;
        }

        if (page == null) {
            page = START_POINT_OF_PAGINATION;
        }

        if (currentDay != null) {
            page = Period.between(LocalDate.parse(sDate), LocalDate.parse(currentDay)).getDays()
                    + START_POINT_OF_PAGINATION;
        }
        SortedMap<LocalDate, ArrayList<LectureDto>> timetablePerDay = adapter
                .stuffTimetableByOwnername(LocalDate.parse(sDate), LocalDate.parse(fDate), ownerId, ownername);
        Map<Long, CourseDto> allCourses = courseServ.retrieveAll().stream().map(mapperUtil::toDto)
                .collect(Collectors.toMap(CourseDto::getId, a -> a));
        Map<Long, TeacherDto> allTeachers = teacherServ.retrieveAll().stream().map(mapperUtil::toDto)
                .collect(Collectors.toMap(TeacherDto::getId, a -> a));
        Map<Long, GroupDto> allGroups = groupServ.retrieveAll().stream().map(mapperUtil::toDto)
                .collect(Collectors.toMap(GroupDto::getId, a -> a));
        Map<Long, RoomDto> allRoom = roomServ.retrieveAll().stream().map(mapperUtil::toDto)
                .collect(Collectors.toMap(RoomDto::getId, a -> a));

        allCourses.put(null, new CourseDto());
        allTeachers.put(null, new TeacherDto());
        allGroups.put(null, new GroupDto());
        allRoom.put(null, new RoomDto());

        model.addAttribute("page", page);
        model.addAttribute("showcase", showcase);
        model.addAttribute("timeview", timeview);
        model.addAttribute("mode", mode);
        model.addAttribute(MAP_OF_COURSE, allCourses);
        model.addAttribute(MAP_OF_TEACHER, allTeachers);
        model.addAttribute(MAP_OF_ROOM, allRoom);
        model.addAttribute(MAP_OF_GROUP, allGroups);
        model.addAttribute("timetablePerDay", timetablePerDay);
        model.addAttribute("timetablePerMonth", adapter.wrapForMonthView(timetablePerDay));
        model.addAttribute(START_DATE, LocalDate.parse(sDate));
        model.addAttribute(FINISH_DATE, LocalDate.parse(fDate));
        return UriComponentsBuilder.newInstance().pathSegment(LECTURES).pathSegment(TIMETABLE).build().toString();
    }

    @Secured(Role.Recall.MODERATOR)
    @GetMapping("modify")
    public String modify(@RequestParam(value = LECTURE_ID, required = false) Long lectureId, Model model,
            @RequestParam(value = "substitute", required = false) String substitute,
            @RequestParam(value = NUMBER_OF_DAY, required = false) int serialNumberPerDay,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = TEACHER_ID, required = false) Long teacherId,
            @RequestParam(value = ROOM_ID, required = false) Long roomId,
            @RequestParam(value = COURSE_ID, required = false) Long courseId,
            @RequestParam(value = GROUP_ID, required = false) Long groupId,
            @RequestParam(value = OWNERNAME, required = false) String ownername,
            @RequestParam(value = OWNER_ID, required = false) Long ownerId,
            @RequestParam(value = START_DATE, required = false) String sDate,
            @RequestParam(value = FINISH_DATE, required = false) String fDate) {

        model.addAttribute(LECTURE, mapperUtil.toDto(lectureServ.retrieveLectureById(lectureId)));
        model.addAttribute("substitute", substitute);

        model.addAttribute(LECTURE_ID, lectureId);
        model.addAttribute(NUMBER_OF_DAY, serialNumberPerDay);
        model.addAttribute("date", LocalDate.parse(date));
        model.addAttribute(TEACHER_ID, teacherId);
        model.addAttribute(ROOM_ID, roomId);
        model.addAttribute(COURSE_ID, courseId);
        model.addAttribute(GROUP_ID, groupId);

        model.addAttribute(OWNERNAME, ownername);
        model.addAttribute(OWNER_ID, ownerId);
        model.addAttribute(START_DATE, LocalDate.parse(sDate));
        model.addAttribute(FINISH_DATE, LocalDate.parse(fDate));

        if (substitute.equals(COURSE)) {
            model.addAttribute(SELECTION, courseServ.retrieveFreeByGroupWithFreeTeachersByTime(groupId,
                    LocalDate.parse(date), serialNumberPerDay));
            return "lectures/stringLectureParts";
        }

        if (substitute.equals(TEACHER)) {
            model.addAttribute(SELECTION,
                    teacherServ.retrieveFreeByTimeForCourse(courseId, LocalDate.parse(date), serialNumberPerDay));
        }

        if (substitute.equals(ROOM)) {
            model.addAttribute(SELECTION, roomServ.retrieveFreeByTime(LocalDate.parse(date), serialNumberPerDay));
        }

        return "lectures/modifyLectureParts";
    }

    @Secured(Role.Recall.MODERATOR)
    @SuppressWarnings("unchecked")
    @GetMapping("new")
    public String collectLectureParts(Model model, @RequestParam(OWNERNAME) String ownername,
            @RequestParam(OWNER_ID) Long ownerId, @RequestParam(START_DATE) String sDate,
            @RequestParam(FINISH_DATE) String fDate, @RequestParam("substitute") String substitute,
            @RequestParam("date") String cDate,
            @RequestParam(value = NUMBER_OF_DAY, required = false) int serialNumberPerDay,
            @RequestParam(value = LECTURE_ID, required = false) Long lectureId,
            @RequestParam(value = TEACHER_ID, required = false) Long teacherId,
            @RequestParam(value = ROOM_ID, required = false) Long roomId,
            @RequestParam(value = COURSE_ID, required = false) Long courseId,
            @RequestParam(value = GROUP_ID, required = false) Long groupId) throws Throwable {
        LectureDto interimLecture = LectureDto.builder().id(lectureId).serialNumberPerDay(serialNumberPerDay)
                .date(LocalDate.parse(cDate)).roomId(ownername != null && ownername.equals(ROOM) ? ownerId : roomId)
                .teacherId(ownername != null && ownername.equals(TEACHER) ? ownerId : teacherId)
                .courseId(ownername != null && ownername.equals(COURSE) ? ownerId : courseId)
                .groupId(ownername != null && ownername.equals(GROUP) ? ownerId : groupId).build();

        if (ownername == null) {
            throw new UniversityException("undefined");
        }
        List<String> sequenceSteps = fillSequenceSetps(ownername);
        substitute = sequenceSteps.get(sequenceSteps.indexOf(substitute) + 1);

        if (ownername.equals(GROUP)) {
            model.addAttribute(SELECTION, getPartsLectureOfGroup(sequenceSteps.indexOf(substitute), interimLecture)
                    .orElseThrow(() -> new UniversityException(ownername)));
        }

        if (ownername.equals(TEACHER)) {
            model.addAttribute(SELECTION, getPartsLectureOfTeacher(sequenceSteps.indexOf(substitute), interimLecture)
                    .orElseThrow(() -> new UniversityException(ownername)));
        }

        if (ownername.equals(ROOM)) {
            model.addAttribute(SELECTION, getPartsLectureOfRoom(sequenceSteps.indexOf(substitute), interimLecture)
                    .orElseThrow(() -> new UniversityException(ownername)));
        }

        model.addAttribute(LECTURE, interimLecture);
        model.addAttribute("substitute", substitute);
        model.addAttribute(NUMBER_OF_DAY, serialNumberPerDay);
        model.addAttribute("date", LocalDate.parse(cDate));
        model.addAttribute(TEACHER_ID, ownername.equals(TEACHER) ? ownerId : teacherId);
        model.addAttribute(LECTURE_ID, lectureId);
        model.addAttribute(ROOM_ID, ownername.equals(ROOM) ? ownerId : roomId);
        model.addAttribute(COURSE_ID, ownername.equals(COURSE) ? ownerId : courseId);
        model.addAttribute(GROUP_ID, ownername.equals(GROUP) ? ownerId : groupId);
        model.addAttribute(OWNERNAME, ownername);
        model.addAttribute(OWNER_ID, ownerId);
        model.addAttribute(START_DATE, LocalDate.parse(sDate));
        model.addAttribute(FINISH_DATE, LocalDate.parse(fDate));

        if (sequenceSteps.indexOf(substitute) != 3) {
            return "lectures/stringLectureParts";
        } else {
            return "lectures/modifyLectureParts";
        }
    }

    @SuppressWarnings("rawtypes")
    public Optional getPartsLectureOfGroup(int stepOfsequence, LectureDto interim) {
        Optional result = java.util.Optional.empty();

        if (stepOfsequence == 1) {
            result = Optional.of(roomServ.retrieveFreeByTime(interim.getDate(), interim.getSerialNumberPerDay()));
        } else if (stepOfsequence == 2) {
            result = Optional.of(courseServ.retrieveFreeByGroupWithFreeTeachersByTime(interim.getGroupId(),
                    interim.getDate(), interim.getSerialNumberPerDay()));
        } else if (stepOfsequence == 3) {
            result = Optional.of(teacherServ.retrieveFreeByGroupByCourseByTime(interim.getGroupId(),
                    interim.getCourseId(), interim.getDate(), interim.getSerialNumberPerDay()));
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    public Optional getPartsLectureOfTeacher(int stepOfsequence, LectureDto interim) {
        Optional result = java.util.Optional.empty();

        if (stepOfsequence == 1) {
            result = Optional.of(roomServ.retrieveFreeByTime(interim.getDate(), interim.getSerialNumberPerDay()));
        } else if (stepOfsequence == 2) {
            result = Optional.of(courseServ.retrieveFreeByTeacherWithFreeGroupsByTime(interim.getTeacherId(),
                    interim.getDate(), interim.getSerialNumberPerDay()));
        } else if (stepOfsequence == 3) {
            result = Optional.of(groupServ.retrieveFreeByTeacherByCourseByTime(interim.getTeacherId(),
                    interim.getCourseId(), interim.getDate(), interim.getSerialNumberPerDay()));
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    public Optional getPartsLectureOfRoom(int stepOfsequence, LectureDto interim) {
        Optional result = java.util.Optional.empty();

        if (stepOfsequence == 1) {
            result = Optional.of(groupServ.retrieveFreeByTime(interim.getDate(), interim.getSerialNumberPerDay()));
        } else if (stepOfsequence == 2) {
            result = Optional.of(courseServ.retrieveFreeByGroupWithFreeTeachersByTime(interim.getGroupId(),
                    interim.getDate(), interim.getSerialNumberPerDay()));
        } else if (stepOfsequence == 3) {
            result = Optional.of(teacherServ.retrieveFreeByGroupByCourseByTime(interim.getGroupId(),
                    interim.getCourseId(), interim.getDate(), interim.getSerialNumberPerDay()));
        }
        return result;
    }

    private List<String> fillSequenceSetps(String ownername) {
        List<String> sequenceSteps = new ArrayList<>();

        if (ownername != null && ownername.equals(GROUP)) {
            sequenceSteps = Arrays.asList(DATE_AND_TIME, ROOM, COURSE, TEACHER);
        } else if (ownername != null && ownername.equals(TEACHER)) {
            sequenceSteps = Arrays.asList(DATE_AND_TIME, ROOM, COURSE, GROUP);
        } else if (ownername != null && ownername.equals(ROOM)) {
            sequenceSteps = Arrays.asList(DATE_AND_TIME, GROUP, COURSE, TEACHER);
        }
        return sequenceSteps;
    }

    @Secured(Role.Recall.MODERATOR)
    @PatchMapping()
    public String reloadLecture(@ModelAttribute(LECTURE) LectureDto lectureDto,
            @RequestParam(OWNERNAME) String ownername, @RequestParam(OWNER_ID) Long ownerId,
            @RequestParam(START_DATE) String sDate, @RequestParam(FINISH_DATE) String fDate) {
        return UriComponentsBuilder.newInstance().scheme(REDIRECT)
                .pathSegment(LECTURES, ownername, ownerId.toString(), "day").queryParam(START_DATE, sDate)
                .queryParam(FINISH_DATE, fDate)
                .queryParam("page",
                        adapter.getPage(sDate, lectureServ.enter(mapperUtil.toEntity(lectureDto)).getDate()))
                .queryParam("mode", "creater").build().toString();
    }

    @Secured(Role.Recall.MODERATOR)
    @DeleteMapping("{id}")
    public String delete(@PathVariable(value = "id") Long id, Model model, @RequestParam(OWNERNAME) String ownername,
            @RequestParam(OWNER_ID) Long ownerId, @RequestParam("date") String date,
            @RequestParam(START_DATE) String sDate, @RequestParam(FINISH_DATE) String fDate) {
        lectureServ.remove(id);

        return UriComponentsBuilder.newInstance().scheme(REDIRECT).pathSegment(LECTURES).pathSegment(ownername)
                .pathSegment(ownerId.toString()).pathSegment("day").queryParam(START_DATE, sDate)
                .queryParam(FINISH_DATE, fDate).queryParam("page", adapter.getPage(sDate, LocalDate.parse(date)))
                .queryParam("mode", "creater").build().toString();
    }

    @Secured(Role.Recall.MODERATOR)
    @GetMapping("lecture{lectureId}/clone")
    String lookForUnoccupied(Model model, @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = OWNERNAME, required = false) String ownername,
            @RequestParam(value = OWNER_ID, required = false) String ownerId,
            @RequestParam(value = "sourcename", required = false) String sourcename,
            @RequestParam(value = "sourceId", required = false) Long sourceId, @PathVariable(LECTURE_ID) Long lectureId,
            @RequestParam(START_DATE) String sDate, @RequestParam(FINISH_DATE) String fDate,
            @RequestParam("date") String cDate,
            @RequestParam(value = NUMBER_OF_DAY, required = false) int serialNumberPerDay,
            @RequestParam(value = TEACHER_ID, required = false) Long teacherId,
            @RequestParam(value = ROOM_ID, required = false) Long roomId,
            @RequestParam(value = COURSE_ID, required = false) Long courseId,
            @RequestParam(value = GROUP_ID, required = false) Long groupId) {

        Map<Long, Course> allCourses = courseServ.retrieveAll().stream()
                .collect(Collectors.toMap(Course::getId, a -> a));
        Map<Long, Teacher> allTeachers = teacherServ.retrieveAll().stream()
                .collect(Collectors.toMap(Teacher::getId, a -> a));
        Map<Long, Group> allGroups = groupServ.retrieveAll().stream().collect(Collectors.toMap(Group::getId, a -> a));
        Map<Long, Room> allRoom = roomServ.retrieveAll().stream().collect(Collectors.toMap(Room::getId, a -> a));
        LectureDto interimLecture = LectureDto.builder().id(lectureId).serialNumberPerDay(serialNumberPerDay)
                .date(LocalDate.parse(cDate)).roomId(roomId).teacherId(teacherId).courseId(courseId).groupId(groupId)
                .build();

        model.addAttribute(FINISH_DATE, LocalDate.parse(fDate));
        model.addAttribute(START_DATE, LocalDate.parse(sDate));
        model.addAttribute("timeline", adapter.stretchMonthTimeline(LocalDate.parse(sDate), LocalDate.parse(fDate)));
        model.addAttribute("page", page != null ? page : START_POINT_OF_PAGINATION);
        model.addAttribute("sourcename", ownername != null ? ownername : sourcename);
        model.addAttribute("sourceId", ownerId != null ? ownerId : sourceId);
        model.addAttribute(OWNERNAME, LECTURE);
        model.addAttribute("owner", interimLecture);
        model.addAttribute("mode", "selector");
        model.addAttribute(MAP_OF_COURSE, allCourses);
        model.addAttribute(MAP_OF_TEACHER, allTeachers);
        model.addAttribute(MAP_OF_ROOM, allRoom);
        model.addAttribute(MAP_OF_GROUP, allGroups);
        model.addAttribute(TIMETABLE,
                adapter.getTimetable(groupId, teacherId, roomId, LocalDate.parse(sDate), LocalDate.parse(fDate)));
        model.addAttribute(LECTURE, new LectureDto());
        
        return "lectures/stringLectures";
    }

    @Secured(Role.Recall.MODERATOR)
    @PostMapping("clone")
    public String clone(Model model, @RequestParam(GROUP_ID) Long groupId, @RequestParam(TEACHER_ID) Long teacherId,
            @RequestParam(ROOM_ID) Long roomId, @RequestParam(COURSE_ID) Long courseId,
            @RequestParam("sourceId") Long sourceId, @RequestParam("sourcename") String sourcename,
            @RequestParam(START_DATE) String sDate, @RequestParam(FINISH_DATE) String fDate,
            @RequestParam("timeSpots") String[] timeSpots,
            @RequestParam(value = "markMoveOwner", required = false) Long oldId) {

        for (int i = 0; i < timeSpots.length; i++) {
            LectureDto newLecture = LectureDto.builder()
                    .id(i == 0 && oldId != null ? oldId : null)
                    .serialNumberPerDay(Integer.parseInt(timeSpots[i].split(" ")[0]))
                    .date(LocalDate.parse(timeSpots[i].split(" ")[1]))
                    .roomId(roomId).teacherId(teacherId)
                    .courseId(courseId).groupId(groupId).build();
            
            lectureServ.enter(mapperUtil.toEntity(newLecture));
        }
        SortedMap<LocalDate, ArrayList<LectureDto>> timetablePerDay = adapter
                .stuffTimetableByOwnername(LocalDate.parse(sDate), LocalDate.parse(fDate), sourceId, sourcename);
        Map<Long, Course> allCourses = courseServ.retrieveAll().stream()
                .collect(Collectors.toMap(Course::getId, a -> a));
        Map<Long, Teacher> allTeachers = teacherServ.retrieveAll().stream()
                .collect(Collectors.toMap(Teacher::getId, a -> a));
        Map<Long, Group> allGroups = groupServ.retrieveAll().stream().collect(Collectors.toMap(Group::getId, a -> a));
        Map<Long, Room> allRoom = roomServ.retrieveAll().stream().collect(Collectors.toMap(Room::getId, a -> a));

        model.addAttribute("page", START_POINT_OF_PAGINATION);
        model.addAttribute("showcase", "");
        model.addAttribute("timeview", MONTH);
        model.addAttribute("mode", "");
        model.addAttribute(MAP_OF_COURSE, allCourses);
        model.addAttribute(MAP_OF_TEACHER, allTeachers);
        model.addAttribute(MAP_OF_ROOM, allRoom);
        model.addAttribute(MAP_OF_GROUP, allGroups);
        model.addAttribute("timetablePerDay", timetablePerDay);
        model.addAttribute("timetablePerMonth", adapter.wrapForMonthView(timetablePerDay));
        model.addAttribute(START_DATE, LocalDate.parse(sDate));
        model.addAttribute(FINISH_DATE, LocalDate.parse(fDate));

        return UriComponentsBuilder.newInstance().scheme(REDIRECT).pathSegment(LECTURES).pathSegment(sourcename)
                .pathSegment(sourceId.toString()).pathSegment(MONTH).queryParam(START_DATE, sDate)
                .queryParam(FINISH_DATE, fDate).build().toString();
    }

}
