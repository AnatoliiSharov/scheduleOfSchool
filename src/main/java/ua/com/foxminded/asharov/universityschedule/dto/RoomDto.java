package ua.com.foxminded.asharov.universityschedule.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
    private Long id;
    @NotEmpty(message = "Room's address cannot be empty")
    private String address;
    @NotNull(message = "Room's capacity cannot be null")
    @Max(value = 1000, message = "Room's capacity is number with value less than 1000")
    private Integer capacity;

    public RoomDto(String address, Integer capacity) {
        this.id = null;
        this.address = address;
        this.capacity = capacity;
    }

}
