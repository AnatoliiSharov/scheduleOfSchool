package ua.com.foxminded.asharov.universityschedule.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDto {
    public static final int MAX_LENGH_STUDENTS_NAME = 1478;
    public static final int MAX_LENGH_STUDENTS_SURNAME = 700;
    
    private Long id;
    @NotNull(message = "Room's capacity cannot be null")
    private Long groupId;
    @NotEmpty(message = "Student's first name cannot be empty")
    @Size(max = 1478, message = "The length of Student's first name cannot be more than 1478")
    private String firstName;
    @NotEmpty(message = "Student's last name cannot be empty")
    @Size(max = 700, message = "The length of Student's last name cannot be more than 700")
    private String lastName;

}
