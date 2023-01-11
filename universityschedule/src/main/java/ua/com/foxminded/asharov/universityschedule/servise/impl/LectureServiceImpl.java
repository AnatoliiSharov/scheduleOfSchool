package ua.com.foxminded.asharov.universityschedule.servise.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.asharov.universityschedule.dao.LectureDao;
import ua.com.foxminded.asharov.universityschedule.model.Lecture;

@Service
public class LectureServiceImpl implements ua.com.foxminded.asharov.universityschedule.service.LectureService {
    private static final Logger logger = LoggerFactory.getLogger(LectureServiceImpl.class);

    private final LectureDao lectureDao;

    public LectureServiceImpl(LectureDao lectureDao) {
        this.lectureDao = lectureDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lecture> retrieveTeacherLectures(Long teacherId, LocalDate startDate, LocalDate finishDate) {
        logger.debug(
                "retrieveTeacherLectures from LectureServiceImpl started with teacherId = {}, startDate = {}, finishDate = {}",
                teacherId, startDate, finishDate);
        return lectureDao.findTeacherSchedule(teacherId, startDate, finishDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lecture> retrieveGroupLectures(Long groupId, LocalDate startDate, LocalDate finishDate) {
        logger.debug(
                "retrieveGroupLectures from LectureServiceImpl started with groupId = {}, startDate = {}, finishDate = {}",
                groupId, startDate, finishDate);
        return lectureDao.findGroupSchedule(groupId, startDate, finishDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lecture> retrieveRoomLectures(Long roomId, LocalDate startDate, LocalDate finishDate) {
        logger.debug(
                "retrieveRoomLectures from LectureServiceImpl started with roomId = {}, startDate = {}, finishDate = {}",
                roomId, startDate, finishDate);
        return lectureDao.findRoomSchedule(roomId, startDate, finishDate);
    }

    @Override
    @Transactional
    public Lecture substituteTeacher(Long teacherId, Lecture lecture) {
        logger.debug("substituteTeacher from LectureServiceImpl started with teacherId = {}, lecture = {}", teacherId,
                lecture);
        lecture.setTeacherId(teacherId);
        return lectureDao.save(lecture);
    }

    @Override
    public Lecture retrieveLectureById(Long id) {
        logger.debug("retrieveLectureById from LectureServiceImpl started with id = {}", id);
        return lectureDao.findById(id).orElse(new Lecture());
    }

    @Override
    public Lecture enter(Lecture lecture) {
        logger.debug("enter from LectureServiceImpl started with Lecture lecture = {}", lecture);
        return lectureDao.save(lecture);
    }

    @Override
    public void remove(Long id) {
        logger.debug("remove from LectureServiceImpl started with Long id = {}", id);
        lectureDao.deleteById(id);
    }
    
    @Override
    public SortedMap<LocalDate, SortedMap<Integer, List<Lecture>>> retrieveAllByGroupIdByTeacherIdByRoomId(Long groupId, Long teacherId, Long roomId, LocalDate startDate, LocalDate finishDate) {
        logger.debug("remove from retrieveAllByGroupIdByTeacherIdByRoomId started with Long groupId = {}, Long teacherId = {}, Long roomId = {}, LocalDate startDate = {}, LocalDate finishDate = {}", groupId, teacherId, roomId, startDate, finishDate);
        
        Set<Lecture> timetable = new HashSet<>(lectureDao.findGroupSchedule(groupId, startDate, finishDate));
        timetable.addAll(new HashSet<>(lectureDao.findTeacherSchedule(teacherId, startDate, finishDate)));
        timetable.addAll(new HashSet<>(lectureDao.findRoomSchedule(roomId, startDate, finishDate)));
        return timetable.stream().collect(Collectors.groupingBy(Lecture::getDate, TreeMap::new, Collectors
                .groupingBy(Lecture::getSerialNumberPerDay, TreeMap::new, Collectors.toCollection(ArrayList::new))));
    }

}
