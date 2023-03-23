package ua.com.foxminded.asharov.universityschedule.dao;

import ua.com.foxminded.asharov.universityschedule.model.Entity;

public abstract class AbstractCrudEntityDao<T extends Entity<K>, K> implements CrudEntityDao<T, K> {

    public T save(T entity) {

        if (entity.getId() == null) {
            return create(entity);
        } else {
            return update(entity);
        }
    }

    protected abstract T update(T entity);

    protected abstract T create(T entity);

}
