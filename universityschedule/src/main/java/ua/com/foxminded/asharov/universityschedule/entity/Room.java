package ua.com.foxminded.asharov.universityschedule.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "rooms")
@NoArgsConstructor
@Data
public class Room {
    public static final String ROOM_ID = "room_id";
    public static final String ROOM_ADDRESS = "room_address";
    public static final String ROOM_CAPACITY = "room_capacity";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ROOM_ID)
    private Long id;

    @Column(name = ROOM_ADDRESS, length = 30, nullable = false)
    @NotEmpty(message = "Room's name cannot be empty")
    @Size(max = 30, message = "The length of Room's name cannot be empty")
    private String address;

    @Column(name = ROOM_CAPACITY, nullable = false)
    @NotNull(message = "Room's capacity cannot be null")
    @Max(value = 1000, message = "Room's capacity is number with value less than 1000")
    private Integer capacity;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Lecture> lectures;

    public Room(Long id, String address, Integer capacity) {
        this.id = id;
        this.address = address;
        this.capacity = capacity;
    }

}
