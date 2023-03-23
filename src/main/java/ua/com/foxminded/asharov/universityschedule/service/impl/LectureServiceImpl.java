package ua.com.foxminded.asharov.universityschedule.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.asharov.universityschedule.entity.Lecture;
import ua.com.foxminded.asharov.universityschedule.exception.UniversityException;
import ua.com.foxminded.asharov.universityschedule.repository.GroupRepository;
import ua.com.foxminded.asharov.universityschedule.repository.LectureRepository;
import ua.com.foxminded.asharov.universityschedule.repository.RoomRepository;
import ua.com.foxminded.asharov.universityschedule.repository.TeacherRepository;

import java.time.LocalDate;
import java.util.*;

@Service
public class LectureServiceImpl implements ua.com.foxminded.asharov.universityschedule.service.LectureService {
    private static final Logger logger = LoggerFactory.getLogger(LectureServiceImpl.class);

    private final LectureRepository lectureRep;
    private final TeacherRepository teacherRep;
    private final GroupRepository groupRep;
    private final RoomRepository roomRep;

    public LectureServiceImpl(LectureRepository lectureRep, TeacherRepository teacherRep, GroupRepository groupRep, RoomRepository roomRep) {
        this.lectureRep = lectureRep;
        this.teacherRep = teacherRep;
        this.groupRep = groupRep;
        this.roomRep = roomRep;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lecture> retrieveTeacherLectures(Long teacherId, LocalDate startDate, LocalDate finishDate) {
        logger.debug(
            "retrieveTeacherLectures from LectureServiceImpl started with teacherId = {}, startDate = {}, finishDate = {}",
            teacherId, startDate, finishDate);
        return lectureRep.findAllByDateBetweenAndTeacher(startDate, finishDate, teacherRep.findById(teacherId).orElseThrow(() -> new UniversityException("not exist LectureServiceImpl/retrieveTeacherLectures/findById with teacherId="+teacherId)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lecture> retrieveGroupLectures(Long groupId, LocalDate startDate, LocalDate finishDate) {
        logger.debug(
            "retrieveGroupLectures from LectureServiceImpl started with groupId = {}, startDate = {}, finishDate = {}",
            groupId, startDate, finishDate);
        return lectureRep.findAllByDateBetweenAndGroup(startDate, finishDate, groupRep.findById(groupId).orElseThrow(() -> new UniversityException("not exist LectureServiceImpl/retrieveGroupLectures/findById with groupId="+groupId)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lecture> retrieveRoomLectures(Long roomId, LocalDate startDate, LocalDate finishDate) {
        logger.debug(
            "retrieveRoomLectures from LectureServiceImpl started with roomId = {}, startDate = {}, finishDate = {}",
            roomId, startDate, finishDate);
        return lectureRep.findAllByDateBetweenAndRoom(startDate, finishDate, roomRep.findById(roomId).orElseThrow(() -> new UniversityException("not exist LectureServiceImpl/retrieveRoomLectures/findById with roomId="+roomId)));
    }

    @Override
    @Transactional
    public Lecture substituteTeacher(Long teacherId, Long lectureId) {
        logger.debug("substituteTeacher from LectureServiceImpl started with teacherId = {}, lecture = {}", teacherId,
            lectureId);
        Lecture result = lectureRep.findById(lectureId).orElseThrow(() -> new UniversityException("not exist Lecture LectureServiceImpl/substituteTeacher/findById with teacherId="+lectureId));
        
        result.setTeacher(teacherRep.findById(teacherId).orElseThrow(() -> new UniversityException("not exist Teacher LectureServiceImpl/substituteTeacher/findById with teacherId="+teacherId)));
        return lectureRep.save(result);
    }

    @Override
    public Lecture retrieveLectureById(Long id) {
        logger.debug("retrieveLectureById from LectureServiceImpl started with id = {}", id);
        return lectureRep.findById(id).orElseThrow(() -> new UniversityException("not exist Lecture LectureServiceImpl/retrieveLectureById/findById with id="+id));
    }

    @Override
    public Lecture enter(Lecture lecture) {
        logger.debug("enter from LectureServiceImpl started with LectureDto lectureDto = {}", lecture);
        return lectureRep.save(lecture);
    }

    @Override
    public void remove(Long id) {
        logger.debug("remove from LectureServiceImpl started with Long id = {}", id);
        
        if(lectureRep.existsById(id)) {
        lectureRep.deleteById(id);
        } else {
            throw new UniversityException("not exist LectureServiceImpl/remove/existsById with id="+id);
        }
    }

    @Override
    public List<Lecture> retrieveAllByGroupIdByTeacherIdByRoomId(Long groupId, Long teacherId, Long roomId, LocalDate startDate, LocalDate finishDate) {
        logger.debug("remove from retrieveAllByGroupIdByTeacherIdByRoomId started with Long groupId = {}, Long teacherId = {}, Long roomId = {}, LocalDate startDate = {}, LocalDate finishDate = {}", groupId, teacherId, roomId, startDate, finishDate);
        Set<Lecture> result = new HashSet<>(lectureRep.findAllByDateBetweenAndGroup(startDate, finishDate, groupRep.findById(groupId).orElseThrow(() -> new UniversityException("not exist LectureServiceImpl/retrieveGroupLectures/findById with groupId="+groupId))));

        result.addAll(new HashSet<>(lectureRep.findAllByDateBetweenAndTeacher(startDate, finishDate, teacherRep.findById(teacherId).orElseThrow(() -> new UniversityException("not exist LectureServiceImpl/retrieveTeacherLectures/findById with teacherId="+teacherId)))));
        result.addAll(new HashSet<>(lectureRep.findAllByDateBetweenAndRoom(startDate, finishDate, roomRep.findById(roomId).orElseThrow(() -> new UniversityException("not exist LectureServiceImpl/retrieveRoomLectures/findById with roomId="+roomId)))));
        return new ArrayList<>(result);
    }
    
}
