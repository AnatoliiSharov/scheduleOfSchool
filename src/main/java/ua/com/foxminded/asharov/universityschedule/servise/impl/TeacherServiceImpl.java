package ua.com.foxminded.asharov.universityschedule.servise.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.asharov.universityschedule.dao.CourseDao;
import ua.com.foxminded.asharov.universityschedule.dao.TeacherDao;
import ua.com.foxminded.asharov.universityschedule.model.Course;
import ua.com.foxminded.asharov.universityschedule.model.Teacher;

@Service
public class TeacherServiceImpl implements ua.com.foxminded.asharov.universityschedule.service.TeacherService {
    private static final Logger logger = LoggerFactory.getLogger(TeacherServiceImpl.class);

    private final TeacherDao teacherDao;
    private final CourseDao courseDao;

    public TeacherServiceImpl(TeacherDao teacherDao, CourseDao courseDao) {
        this.teacherDao = teacherDao;
        this.courseDao = courseDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Teacher> retrieveAll() {
        logger.debug("retrieveAllTeachers in TeacherServiceImpl started");
        return teacherDao.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Teacher> retrieveAccreditedTeachers(Long courseId) {
        logger.debug("retrieveAccreditedCourseTeachers in TeacherServiceImpl started with courseId = {}", courseId);
        return teacherDao.findByCoureId(courseId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Teacher> retrieveWithSimilarNames(String firstNamePart, String lastNamePart) {
        logger.debug("retrieveTeachersWithSimilarNames in TeacherServiceImpl started with firstNamePart = {}, lastNamePart = {}", firstNamePart, lastNamePart);
        return teacherDao.findByFullName(firstNamePart, lastNamePart);
    }

    @Override
    @Transactional(readOnly = true)
    public Teacher retrieveById(Long id) {
        logger.debug("retrieveTeacherById in TeacherServiceImpl started with id = {}", id);
        return teacherDao.findById(id).orElse(new Teacher("check data", "check data"));
    }

    @Override
    public Teacher enter(Teacher teacher) {
        logger.debug("enterTeacher in TeacherServiceImpl started with teacher = {}", teacher);
        return teacherDao.save(teacher);
    }

    @Override
    public void removeById(Long id) {
        logger.debug("removeById in TeacherServiceImpl started with teacherId = {}", id); 
        teacherDao.deleteById(id);
    }
    
    @Override
    public List<Teacher> retrieveFreeByTimeForCourse(Long courseId, LocalDate date, int numberPerDay) {
        logger.debug("retrieveSubstitutionsByTimeForCourse in TeacherServiceImpl started with courseId = {}, date = {}, numberPerDay = {}", courseId, date, numberPerDay);
        return teacherDao.findFreeOnesByTimeForCourse(courseId, date, numberPerDay);
    }

    @Override
    public List<Teacher> retrieveFreeByGroupByCourseByTime(Long groupId, Long courseId, LocalDate date, int numberPerDay) { 
        logger.debug("retrieveFreeByGroupByCourseByTime in TeacherServiceImpl started with groupId = {}, courseId = {}, date = {}, numberPerDay = {}", groupId, courseId, date, numberPerDay);
        return teacherDao.findFreeByGroupByCourseByTime(groupId, courseId, date, numberPerDay); 
    }

    @Override
    public List<Map<Teacher, Course>> retrieveFreeLinkedCoursesByTimeByGroupId(Long groupId, LocalDate currentDate,
            int serialNumberPerDay) {
        logger.debug(
                "retrieveFreeLinkedCoursesByTimeByGroupId in GroupServiceImpl started with Long groupId={}, LocalDate currentDate={}, int serialNumberPerDay={}",
                groupId, currentDate, serialNumberPerDay);

        List<Map<Teacher, Course>> result = new ArrayList<>();
        teacherDao.findFreeByTimeByGroupId(groupId, currentDate, serialNumberPerDay)
        .forEach(teacher -> courseDao.findByAccreditedTeacherForGroup(groupId, teacher.getId()).forEach(course -> {
            Map<Teacher, Course> tmp = new HashMap<>();
            tmp.put(teacher, course);
            result.add(tmp);
        }));
        return result;

    }

}
