package ua.com.foxminded.asharov.universityschedule.model;

import java.util.Objects;

public class Course extends AbstractEntity<Long> {
    public static final String COURSE_TABLE_NAME = "courses";
    public static final String COURSE_ID = "course_id";
    public static final String COURSE_NAME = "course_name";
    public static final String COURSE_DESCRIPTION = "course_description";

    private String name;
    private String description;

    public Course(Long id, String name, String description) {
        super(id);
        this.name = name;
        this.description = description;
    }

    public Course(String name, String description) {
        this(null, name, description);
    }

    public Course() {
        this(null, null, null);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String courseName) {
        this.name = courseName;
    }

    public void setDescription(String courseDescription) {
        this.description = courseDescription;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(description, name);
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
        Course other = (Course) obj;
        return Objects.equals(description, other.description) && Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return "Course [ id=" + id + ", name=" + name + ", description=" + description + "]\n";
    }

}
