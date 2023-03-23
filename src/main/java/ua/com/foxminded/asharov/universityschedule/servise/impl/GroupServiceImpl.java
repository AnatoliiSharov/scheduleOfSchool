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
import ua.com.foxminded.asharov.universityschedule.dao.GroupDao;
import ua.com.foxminded.asharov.universityschedule.model.Course;
import ua.com.foxminded.asharov.universityschedule.model.Group;
import ua.com.foxminded.asharov.universityschedule.service.GroupService;

@Service
public class GroupServiceImpl implements GroupService {
    private static final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

    private GroupDao groupDao;
    private CourseDao courseDao;

    public GroupServiceImpl(GroupDao groupDao, CourseDao courseDao) {
        this.groupDao = groupDao;
        this.courseDao = courseDao;
    }

    @Override
    @Transactional(readOnly = true)
    public Group retrieveGroupByStudentId(Long studentId) {
        logger.debug("retrieveGroupByStudentId in GroupServiceImpl started with studentId = {}", studentId);
        return groupDao.findByStudentId(studentId).orElse(new Group());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Group> retrieveAll() {
        logger.debug("retrieveAllGroups in GroupServiceImpl started");
        return groupDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Group retrieveById(Long groupId) {
        logger.debug("retrieveGroupNameById in GroupServiceImpl started with groupId = {}", groupId);
        return groupDao.findById(groupId).orElse(new Group());
    }

    @Override
    public Group enter(Group group) {
        logger.debug("enter in GroupServiceImpl started with group = {}", group);
        return groupDao.save(group);
    }

    @Override
    public void removeById(Long id) {
        logger.debug("remiveById in GroupServiceImpl started with id = {}", id);
        groupDao.deleteById(id);
    }

    @Override
    public List<Group> retrieveFreeByTime(LocalDate currentDate, int serialNumberPerDay) {
        logger.debug(
                "retrieveSubstitutesByTime in GroupServiceImpl started with LocalDate currentDate = {}, int serialNumberPerDay = {}",
                currentDate, serialNumberPerDay);
        return groupDao.findFreeByTime(currentDate, serialNumberPerDay);
    }

    @Override
    public List<Map<Group, Course>> retrieveFreeLinkedCoursesByTimeByTeacherId(Long teacherId, LocalDate currentDate,
            int serialNumberPerDay) {
        logger.debug(
                "findFreeByTimeByTeacherId in GroupServiceImpl started with Long teacherId={}, LocalDate currentDate={}, int serialNumberPerDay={}",
                teacherId, currentDate, serialNumberPerDay);

        List<Map<Group, Course>> result = new ArrayList<>();
        groupDao.findFreeByTimeByTeacherId(teacherId, currentDate, serialNumberPerDay)
        .forEach(group -> courseDao.findByAccreditedTeacherForGroup(group.getId(), teacherId).forEach(course -> {
            Map<Group, Course> tmp = new HashMap<>();
            tmp.put(group, course);
            result.add(tmp);
        }));
        return result;

    }
    @Override
    public List<Group> retrieveFreeByTeacherByCourseByTime(Long teacherId, Long courseId, LocalDate currentDate, int serialNumberPerDay) {
        return groupDao.findFreeFreeByTeacherByCourseByTime(teacherId, courseId, currentDate, serialNumberPerDay);
    }

}
