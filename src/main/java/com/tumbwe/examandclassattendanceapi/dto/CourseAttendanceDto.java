package com.tumbwe.examandclassattendanceapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CourseAttendanceDto {
    private String courseCode;
    private String courseName;
    private long presentStudents;
    private long absentStudents;
}
