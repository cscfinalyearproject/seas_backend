package com.tumbwe.examandclassattendanceapi.dto;

import lombok.Data;

@Data
public class AttendanceSummary {
    private String courseCode;
    private String avgAttendance;
}
