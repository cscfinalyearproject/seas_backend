package com.tumbwe.examandclassattendanceapi.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseStatisticsDto {
    private String courseCode;
    private String courseName;
    private int totalEnrolledStudents;
    private double averageAttendancePercentage;
    private int totalClassesHeld;
}