package ua.com.foxminded.asharov.universityschedule.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;

import static ua.com.foxminded.asharov.universityschedule.entity.Course.COURSE_ID;
import static ua.com.foxminded.asharov.universityschedule.entity.Teacher.TEACHER_ID;

@SuppressWarnings("serial")
@Embeddable
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {TEACHER_ID, COURSE_ID})})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccreditationKey implements Serializable {

    @Column(name = COURSE_ID, nullable = false)
    Long courseId;

    @Column(name = TEACHER_ID, nullable = false)
    Long teacherId;
    
}
