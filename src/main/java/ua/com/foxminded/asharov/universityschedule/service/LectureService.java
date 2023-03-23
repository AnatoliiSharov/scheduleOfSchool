package ua.com.foxminded.asharov.universityschedule.service;

import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.asharov.universityschedule.entity.Lecture;

import java.time.LocalDate;
import java.util.List;

public interface LectureService {
    @Transactional(readOnly = true)
    List<Lecture> retrieveTeacherLectures(Long teacherId, LocalDate startDate, LocalDate finishDate);

    @Transactional(readOnly = true)
    List<Lecture> retrieveGroupLectures(Long groupId, LocalDate startDate, LocalDate finishDate);

    @Transactional(readOnly = true)
    List<Lecture> retrieveRoomLectures(Long roomId, LocalDate startDate, LocalDate finishDate);

    @Transactional(readOnly = true)
    Lecture substituteTeacher(Long teacherId, Long lectureId);

    @Transactional(readOnly = true)
    List<Lecture> retrieveAllByGroupIdByTeacherIdByRoomId(Long groupId, Long teacherId, Long roomId,
            LocalDate startDate, LocalDate finishDate);

    @Transactional
    Lecture enter(Lecture lecture);

    @Transactional
    Lecture retrieveLectureById(Long id);

    @Transactional
    void remove(Long id);

}
