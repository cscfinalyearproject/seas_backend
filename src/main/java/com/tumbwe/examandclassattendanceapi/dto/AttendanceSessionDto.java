package com.tumbwe.examandclassattendanceapi.dto;

import com.tumbwe.examandclassattendanceapi.model.AttendanceType;
import com.tumbwe.examandclassattendanceapi.model.Course;
import com.tumbwe.examandclassattendanceapi.model.SessionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AttendanceSessionDto {
    private Long attendanceSessionId;
    private String courseCode;
    private String attendanceType;
    private String sessionStatus;
    private String timeStamp;
}
