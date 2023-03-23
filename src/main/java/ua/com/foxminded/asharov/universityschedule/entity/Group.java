package ua.com.foxminded.asharov.universityschedule.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "groups")
@Data
@NoArgsConstructor
public class Group {
    public static final String GROUP_BODILY = "group";
    public static final String GROUP_ID = "group_id";
    public static final String GROUP_NAME = "group_name";
    public static final String GROUP_TABLE_NAME = "groups";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = GROUP_ID)
    private Long id;

    @Column(name = GROUP_NAME, length = 30, nullable = false)
    @NotEmpty(message = "Group's name cannot be empty")
    @Size(max = 30, message = "The length of Group's name cannot be more then 30 symbols")
    private String name;

    @OneToMany(mappedBy = GROUP_BODILY, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Student> students;

    @OneToMany(mappedBy = GROUP_BODILY, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Lecture> lectures;

    @OneToMany(mappedBy = GROUP_BODILY, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Education> educations;

    public Group(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
