package com.tumbwe.examandclassattendanceapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EnrollmentDto {

    @NotBlank(message = "Student Id is required")
    private String studentId;

    @NotBlank(message = "Course code is required")
    private String courseCode;
}
