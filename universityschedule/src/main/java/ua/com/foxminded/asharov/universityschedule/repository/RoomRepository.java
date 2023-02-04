package ua.com.foxminded.asharov.universityschedule.repository;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.asharov.universityschedule.entity.Room;
import ua.com.foxminded.asharov.universityschedule.repository.custom.CustomRoomRepository;

@Repository
@EnableJpaRepositories
public interface RoomRepository extends CustomRoomRepository, CrudRepository<Room, Long> {

}
