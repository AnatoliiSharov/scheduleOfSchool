package ua.com.foxminded.asharov.universityschedule.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.asharov.universityschedule.entity.Accreditation;
import ua.com.foxminded.asharov.universityschedule.entity.Course;
import ua.com.foxminded.asharov.universityschedule.entity.Education;
import ua.com.foxminded.asharov.universityschedule.entity.Group;
import ua.com.foxminded.asharov.universityschedule.entity.Teacher;
import ua.com.foxminded.asharov.universityschedule.exception.ResourceNotFoundException;
import ua.com.foxminded.asharov.universityschedule.exception.UniversityException;
import ua.com.foxminded.asharov.universityschedule.repository.AccreditationRepository;
import ua.com.foxminded.asharov.universityschedule.repository.CourseRepository;
import ua.com.foxminded.asharov.universityschedule.repository.EducationRepository;
import ua.com.foxminded.asharov.universityschedule.repository.GroupRepository;
import ua.com.foxminded.asharov.universityschedule.repository.TeacherRepository;
import ua.com.foxminded.asharov.universityschedule.service.CourseService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRep;
    private final EducationRepository educationRep;
    private final TeacherRepository teacherRep;
    private final GroupRepository groupRep;
    private final AccreditationRepository accreditationRep;

    public CourseServiceImpl(CourseRepository courseRep, AccreditationRepository accreditationRep,
            EducationRepository educationRep, TeacherRepository teacherRep, GroupRepository groupRep) {
        this.courseRep = courseRep;
        this.accreditationRep = accreditationRep;
        this.educationRep = educationRep;
        this.teacherRep = teacherRep;
        this.groupRep = groupRep;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> retrieveByAccreditedTeacher(Long teacherId) {
        log.debug("retrieveByAccreditedTeacher in CourseServiceImpl with teacherId = {}", teacherId);
        return courseRep.findWithTeacher(teacherId);
    }

    @Override
    @Transactional(readOnly = true)
    public Course retrieveById(Long courseId) {
        log.debug("etrieveById in CourseServiceImpl started with courseId = {}", courseId);
        return courseRep.findById(courseId).orElseThrow(
                () -> new UniversityException("dont find Course with retrieveById when courseId = " + courseId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> retrieveAll() {
        log.debug("retrieveAllCourses in CourseServiceImpl started");
        return StreamSupport.stream(courseRep.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public void addAccreditedCourse(Long teacherId, Long courseId) {
        log.debug("addAccreditedCourse in CourseServiceImpl started with teacherId ={} courseId={}", teacherId,
                courseId);
        Teacher teacher = teacherRep.findById(teacherId).orElseThrow(() -> new ResourceNotFoundException("something with Teacher in addAccreditedCourse"));
        Course course = courseRep.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("something with Course in addAccreditedCourse"));

        accreditationRep.save(new Accreditation(course, teacher));
    }

    @Override
    public void removeAccreditedCourse(Long teacherId, Long courseId) {
        log.debug("removeAccreditedCourse in CourseServiceImpl started with teacherId ={} courseId={}", teacherId,
                courseId);
        Teacher teacher = teacherRep.findById(teacherId).orElseThrow(() -> new UniversityException(
                "dont find Teacher with removeAccreditedCourse/findById when teacherId = " + teacherId));
        Course course = courseRep.findById(courseId).orElseThrow(() -> new UniversityException(
                "dont find Course with removeAccreditedCourse/findById when courseId = " + courseId));

        accreditationRep.delete(new Accreditation(course, teacher));
    }

    @Override
    public void addToGroup(Long courseId, Long groupId) {
        log.debug("addToGroup in CourseServiceImpl started with courseId ={} and groupId={}", courseId, groupId);
        Group group = groupRep.findById(groupId).orElseThrow(
                () -> new UniversityException("dont find Group with addToGroup/findById when groupId = " + groupId));
        Course course = courseRep.findById(courseId).orElseThrow(
                () -> new UniversityException("dont find Course with addToGroup/findById when courseId = " + courseId));

        educationRep.save(new Education(group, course));
    }

    public void removeFromGroup(Long courseId, Long groupId) {
        log.debug("removeFromGroup in CourseServiceImpl started with courseId ={} and groupId={}", courseId, groupId);
        Group group = groupRep.findById(groupId).orElseThrow(() -> new UniversityException(
                "dont find Group with removeFromGroup/findById when groupId = " + groupId));
        Course course = courseRep.findById(courseId).orElseThrow(() -> new UniversityException(
                "dont find Course with removeFromGroup/findById when courseId = " + courseId));

        educationRep.delete(new Education(group, course));
    }

    @Override
    public List<Course> retrieveByGroupId(Long groupId) {
        log.debug("retrieveByGroupId in CourseServiceImpl started with groupId={}", groupId);
        return courseRep.findWithGroup(groupId);
    }

    @Override
    public Course enter(Course course) {
        log.debug("enter in CourseServiceImpl started with course={}", course);
        return courseRep.save(course);
    }

    @Override
    public void removeById(Long id) {
        log.debug("removeById in CourseServiceImpl started with id={}", id);
        courseRep.deleteById(id);
    }

    @Override
    public List<Course> retrieveFreeByGroupWithFreeTeachersByTime(Long groupId, LocalDate currentDate,
            int serialNumberPerDay) {
        log.debug(
                "retrieveFreeByTeacherWithFreeGroupsByTime in CourseServiceImpl started with Long groupId={}, LocalDate currentDate={}, int serialNumberPerDay={}",
                groupId, currentDate, serialNumberPerDay);
        return courseRep.findFreeWithGroupWithFreeTeachersByTime(groupId, currentDate, serialNumberPerDay);
    }

    @Override
    public List<Course> retrieveFreeByTeacherWithFreeGroupsByTime(Long techerId, LocalDate currentDate,
            int serialNumberPerDay) {
        log.debug(
                "retrieveFreeByTeacherWithFreeGroupsByTime in CourseServiceImpl started with Long techerId={}, LocalDate currentDate={}, int serialNumberPerDay={}",
                techerId, currentDate, serialNumberPerDay);
        return courseRep.findFreeWithTeacherWithAllFreeGroupsByTime(techerId, currentDate, serialNumberPerDay);
    }

}
