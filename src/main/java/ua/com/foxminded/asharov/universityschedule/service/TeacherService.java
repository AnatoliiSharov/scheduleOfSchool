package ua.com.foxminded.asharov.universityschedule.service;

import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.asharov.universityschedule.entity.Teacher;

import java.time.LocalDate;
import java.util.List;

public interface TeacherService {
    @Transactional(readOnly = true)
    List<Teacher> retrieveAll();

    @Transactional(readOnly = true)
    List<Teacher> retrieveAccreditedTeachers(Long courseId);

    @Transactional(readOnly = true)
    Teacher retrieveById(Long id);

    @Transactional(readOnly = true)
    List<Teacher> retrieveFreeByTimeForCourse(Long courseId, LocalDate date, int numberPerDay);

    @Transactional
    Teacher enter(Teacher teacher);

    @Transactional
    void removeById(Long id);

    @Transactional(readOnly = true)
    List<Teacher> retrieveFreeByGroupByCourseByTime(Long groupId, Long courseId, LocalDate date, int numberPerDay);

}
