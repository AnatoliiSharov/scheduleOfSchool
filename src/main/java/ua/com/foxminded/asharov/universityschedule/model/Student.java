package ua.com.foxminded.asharov.universityschedule.model;

import java.util.Objects;

public class Student extends AbstractEntity<Long> {
    public static final String STUDENT_TABLE_NAME = "students";
    public static final String STUDENT_ID = "student_id";
    public static final String STUDENT_GROUP_ID = "group_id";
    public static final String STUDENT_FIRST_NAME = "student_firstname";
    public static final String STUDENT_LAST_NAME = "student_lastname";

    private String firstName;
    private String lastName;
    private Long groupId;

    public Student(Long id, Long groupId, String firstName, String lastName) {
        super(id);
        this.groupId = groupId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Student(Long id, String firstName, String lastName) {
        super(id);
        this.groupId = null;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Student(String firstName, String lastName) {
        this(null, null, firstName, lastName);
    }

    public Student() {
        this(null, null, null, null);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(firstName, groupId, lastName);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Student other = (Student) obj;
        return Objects.equals(firstName, other.firstName) && Objects.equals(groupId, other.groupId)
            && Objects.equals(lastName, other.lastName);
    }

    @Override
    public String toString() {
        return "Student [firstName=" + firstName + ", last" + "Name=" + lastName + ", groupId=" + groupId + ", Id=" + id
            + "]";
    }

}
