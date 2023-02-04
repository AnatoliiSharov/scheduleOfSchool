package ua.com.foxminded.asharov.universityschedule.service;

import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.asharov.universityschedule.entity.Group;

import java.time.LocalDate;
import java.util.List;

public interface GroupService {
    @Transactional(readOnly = true)
    Group retrieveGroupByStudentId(Long studentId);

    @Transactional(readOnly = true)
    List<Group> retrieveAll();

    @Transactional(readOnly = true)
    Group retrieveById(Long groupId);

    Group enter(Group group);

    void removeById(Long id);

    @Transactional(readOnly = true)
    List<Group> retrieveFreeByTime(LocalDate currentDate, int serialNumberPerDay);

    @Transactional(readOnly = true)
    List<Group> retrieveFreeByTeacherByCourseByTime(Long teacherId, Long courseId, LocalDate currentDate,
            int serialNumberPerDay);

}
