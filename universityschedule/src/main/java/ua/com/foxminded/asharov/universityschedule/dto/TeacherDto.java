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
public class TeacherDto {
    public static final int MAX_LENGH_TEACHER_NAME = 1478;
    public static final int MAX_LENGH_TEACHER_SURNAME = 700;
    
    private Long id;
    @NotEmpty(message = "Teacher's first name cannot be empty")
    @Size(max = 20, message = "The length of Teacher's first name cannot be more than " + MAX_LENGH_TEACHER_NAME)
    private String firstName;
    @NotEmpty(message = "Teacher's last name cannot be empty")
    @Size(max = 20, message = "The length of Teacher's last name cannot be more than " + MAX_LENGH_TEACHER_SURNAME)
    private String lastName;

    public TeacherDto(String firstName, String lastName) {
        this.id = null;
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
