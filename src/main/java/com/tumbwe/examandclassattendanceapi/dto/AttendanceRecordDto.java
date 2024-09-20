package com.tumbwe.examandclassattendanceapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceRecordDto {
    private long attendanceCount;
    private String studentId;
    private String courseCode;
    private String timeStamp;
    private String attendanceType;
    private String fullName;
}
