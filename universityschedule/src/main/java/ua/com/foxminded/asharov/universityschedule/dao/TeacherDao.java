package ua.com.foxminded.asharov.universityschedule.dao;

import java.time.LocalDate;
import java.util.List;

import ua.com.foxminded.asharov.universityschedule.model.Teacher;

public interface TeacherDao extends CrudEntityDao<Teacher, Long> {

    List<Teacher> findByFullName(String firstName, String lastName);

    List<Teacher> findByCoureId(Long courseId);

    List<Teacher> findFreeOnesByTimeForCourse(Long courseId, LocalDate date, int numberPerDay);

    List<Teacher> findFreeByGroupByCourseByTime(Long groupId, Long courseId, LocalDate date, int numberPerDay);

    List<Teacher> findFreeByTimeByGroupId(Long groupId, LocalDate currentDate, int serialNumberPerDay);

    
}
