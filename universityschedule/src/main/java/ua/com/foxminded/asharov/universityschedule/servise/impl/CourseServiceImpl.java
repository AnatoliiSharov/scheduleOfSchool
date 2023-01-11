package ua.com.foxminded.asharov.universityschedule.servise.impl;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.asharov.universityschedule.dao.CourseDao;
import ua.com.foxminded.asharov.universityschedule.model.Course;
import ua.com.foxminded.asharov.universityschedule.service.CourseService;

@Service
public class CourseServiceImpl implements CourseService {
    private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);

    private final CourseDao courseDao;

    public CourseServiceImpl(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> retrieveByAccreditedTeacher(Long teacherId) {
        logger.debug("retrieveByAccreditedTeacher in CourseServiceImpl with teacherId = {}", teacherId);
        return courseDao.findByAccreditedTeacher(teacherId);
    }

    @Override
    @Transactional(readOnly = true)
    public Course retrieveById(Long courseId) {
        logger.debug("etrieveById in CourseServiceImpl started with courseId = {}", courseId);
        return courseDao.findById(courseId).orElse(new Course("check data", "check data"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> retrieveAll() {
        logger.debug("retrieveAllCourses in CourseServiceImpl started");
        return courseDao.findAll();
    }

    @Override
    public void addAccreditedCourse(Long teacherId, Long courseId) {
        logger.debug("addAccreditedCourse in CourseServiceImpl started with teacherId ={} courseId={}", teacherId,
                courseId);
        courseDao.linkCourseAndTeacher(teacherId, courseId);
    }

    @Override
    public void removeAccreditedCourse(Long teacherId, Long courseId) {
        logger.debug("removeAccreditedCourse in CourseServiceImpl started with teacherId ={} courseId={}", teacherId,
                courseId);
        courseDao.ripCourseAndTeacher(teacherId, courseId);
    }

    @Override
    public void addToGroup(Long courseId, Long groupId) {
        logger.debug("addToGroup in CourseServiceImpl started with courseId ={} and groupId={}", courseId, groupId);
        courseDao.linkCourseAndGroup(courseId, groupId);
    }

    @Override
    public void removeFromGroup(Long courseId, Long groupId) {
        logger.debug("removeFromGroup in CourseServiceImpl started with courseId ={} and groupId={}", courseId,
                groupId);
        courseDao.ripCourseAndGroup(courseId, groupId);
    }

    @Override
    public List<Course> retrieveByGroupId(Long groupId) {
        logger.debug("retrieveByGroupId in CourseServiceImpl started with groupId={}", groupId);
        return courseDao.findByGroupId(groupId);
    }

    @Override
    public Course enter(Course course) {
        logger.debug("enter in CourseServiceImpl started with course={}", course);
        return courseDao.save(course);
    }

    @Override
    public void removeById(Long id) {
        logger.debug("removeById in CourseServiceImpl started with id={}", id);
        courseDao.deleteById(id);
    }
    @Override
    public List<Course> retrieveFreeByGroupWithFreeTeachersByTime(Long groupId, LocalDate currentDate,
            int serialNumberPerDay) {
        return courseDao.findFreeByGroupWithFreeTeachersByTime(groupId, currentDate,
                serialNumberPerDay);
    }
    @Override
    public List<Course> retrieveFreeByTeacherWithFreeGroupsByTime(Long techerId, LocalDate currentDate,
            int serialNumberPerDay) {
        return courseDao.findFreeByTeacherWithAllFreeGroupsByTime(techerId, currentDate,
                serialNumberPerDay);
    }

}
