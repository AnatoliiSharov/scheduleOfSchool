package ua.com.foxminded.asharov.universityschedule.repository.custom.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.asharov.universityschedule.entity.Group;
import ua.com.foxminded.asharov.universityschedule.repository.custom.CustomGroupRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class CustomGroupRepositoryImpl implements CustomGroupRepository {
    public static final String SELECT_BY_STUDENT_ID = "SELECT g FROM Group g JOIN g.students s WHERE s.id=:studentId";
    public static final String SELECT_FREE_BY_TIME = "SELECT g FROM Group g WHERE g NOT IN(SELECT l.group FROM Lecture l WHERE l.date=:date AND l.serialNumberPerDay=:serialNumberPerDay)";
    public static final String SELECT_FREE_BY_TIME_BY_TEACHER_ID = "SELECT g FROM Group g JOIN g.educations e JOIN e.course c JOIN c.accreditations a JOIN a.teacher t WHERE t.id=:teacherId AND g NOT IN(SELECT l.group FROM Lecture l WHERE l.date=:date AND l.serialNumberPerDay=:serialNumberPerDay)";
    public static final String SELECT_FREE_BY_TEACHER_BY_COURSE_BY_TIME = "SELECT g FROM Group g JOIN g.educations e JOIN e.course c JOIN c.accreditations a JOIN a.teacher t WHERE t.id=:teacherId AND c.id=:courseId AND g NOT IN(SELECT l.group FROM Lecture l WHERE l.date=:date AND l.serialNumberPerDay=:serialNumberPerDay)";

    public static final String COURSE_ID = "courseId";
    public static final String GROUP_ID = "groupId";
    public static final String TEACHER_ID = "teacherId";
    public static final String DATE = "date";
    public static final String NUMBER_PER_DAY = "serialNumberPerDay";

    @Autowired
    EntityManagerFactory factory;

    @PersistenceContext
    EntityManager em;

    @SuppressWarnings("unchecked")
    @Override
    public List<Group> findFreeWithTime(LocalDate currentDate, int serialNumberPerDay) {
        log.debug("CustomGroupRepositoryImpl/findFreeWithTime started with currentDate = {}, serialNumberPerDay = {}", currentDate, serialNumberPerDay);
        return em.createQuery(SELECT_FREE_BY_TIME).setParameter(NUMBER_PER_DAY, serialNumberPerDay)
                .setParameter(DATE, currentDate).getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Group> findFreeWithTimeWithTeacherId(Long teacherId, LocalDate currentDate, int serialNumberPerDay) {
        log.debug("CustomGroupRepositoryImpl/findFreeWithTimeWithTeacherId started with teacherId = {}, currentDate = {}, serialNumberPerDay = {}", teacherId, currentDate, serialNumberPerDay);
        return new ArrayList<>(new HashSet<>(em.createQuery(SELECT_FREE_BY_TIME_BY_TEACHER_ID)
                .setParameter(TEACHER_ID, teacherId).setParameter(NUMBER_PER_DAY, serialNumberPerDay)
                .setParameter(DATE, currentDate).getResultList()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Group> findFreeWithTeacherWithCourseWithTime(Long teacherId, Long courseId, LocalDate date, int serialNumberPerDay) {
        log.debug("CustomGroupRepositoryImpl/findFreeWithTeacherWithCourseWithTime started with teacherId = {}, courseId = {}, date = {}, serialNumberPerDay = {}", teacherId, courseId, date, serialNumberPerDay);
        return em.createQuery(SELECT_FREE_BY_TEACHER_BY_COURSE_BY_TIME).setParameter(TEACHER_ID, teacherId)
                .setParameter(COURSE_ID, courseId).setParameter(NUMBER_PER_DAY, serialNumberPerDay)
                .setParameter("date", date).getResultList();
    }

}
