package ua.com.foxminded.asharov.universityschedule.repository.custom.impl;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.asharov.universityschedule.entity.Course;
import ua.com.foxminded.asharov.universityschedule.repository.custom.CustomCourseRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class CustomCourseRepositoryImpl implements CustomCourseRepository {
    public static final String SELECT_BY_ACCREDITATED_TEACHER_ID = "SELECT c FROM Course c JOIN c.accreditations a JOIN a.teacher t WHERE t.id=:teacherId";
    public static final String SELECT_BY_EUCATING_GROUP_ID = "SELECT c FROM Course c JOIN c.educations e JOIN e.group g WHERE g.id =:groupId";
    public static final String SELECT_BY_ACCREDITATED_TEACHER_FOR_GROUP = "SELECT c FROM Course c WHERE c IN (SELECT e.course FROM Education e JOIN e.group g WHERE g.id=:groupId) AND c IN (SELECT a.course FROM Accreditation a JOIN a.teacher t WHERE t.id=:teacherId)";
    public static final String SELECT_FREE_BY_GROUP_ID_WITH_FREE_TEACHERS_BY_TIME = "SELECT c FROM Course c JOIN c.educations e JOIN e.group g JOIN c.accreditations a JOIN a.teacher t WHERE t IN(SELECT a.teacher FROM Accreditation a JOIN a.course c JOIN c.educations e JOIN e.group g WHERE g.id =:groupId) AND t NOT IN(SELECT l.teacher FROM Lecture l WHERE l.date=:date AND l.serialNumberPerDay=:serialNumberPerDay) AND g.id=:groupId";
    public static final String SELECT_FREE_BY_TEACHER_ID_WITH_FREE_GROUPS_BY_TIME = "SELECT c FROM Course c JOIN c.educations e JOIN e.group g JOIN c.accreditations a JOIN a.teacher t WHERE g IN(SELECT e.group FROM Education a JOIN a.course c JOIN c.accreditations a JOIN a.teacher t WHERE t.id =:teacherId) AND g NOT IN(SELECT l.group FROM Lecture l WHERE l.date=:date AND l.serialNumberPerDay=:serialNumberPerDay) AND t.id=:teacherId";
    
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
    public List<Course> findWithGroup(Long groupId) {
        log.debug("CustomCourseRepositoryImpl/findWithGroup started with roomDto = {}", groupId);
        return em.createQuery(SELECT_BY_EUCATING_GROUP_ID).setParameter(GROUP_ID, groupId).getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Course> findWithTeacher(Long teacherId) {
        log.debug("CustomCourseRepositoryImpl/findWithTeacher started with teacherId = {}", teacherId);
        return em.createQuery(SELECT_BY_ACCREDITATED_TEACHER_ID).setParameter(TEACHER_ID, teacherId).getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Course> findWithAccreditedTeacherForGroup(Long groupId, Long teacherId) {
        log.debug(
                "CustomCourseRepositoryImpl/findWithAccreditedTeacherForGroup started with Long groupId = {}, Long teacherId = {}",
                groupId, teacherId);
        return new ArrayList<>(new HashSet<>(em.createQuery(SELECT_BY_ACCREDITATED_TEACHER_FOR_GROUP)
                .setParameter(TEACHER_ID, teacherId).setParameter(GROUP_ID, groupId).getResultList()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Course> findFreeWithGroupWithFreeTeachersByTime(Long groupId, LocalDate currentDate,
            int serialNumberPerDay) {
        log.debug(
                "CustomCourseRepositoryImpl/findFreeWithGroupWithFreeTeachersByTime started with Long groupId = {}, LocalDate currentDate = {}, int serialNumberPerDay = {}",
                groupId, currentDate, serialNumberPerDay);
        return new ArrayList<>(new HashSet<>(em.createQuery(SELECT_FREE_BY_GROUP_ID_WITH_FREE_TEACHERS_BY_TIME)
                .setParameter(GROUP_ID, groupId).setParameter(DATE, currentDate)
                .setParameter(NUMBER_PER_DAY, serialNumberPerDay).getResultList()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Course> findFreeWithTeacherWithAllFreeGroupsByTime(Long techerId, LocalDate currentDate,
            int serialNumberPerDay) {
        log.debug(
                "CustomCourseRepositoryImpl/findFreeWithTeacherWithAllFreeGroupsByTime started with Long techerId={}, LocalDate currentDate={}, int serialNumberPerDay={}",
                techerId, currentDate, serialNumberPerDay);
        return (List<Course>) em.createQuery(SELECT_FREE_BY_TEACHER_ID_WITH_FREE_GROUPS_BY_TIME)
                .setParameter(TEACHER_ID, techerId).setParameter(DATE, currentDate)
                .setParameter(NUMBER_PER_DAY, serialNumberPerDay).getResultList().stream().distinct().collect(Collectors.toList());
    }

}
