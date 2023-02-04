package ua.com.foxminded.asharov.universityschedule.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.asharov.universityschedule.entity.Teacher;
import ua.com.foxminded.asharov.universityschedule.exception.UniversityException;
import ua.com.foxminded.asharov.universityschedule.repository.TeacherRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TeacherServiceImpl implements ua.com.foxminded.asharov.universityschedule.service.TeacherService {
    private static final Logger logger = LoggerFactory.getLogger(TeacherServiceImpl.class);

    private final TeacherRepository teacherRep;

    public TeacherServiceImpl(TeacherRepository teacherRep) {
        this.teacherRep = teacherRep;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Teacher> retrieveAll() {
        logger.debug("retrieveAllTeachers in TeacherServiceImpl started");
        return StreamSupport.stream(teacherRep.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Teacher> retrieveAccreditedTeachers(Long courseId) {
        logger.debug("retrieveAccreditedCourseTeachers in TeacherServiceImpl started with courseId = {}", courseId);
        return teacherRep.findWithCoureId(courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public Teacher retrieveById(Long id) {
        logger.debug("retrieveTeacherById in TeacherServiceImpl started with id = {}", id);
        return teacherRep.findById(id).orElseThrow(() -> new UniversityException(
                "don't find Teacher with TeacherServiceImpl/retrieveById/findById when id= " + id));
    }

    @Override
    public Teacher enter(Teacher teacher) {
        logger.debug("enterTeacher in TeacherServiceImpl started with teacherDto = {}", teacher);
        return teacherRep.save(teacher);
    }

    @Override
    public void removeById(Long id) {
        logger.debug("removeById in TeacherServiceImpl started with teacherId = {}", id);
        teacherRep.deleteById(id);
    }

    @Override
    public List<Teacher> retrieveFreeByTimeForCourse(Long courseId, LocalDate date, int numberPerDay) {
        logger.debug(
                "retrieveSubstitutionsByTimeForCourse in TeacherServiceImpl started with courseId = {}, date = {}, numberPerDay = {}",
                courseId, date, numberPerDay);
        return teacherRep.findFreeOnesWithTimeForCourse(courseId, date, numberPerDay);
    }

    @Override
    public List<Teacher> retrieveFreeByGroupByCourseByTime(Long groupId, Long courseId, LocalDate date,
            int numberPerDay) {
        logger.debug(
                "retrieveFreeByGroupByCourseByTime in TeacherServiceImpl started with groupId = {}, courseId = {}, date = {}, numberPerDay = {}",
                groupId, courseId, date, numberPerDay);
        return teacherRep.findFreeWithGroupWithCourseByTime(groupId, courseId, date, numberPerDay);
    }

}
