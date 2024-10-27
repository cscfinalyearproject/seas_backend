package com.tumbwe.examandclassattendanceapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceResponseDto {
    private String attendanceType;
    private String courseCode;
    private String sessionTime;
    private String status;
}
