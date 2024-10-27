package com.tumbwe.examandclassattendanceapi.dto;


import lombok.Data;

import java.util.List;

@Data
public class StudentResponseDto {
    private String fullName;
    private String studentId;
    private List<AttendanceSummary> attendanceSummaryList;
}
