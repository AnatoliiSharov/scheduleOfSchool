package ua.com.foxminded.asharov.universityschedule.repository;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.asharov.universityschedule.entity.Group;
import ua.com.foxminded.asharov.universityschedule.repository.custom.CustomGroupRepository;

@Repository
@EnableJpaRepositories
public interface GroupRepository extends CustomGroupRepository, CrudRepository<Group, Long> {

}
