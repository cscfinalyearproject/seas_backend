package com.tumbwe.examandclassattendanceapi.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentDto {
    @NotBlank(message = "Student ID is required")
    private String studentId;

    @Lob
    @Column(columnDefinition = "LONGTEXT")  // For MySQL, use LONGTEXT for large text
    private String fingerprintTemplate;
    private String fullname;
    private String message;
}
