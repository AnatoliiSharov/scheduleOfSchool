package ua.com.foxminded.asharov.universityschedule.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
    private Long id;
    @NotEmpty(message = "Course's name cannot be empty.")
    @Size(min = 5, max = 30, message = "The length of Course's cannot be less than 5 and more than 30 symbols.")
    private String name;
    @NotEmpty(message = "Course's description cannot be empty.")
    private String description;

    public CourseDto(String name, String description) {
        this.id = null;
        this.name = name;
        this.description = description;
    }

}
