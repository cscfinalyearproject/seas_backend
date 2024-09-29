package com.tumbwe.examandclassattendanceapi.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SessionAttendanceDto {
    private String sessionDate;
    private String courseCode;
    private String courseName;
    private int presentStudents;
    private int absentStudents;
}
