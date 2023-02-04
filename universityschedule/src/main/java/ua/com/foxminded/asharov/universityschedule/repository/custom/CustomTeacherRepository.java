package ua.com.foxminded.asharov.universityschedule.repository.custom;

import ua.com.foxminded.asharov.universityschedule.entity.Teacher;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface CustomTeacherRepository {

    List<Teacher> findWithCoureId(Long courseId);

    List<Teacher> findFreeOnesWithTimeForCourse(Long courseId, LocalDate date, int numberPerDay);

    List<Teacher> findFreeWithGroupWithCourseByTime(Long groupId, Long courseId, LocalDate date, int numberPerDay);

    List<Teacher> findFreeWithTimeWithGroupId(Long groupId, LocalDate currentDate, int serialNumberPerDay);

}
