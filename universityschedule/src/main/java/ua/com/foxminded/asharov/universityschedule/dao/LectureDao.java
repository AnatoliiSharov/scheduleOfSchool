package ua.com.foxminded.asharov.universityschedule.dao;

import ua.com.foxminded.asharov.universityschedule.model.Lecture;

import java.time.LocalDate;
import java.util.List;

public interface LectureDao extends CrudEntityDao<Lecture, Long> {

    List<Lecture> findGroupSchedule(Long groupId, LocalDate startDate, LocalDate finishDate);

    List<Lecture> findTeacherSchedule(Long teacherId, LocalDate startDate, LocalDate finishDate);

    List<Lecture> findRoomSchedule(Long roomId, LocalDate startDate, LocalDate finishDate);

}
