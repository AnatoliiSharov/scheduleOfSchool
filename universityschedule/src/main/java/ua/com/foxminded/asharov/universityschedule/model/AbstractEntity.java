package ua.com.foxminded.asharov.universityschedule.model;

import java.util.Objects;

public abstract class AbstractEntity<I> implements Entity<I> {

    protected I id;

    protected AbstractEntity(I id) {
        this.id = id;
    }

    public I getId() {
        return id;
    }

    public void setId(I id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractEntity<?> other = (AbstractEntity<?>) obj;
        return Objects.equals(id, other.id);
    }

}
