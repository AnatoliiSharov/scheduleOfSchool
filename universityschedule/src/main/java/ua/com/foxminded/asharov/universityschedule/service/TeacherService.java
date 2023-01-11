package ua.com.foxminded.asharov.universityschedule.service;

import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.asharov.universityschedule.model.Course;
import ua.com.foxminded.asharov.universityschedule.model.Teacher;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TeacherService {
    @Transactional(readOnly = true)
    List<Teacher> retrieveAll();

    @Transactional(readOnly = true)
    List<Teacher> retrieveAccreditedTeachers(Long courseId);

    @Transactional(readOnly = true)
    List<Teacher> retrieveWithSimilarNames(String firstNamePart, String lastNamePart);

    @Transactional(readOnly = true)
    Teacher retrieveById(Long id);
    
    @Transactional(readOnly = true)
    List<Teacher> retrieveFreeByTimeForCourse(Long courseId, LocalDate date, int numberPerDay);

    Teacher enter(Teacher teacher);

    void removeById(Long id);

    @Transactional(readOnly = true)
    List<Teacher> retrieveFreeByGroupByCourseByTime(Long groupId, Long courseId, LocalDate date,
            int numberPerDay);
    
    @Transactional(readOnly = true)
    List<Map<Teacher, Course>> retrieveFreeLinkedCoursesByTimeByGroupId(Long groupId, LocalDate currentDate, int serialNumberPerDay);

}
