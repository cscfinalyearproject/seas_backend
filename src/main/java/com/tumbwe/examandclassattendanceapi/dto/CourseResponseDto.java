package com.tumbwe.examandclassattendanceapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseResponseDto {
    private String courseName;
    private String courseCode;
    private Long departmentId;
    private Long id;
}
