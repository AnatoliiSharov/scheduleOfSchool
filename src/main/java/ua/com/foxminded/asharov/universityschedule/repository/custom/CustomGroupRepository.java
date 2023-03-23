package ua.com.foxminded.asharov.universityschedule.repository.custom;

import ua.com.foxminded.asharov.universityschedule.entity.Group;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface CustomGroupRepository {

    public List<Group> findFreeWithTime(LocalDate currentDate, int serialNumberPerDay);

    public List<Group> findFreeWithTimeWithTeacherId(Long teacherId, LocalDate currentDate, int serialNumberPerDay);

    public List<Group> findFreeWithTeacherWithCourseWithTime(Long teacherId, Long courseId, LocalDate currentDate,
            int serialNumberPerDay);

}
