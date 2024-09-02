package com.tumbwe.examandclassattendanceapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentDto {
    @NotBlank(message = "Student ID is required")
    private String studentId;

    private String fingerprintTemplate;
    private String fullname;
    private String message;
    private String department;
}
