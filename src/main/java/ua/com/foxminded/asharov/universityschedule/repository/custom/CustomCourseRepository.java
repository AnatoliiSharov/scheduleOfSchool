package ua.com.foxminded.asharov.universityschedule.repository.custom;

import org.springframework.stereotype.Repository;
import ua.com.foxminded.asharov.universityschedule.entity.Course;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CustomCourseRepository {

    List<Course> findWithGroup(Long groupId);

    List<Course> findWithTeacher(Long teacherId);

    List<Course> findWithAccreditedTeacherForGroup(Long groupId, Long teacherId);

    List<Course> findFreeWithGroupWithFreeTeachersByTime(Long groupId, LocalDate currentDate, int serialNumberPerDay);

    List<Course> findFreeWithTeacherWithAllFreeGroupsByTime(Long techerId, LocalDate currentDate,
            int serialNumberPerDay);

}
