package ua.com.foxminded.asharov.universityschedule.repository;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.asharov.universityschedule.entity.Accreditation;
import ua.com.foxminded.asharov.universityschedule.entity.AccreditationKey;

@Repository
@EnableJpaRepositories
public interface AccreditationRepository extends CrudRepository<Accreditation, AccreditationKey> {

}
