package ua.com.foxminded.asharov.universityschedule.model;

import java.util.List;
import java.util.Objects;

public class Group extends AbstractEntity<Long> {
    public static final String GROUP_ID = "group_id";
    public static final String GROUP_NAME = "group_name";
    public static final String GROUP_TABLE_NAME = "groups";

    private String name;

    public Group(Long id, String name) {
        super(id);
        this.name = name;
    }

    public Group(String name) {
        this(null, name);
    }

    public Group() {
        this(null, null);
    }

    public Group getByIdFromList(List<Group> groups, Long groupId) {
        return groups.stream().filter(group -> group.getId() != 0 && group.getId().equals(groupId)).findFirst().orElse(null);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(name);
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
        Group other = (Group) obj;
        return Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return "Group [name=" + name + ", id=" + id + "]";
    }

}
