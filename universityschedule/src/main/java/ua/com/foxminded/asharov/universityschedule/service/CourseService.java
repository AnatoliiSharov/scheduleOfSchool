package ua.com.foxminded.asharov.universityschedule.service;

import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.asharov.universityschedule.entity.Course;

import java.time.LocalDate;
import java.util.List;

public interface CourseService {
    @Transactional(readOnly = true)
    List<Course> retrieveByAccreditedTeacher(Long teacherId);

    @Transactional(readOnly = true)
    List<Course> retrieveByGroupId(Long groupId);

    @Transactional(readOnly = true)
    Course retrieveById(Long courseId);

    @Transactional(readOnly = true)
    List<Course> retrieveAll();

    void addAccreditedCourse(Long teacherId, Long courseId);

    void removeAccreditedCourse(Long teacherId, Long courseId);

    void addToGroup(Long courseId, Long groupId);

    void removeFromGroup(Long courseId, Long groupId);

    Course enter(Course course);

    void removeById(Long id);

    @Transactional(readOnly = true)
    List<Course> retrieveFreeByGroupWithFreeTeachersByTime(Long groupId, LocalDate currentDate, int serialNumberPerDay);

    @Transactional(readOnly = true)
    List<Course> retrieveFreeByTeacherWithFreeGroupsByTime(Long techerId, LocalDate currentDate,
            int serialNumberPerDay);

}
