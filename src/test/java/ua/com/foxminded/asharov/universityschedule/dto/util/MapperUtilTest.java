package ua.com.foxminded.asharov.universityschedule.dto.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ua.com.foxminded.asharov.universityschedule.dto.CourseDto;
import ua.com.foxminded.asharov.universityschedule.dto.GroupDto;
import ua.com.foxminded.asharov.universityschedule.dto.LectureDto;
import ua.com.foxminded.asharov.universityschedule.dto.RoomDto;
import ua.com.foxminded.asharov.universityschedule.dto.StudentDto;
import ua.com.foxminded.asharov.universityschedule.dto.TeacherDto;
import ua.com.foxminded.asharov.universityschedule.entity.Course;
import ua.com.foxminded.asharov.universityschedule.entity.Group;
import ua.com.foxminded.asharov.universityschedule.entity.Lecture;
import ua.com.foxminded.asharov.universityschedule.entity.Room;
import ua.com.foxminded.asharov.universityschedule.entity.Student;
import ua.com.foxminded.asharov.universityschedule.entity.Teacher;
import ua.com.foxminded.asharov.universityschedule.service.CourseService;
import ua.com.foxminded.asharov.universityschedule.service.GroupService;
import ua.com.foxminded.asharov.universityschedule.service.RoomService;
import ua.com.foxminded.asharov.universityschedule.service.TeacherService;

@SpringBootTest(classes = {MapperUtil.class})
class MapperUtilTest {
    
    @MockBean
    TeacherService teacherServ;
    @MockBean
    GroupService groupServ;
    @MockBean
    RoomService roomServ;
    @MockBean
    CourseService courseServ;
    @MockBean
    ModelMapper modelMapper;
    
    @Autowired
    MapperUtil mapperUtil;
    
    DateTimeFormatter pattern;
    Group group;
    Teacher teacher;
    Course course;
    Room room;
    Student student;
    
    GroupDto groupDto;
    TeacherDto teacherDto;
    CourseDto courseDto;
    RoomDto roomDto;
    StudentDto studentDto;
    
    @BeforeEach
    void setUp() throws Exception {
       this.modelMapper = new ModelMapper();;
        pattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        group = new Group(10001L, "AA-11");
        teacher = new Teacher(10001L, "teacherFirstName1", "teacherLastName1");
        course = new Course(10001L, "Course1", "Description1");
        room = new Room(10001L, "address1", 10);
        student = new Student(10001L, group, "name", "surname");
        
        groupDto = new GroupDto(10001L, "AA-11");
        teacherDto = new TeacherDto(10001L, "teacherFirstName1", "teacherLastName1");
        courseDto = new CourseDto(10001L, "Course1", "Description1");
        roomDto = new RoomDto(10001L, "address1", 10);
        studentDto = new StudentDto(10001L, group.getId(), "name", "surname");
        
        
    }
        
    @Test
    void testToDtoLecture() {
        Lecture entity = new Lecture(10001L, 1, LocalDate.parse("01-01-0001", pattern), room, teacher, course, group);
        LectureDto dto = new LectureDto(10001L, 1, LocalDate.parse("0001-01-01"), room.getId(), teacher.getId(), course.getId(), group.getId());
        
        assertEquals(dto, mapperUtil.toDto(entity));
    }

    @Test
    void testToEntityLectureDto() {
        Lecture entity = new Lecture(10001L, 1, LocalDate.parse("01-01-0001", pattern), room, teacher, course, group);
        LectureDto dto = new LectureDto(10001L, 1, LocalDate.parse("0001-01-01"), room.getId(), teacher.getId(), course.getId(), group.getId());
        
        when(teacherServ.retrieveById(teacher.getId())).thenReturn(teacher);
        when(groupServ.retrieveById(group.getId())).thenReturn(group);
        when(courseServ.retrieveById(course.getId())).thenReturn(course);
        when(roomServ.retrieveById(room.getId())).thenReturn(room);
        
        assertEquals(entity, mapperUtil.toEntity(dto));
        
        verify(teacherServ).retrieveById(teacher.getId());
        verify(groupServ).retrieveById(group.getId());
        verify(courseServ).retrieveById(course.getId());
        verify(teacherServ).retrieveById(room.getId());
    }

    @Test
    void testToDtoCourse() {
           assertEquals(courseDto, mapperUtil.toDto(course));
    }

    @Test
    void testToEntityCourseDto() {
        assertEquals(course, mapperUtil.toEntity(courseDto));
        
    }

    @Test
    void testToDtoStudent() {
        assertEquals(studentDto, mapperUtil.toDto(student));
    }

    @Test
    void testToEntityStudentDto() {
        assertEquals(student, mapperUtil.toEntity(studentDto));
    }

    @Test
    void testToDtoGroup() {
        assertEquals(groupDto, mapperUtil.toDto(group));
    }

    @Test
    void testToEntityGroupDto() {
        assertEquals(group, mapperUtil.toEntity(groupDto));
    }

    @Test
    void testToDtoRoom() {
        assertEquals(roomDto, mapperUtil.toDto(room));
    }

    @Test
    void testToEntityRoomDto() {
        assertEquals(room, mapperUtil.toEntity(roomDto));
    }

    @Test
    void testToDtoTeacher() {
        assertEquals(teacherDto, mapperUtil.toDto(teacher));
    }

    @Test
    void testToEntityTeacherDto() {
        assertEquals(teacher, mapperUtil.toEntity(teacherDto));
    }

}
