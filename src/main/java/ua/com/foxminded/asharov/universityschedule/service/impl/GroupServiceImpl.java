package ua.com.foxminded.asharov.universityschedule.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.asharov.universityschedule.entity.Group;
import ua.com.foxminded.asharov.universityschedule.exception.UniversityException;
import ua.com.foxminded.asharov.universityschedule.repository.GroupRepository;
import ua.com.foxminded.asharov.universityschedule.repository.StudentRepository;
import ua.com.foxminded.asharov.universityschedule.service.GroupService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class GroupServiceImpl implements GroupService {
    private static final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

    private GroupRepository groupRep;
    private StudentRepository studentRep;

    public GroupServiceImpl(GroupRepository groupRep, StudentRepository studentRep) {
        this.groupRep = groupRep;
        this.studentRep = studentRep;
    }

    @Override
    @Transactional(readOnly = true)
    public Group retrieveGroupByStudentId(Long studentId) {
        logger.debug("retrieveGroupByStudentId in GroupServiceImpl started with studentId = {}", studentId);
        return studentRep.findById(studentId).orElseThrow(() -> new UniversityException(
                "dont find Student with GroupServiceImpl/retrieveGroupByStudentId/findWithStudentId when studentId = "
                        + studentId))
                .getGroup();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Group> retrieveAll() {
        logger.debug("retrieveAllGroups in GroupServiceImpl started");
        return StreamSupport.stream(groupRep.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Group retrieveById(Long groupId) {
        logger.debug("retrieveGroupNameById in GroupServiceImpl started with groupId = {}", groupId);
        return groupRep.findById(groupId).orElseThrow(() -> new UniversityException(
                "dont find Group with GroupServiceImpl/retrieveById/findById when groupId = " + groupId));
    }

    @Override
    public Group enter(Group group) {
        logger.debug("enter in GroupServiceImpl started with group = {}", group);
        return groupRep.save(group);
    }

    @Override
    public void removeById(Long id) {
        logger.debug("remiveById in GroupServiceImpl started with id = {}", id);
        if (groupRep.existsById(id)) {
            groupRep.deleteById(id);
        } else {
            throw new UniversityException(
                    "dont find Group with GroupServiceImpl/removeById/existsById when id = " + id);
        }
    }

    @Override
    public List<Group> retrieveFreeByTime(LocalDate currentDate, int serialNumberPerDay) {
        logger.debug(
                "retrieveSubstitutesByTime in GroupServiceImpl started with LocalDate currentDate = {}, int serialNumberPerDay = {}",
                currentDate, serialNumberPerDay);
        return groupRep.findFreeWithTime(currentDate, serialNumberPerDay);
    }

    @Override
    public List<Group> retrieveFreeByTeacherByCourseByTime(Long teacherId, Long courseId, LocalDate currentDate,
            int serialNumberPerDay) {
        logger.debug(
                "retrieveFreeByTeacherByCourseByTime in GroupServiceImpl started with Long teacherId={}, Long courseId={}, LocalDate currentDate={}, int serialNumberPerDay={}",
                teacherId, courseId, currentDate, serialNumberPerDay);
        return groupRep.findFreeWithTeacherWithCourseWithTime(teacherId, courseId, currentDate, serialNumberPerDay);
    }

}
