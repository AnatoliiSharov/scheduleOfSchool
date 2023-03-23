package ua.com.foxminded.asharov.universityschedule.dao;

import ua.com.foxminded.asharov.universityschedule.model.Group;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GroupDao extends CrudEntityDao<Group, Long> {

    public Optional<Group> findByName(String name);

    public Optional<Group> findByStudentId(Long studentId);

    public List<Group> findFreeByTime(LocalDate currentDate, int serialNumberPerDay);

    public List<Group> findFreeByTimeByTeacherId(Long teacherId, LocalDate currentDate,
                                                 int serialNumberPerDay);

    public List<Group> findFreeFreeByTeacherByCourseByTime(Long teacherId, Long courseId, LocalDate currentDate,
                                                           int serialNumberPerDay);

}
