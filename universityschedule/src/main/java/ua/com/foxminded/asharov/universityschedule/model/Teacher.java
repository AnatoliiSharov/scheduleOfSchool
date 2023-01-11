package ua.com.foxminded.asharov.universityschedule.model;

import java.util.Objects;

public class Teacher extends AbstractEntity<Long> {
    public static final String TEACHER_TABLE_NAME = "teachers";
    public static final String TEACHER_ID = "teacher_id";
    public static final String TEACHER_FIRST_NAME = "teacher_firstname";
    public static final String TEACHER_LAST_NAME = "teacher_lastname";

    private String firstName;
    private String lastName;

    public Teacher(Long id, String firstName, String lastName) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Teacher(String firstName, String lastName) {
        this(null, firstName, lastName);
    }

    public Teacher() {
        this(null, null, null);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(firstName, lastName);
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
        Teacher other = (Teacher) obj;
        return Objects.equals(firstName, other.firstName) && Objects.equals(lastName, other.lastName);
    }

    @Override
    public String toString() {
        return "Teacher [firstName=" + firstName + ", last" + "Name=" + lastName + ", Id=" + id + "]";
    }

}
