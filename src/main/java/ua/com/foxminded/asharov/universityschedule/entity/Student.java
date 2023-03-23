package ua.com.foxminded.asharov.universityschedule.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@Builder
public class Student {
    public static final String STUDENT_ID = "student_id";
    public static final String STUDENT_GROUP_ID = "group_id";
    public static final String STUDENT_FIRST_NAME = "student_firstname";
    public static final String STUDENT_LAST_NAME = "student_lastname";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = STUDENT_ID)
    private Long id;

    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = STUDENT_GROUP_ID)
    @EqualsAndHashCode.Exclude
    @NotBlank(message = "the field cannot be empty")
    private Group group;

    @Column(name = STUDENT_FIRST_NAME, length = 1478, nullable = false)
    @NotEmpty(message = "Student's first name cannot be empty")
    @Size(max = 1478, message = "The length of Student's first name cannot be more than 1478")
    private String firstName;

    @Column(name = STUDENT_LAST_NAME, length = 700, nullable = false)
//    @NotEmpty(message = "Student's last name cannot be empty")
//    @Size(max = 700, message = "The length of Student's last name cannot be more than 700")
    private String lastName;

    public Student(Group group, String firstName, String lastName) {
        super();
        this.id = null;
        this.group = group;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Student(Long id, Group group, String firstName, String lastName) {
        super();
        this.id = id;
        this.group = group;
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
