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
import static ua.com.foxminded.asharov.universityschedule.entity.Group.GROUP_ID;

@SuppressWarnings("serial")
@Embeddable
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { GROUP_ID, COURSE_ID }) })
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EducationKey implements Serializable {

    @Column(name = GROUP_ID, nullable = false)
    Long groupId;

    @Column(name = COURSE_ID, nullable = false)
    Long courseId;

}
