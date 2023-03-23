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
@Table(name = "teachers")
@Data
@NoArgsConstructor
public class Teacher {
    public static final String TEACHER_ID = "teacher_id";
    public static final String TEACHER_FIRST_NAME = "teacher_firstname";
    public static final String TEACHER_LAST_NAME = "teacher_lastname";
    public static final String TEACHER_BODILY = "teacher";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = TEACHER_ID)
    private Long id;

    @Column(name = TEACHER_FIRST_NAME, length = 1478, nullable = false)
    @NotEmpty(message = "Teacher's first name cannot be empty")
    @Size(max = 1478, message = "The length of Teacher's first name cannot be more than 1478")
    private String firstName;

    @Column(name = TEACHER_LAST_NAME, length = 700, nullable = false)
    @NotEmpty(message = "Teacher's last name cannot be empty")
    @Size(max = 700, message = "The length of Teacher's last name cannot be more than 700")
    private String lastName;

    @OneToMany(mappedBy = TEACHER_BODILY, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Lecture> lectures;

    @OneToMany(mappedBy = TEACHER_BODILY, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Accreditation> accreditations;

    public Teacher(Long id, String firstName, String lastName) {
        super();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Teacher(String firstName, String lastName) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
