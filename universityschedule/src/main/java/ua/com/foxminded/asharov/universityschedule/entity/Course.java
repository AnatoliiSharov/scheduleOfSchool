package ua.com.foxminded.asharov.universityschedule.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "courses")
@NoArgsConstructor
@Data
public class Course {
    public static final String COURSE_BODILY = "course";
    public static final String COURSE_ID = "course_id";
    public static final String COURSE_NAME = "course_name";
    public static final String COURSE_DESCRIPTION = "course_description";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COURSE_ID)
    private Long id;

    @Column(name = COURSE_NAME, unique = true, nullable = false)
    @NotEmpty(message = "Course's name cannot be empty.")
    @Size(min = 5, max = 30, message = "The length of Course's cannot be less than 5 and more than 30 symbols.")
    private String name;

    @Column(name = COURSE_DESCRIPTION, nullable = false)
    @NotEmpty(message = "Course's description cannot be empty.")
    @Size(max = 1000, message = "The ltngth of Course's description cannot be more than 1000 symbols.")
    private String description;

    @OneToMany(mappedBy = COURSE_BODILY, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Lecture> lectures;

    @OneToMany(mappedBy = COURSE_BODILY, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Accreditation> accreditations;

    @OneToMany(mappedBy = COURSE_BODILY, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Education> educations;

    public Course(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.lectures = null;
        this.accreditations = null;
        this.educations = null;
    }

    public Course(String name, String description) {
        this.id = null;
        this.name = name;
        this.description = description;
        this.lectures = null;
        this.accreditations = null;
        this.educations = null;
    }

}
