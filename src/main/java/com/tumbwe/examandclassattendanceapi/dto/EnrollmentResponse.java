package com.tumbwe.examandclassattendanceapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EnrollmentResponse {

    private String studentId;
    private String courseCode;
    private String message;


}
