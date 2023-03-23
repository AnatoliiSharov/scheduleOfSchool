package ua.com.foxminded.asharov.universityschedule.dto.util;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

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

@Component
public class MapperUtil {
    private final ModelMapper modelMapper;
    private final RoomService roomServ;
    private final TeacherService teacherServ;
    private final CourseService courseServ;
    private final GroupService groupServ;

    public MapperUtil(RoomService roomServ, TeacherService teacherServ, CourseService courseServ,
            GroupService groupServ) {
        this.modelMapper = new ModelMapper();
        this.roomServ = roomServ;
        this.teacherServ = teacherServ;
        this.courseServ = courseServ;
        this.groupServ = groupServ;
    }

    public LectureDto toDto(Lecture entity) {
        return modelMapper.map(entity, LectureDto.class);
    }

    public Lecture toEntity(LectureDto dto) {
        return Lecture.builder().id(dto.getId()).serialNumberPerDay(dto.getSerialNumberPerDay()).date(dto.getDate())
                .room(roomServ.retrieveById(dto.getRoomId())).teacher(teacherServ.retrieveById(dto.getTeacherId()))
                .group(groupServ.retrieveById(dto.getGroupId())).course(courseServ.retrieveById(dto.getCourseId()))
                .build();
    }

    public List<LectureDto> toDto(List<Lecture> entity) {
        return entity.stream().map(this::toDto).collect(Collectors.toList());
    }

    public CourseDto toDto(Course entity) {
        return modelMapper.map(entity, CourseDto.class);
    }

    public Course toEntity(CourseDto dto) {
        return modelMapper.map(dto, Course.class);
    }

    public StudentDto toDto(Student entity) {
        return StudentDto.builder().id(entity.getId()).groupId(entity.getGroup().getId())
                .firstName(entity.getFirstName()).lastName(entity.getLastName()).build();
    }

    public Student toEntity(StudentDto dto) {
        Group group = new Group();

        if (dto.getId() != null) {
            group = groupServ.retrieveById(dto.getGroupId());
        }
        return Student.builder().id(dto.getId()).group(group).firstName(dto.getFirstName()).lastName(dto.getLastName())
                .build();
    }

    public GroupDto toDto(Group entity) {
        return modelMapper.map(entity, GroupDto.class);
    }

    public Group toEntity(GroupDto dto) {
        return modelMapper.map(dto, Group.class);
    }

    public RoomDto toDto(Room entity) {
        return modelMapper.map(entity, RoomDto.class);
    }

    public Room toEntity(RoomDto dto) {
        return modelMapper.map(dto, Room.class);
    }

    public TeacherDto toDto(Teacher entity) {
        return modelMapper.map(entity, TeacherDto.class);
    }

    public Teacher toEntity(TeacherDto dto) {
        return modelMapper.map(dto, Teacher.class);
    }

}
