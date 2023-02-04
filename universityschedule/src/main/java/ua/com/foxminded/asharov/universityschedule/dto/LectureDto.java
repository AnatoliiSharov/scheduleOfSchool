package ua.com.foxminded.asharov.universityschedule.dto;

import java.time.LocalDate;
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
public class LectureDto {
    private Long id;
    private Integer serialNumberPerDay;
    private LocalDate date;
    private Long roomId;
    private Long teacherId;
    private Long courseId;
    private Long groupId;
    
    public LectureDto(Integer serialNumberPerDay, LocalDate date, Long roomId, Long teacherId, Long courseId,
            Long groupId) {
        super();
        this.serialNumberPerDay = serialNumberPerDay;
        this.date = date;
        this.roomId = roomId;
        this.teacherId = teacherId;
        this.courseId = courseId;
        this.groupId = groupId;
    }

    public LectureDto(Integer serialNumberPerDay, LocalDate date) {
        super();
        this.serialNumberPerDay = serialNumberPerDay;
        this.date = date;
    }

    
    
}
