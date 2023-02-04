package ua.com.foxminded.asharov.universityschedule.dao;

import ua.com.foxminded.asharov.universityschedule.model.Course;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CourseDao extends CrudEntityDao<Course, Long> {

    Optional<Course> findByName(String name);

    List<Course> findByAccreditedTeacher(Long teacherId);

    void linkCourseAndTeacher(Long teacherId, Long courseId);

    void ripCourseAndTeacher(Long teacherId, Long courseId);

    List<Course> findByGroupId(Long groupId);

    void linkCourseAndGroup(Long courseId, Long groupId);

    void ripCourseAndGroup(Long courseId, Long groupId);

    List<Course> findByAccreditedTeacherForGroup(Long groupId, Long teacherId);

    List<Course> findFreeByGroupWithFreeTeachersByTime(Long groupId, LocalDate currentDate, int serialNumberPerDay);

    List<Course> findFreeByTeacherWithAllFreeGroupsByTime(Long techerId, LocalDate currentDate,
                                                          int serialNumberPerDay);

}
