package com.tumbwe.examandclassattendanceapi.model;

import com.tumbwe.examandclassattendanceapi.dto.StartSession;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class AttendanceSessionOut {
    private String sessionId;
    private Set<Student> students;


}
