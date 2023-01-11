package ua.com.foxminded.asharov.universityschedule.service;

import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.asharov.universityschedule.model.Lecture;

import java.time.LocalDate;
import java.util.List;
import java.util.SortedMap;

public interface LectureService {
    @Transactional(readOnly = true)
    List<Lecture> retrieveTeacherLectures(Long teacherId, LocalDate startDate, LocalDate finishDate);

    @Transactional(readOnly = true)
    List<Lecture> retrieveGroupLectures(Long groupId, LocalDate startDate, LocalDate finishDate);

    @Transactional(readOnly = true)
    List<Lecture> retrieveRoomLectures(Long roomId, LocalDate startDate, LocalDate finishDate);

    @Transactional(readOnly = true)
    Lecture substituteTeacher(Long teacherId, Lecture lecture);
    
    @Transactional(readOnly = true)
    Lecture retrieveLectureById(Long id);

    @Transactional
    Lecture enter(Lecture lecture);
    
    @Transactional
    void remove(Long id);

    @Transactional(readOnly = true)
    SortedMap<LocalDate, SortedMap<Integer, List<Lecture>>> retrieveAllByGroupIdByTeacherIdByRoomId(Long groupId,
            Long teacherId, Long roomId, LocalDate startDate, LocalDate finishDate);

}
