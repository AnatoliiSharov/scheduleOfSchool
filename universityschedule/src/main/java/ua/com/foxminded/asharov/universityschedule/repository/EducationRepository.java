package ua.com.foxminded.asharov.universityschedule.repository;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.asharov.universityschedule.entity.Education;
import ua.com.foxminded.asharov.universityschedule.entity.EducationKey;

@Repository
@EnableJpaRepositories
public interface EducationRepository extends CrudRepository<Education, EducationKey> {

}
