package ua.com.foxminded.asharov.universityschedule.repository.custom.impl;

import ua.com.foxminded.asharov.universityschedule.entity.Teacher;
import ua.com.foxminded.asharov.universityschedule.repository.custom.CustomTeacherRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Repository
public class CustomTeacherRepositoryImpl implements CustomTeacherRepository {
    static final String SELECT_BY_COURSE_ID = "SELECT t FROM Teacher t JOIN t.accreditations a JOIN a.course c WHERE c.id=:courseId";
    static final String SELECT_FREE_BY_COURSE_BY_TIME = "SELECT t FROM Teacher t JOIN t.accreditations a JOIN a.course c WHERE c.id=:courseId AND t NOT IN(SELECT l.teacher FROM Lecture l WHERE l.date=:date AND l.serialNumberPerDay=:serialNumberPerDay)";
    static final String SELECT_FREE_BY_GROUP_BY_TIME = "SELECT t FROM Teacher t JOIN t.accreditations a JOIN a.course c JOIN c.educations e JOIN e.group g WHERE g.id=:groupId AND t NOT IN(SELECT l.teacher FROM Lecture l WHERE l.date=:date AND l.serialNumberPerDay=:serialNumberPerDay)";
    static final String SELECT_FREE_BY_GROUP_BY_COURSE_BY_TIME = "SELECT t FROM Teacher t JOIN t.accreditations a JOIN a.course c JOIN c.educations e JOIN e.group g WHERE g.id=:groupId AND c.id=:courseId AND t NOT IN(SELECT l.teacher FROM Lecture l WHERE l.date=:date AND l.serialNumberPerDay=:serialNumberPerDay)";

    public static final String COURSE_ID = "courseId";
    public static final String GROUP_ID = "groupId";
    public static final String TEACHER_ID = "teacherId";
    public static final String DATE = "date";
    public static final String NUMBER_PER_DAY = "serialNumberPerDay";

    @PersistenceContext
    EntityManager em;

    @SuppressWarnings("unchecked")
    @Override
    public List<Teacher> findWithCoureId(Long courseId) {
        log.debug("CustomTeacherRepositoryImpl/findWithCoureId started with Long courseId = {}", courseId);
        return em.createQuery(SELECT_BY_COURSE_ID).setParameter(COURSE_ID, courseId).getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Teacher> findFreeOnesWithTimeForCourse(Long courseId, LocalDate date, int serialNumberPerDay) {
        log.debug(
                "CustomTeacherRepositoryImpl/findFreeOnesWithTimeForCourse started with courseId = {}, date = {}, serialNumberPerDay = {}",
                courseId, date, serialNumberPerDay);
        return em.createQuery(SELECT_FREE_BY_COURSE_BY_TIME).setParameter(COURSE_ID, courseId).setParameter(DATE, date)
                .setParameter(NUMBER_PER_DAY, serialNumberPerDay).getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Teacher> findFreeWithGroupWithCourseByTime(Long groupId, Long courseId, LocalDate date,
            int serialNumberPerDay) {
        log.debug(
                "CustomTeacherRepositoryImpl/findFreeWithGroupWithCourseByTime started with groupId = {}, courseId = {}, date = {}, serialNumberPerDay = {}",
                groupId, courseId, date, serialNumberPerDay);
        return em.createQuery(SELECT_FREE_BY_GROUP_BY_COURSE_BY_TIME).setParameter(GROUP_ID, groupId)
                .setParameter(COURSE_ID, courseId).setParameter(DATE, date)
                .setParameter(NUMBER_PER_DAY, serialNumberPerDay).getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Teacher> findFreeWithTimeWithGroupId(Long groupId, LocalDate date, int serialNumberPerDay) {
        log.debug(
                "CustomTeacherRepositoryImpl/findFreeWithTimeWithGroupId started with Long groupId = {}, LocalDate date = {}, int serialNumberPerDay = {}",
                groupId, date, serialNumberPerDay);
        return new ArrayList<>(
                new HashSet<>(em.createQuery(SELECT_FREE_BY_GROUP_BY_TIME).setParameter(GROUP_ID, groupId)
                        .setParameter(DATE, date).setParameter(NUMBER_PER_DAY, serialNumberPerDay).getResultList()));
    }

}
