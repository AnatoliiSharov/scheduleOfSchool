package ua.com.foxminded.asharov.universityschedule.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDto {
    private Long id;
    @NotEmpty(message = "Group's name cannot be empty")
    @Size(max = 30, message = "The length of Group's name cannot be more then 30 symbols")
    private String name;

    public GroupDto(String name) {
        super();
        this.name = name;
    }

}
