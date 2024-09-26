package com.tumbwe.examandclassattendanceapi.dto;

import com.tumbwe.examandclassattendanceapi.model.AttendanceType;
import lombok.Data;

import java.util.UUID;

@Data
public class StartSession {

    private Long sessionId;
    private AttendanceType type;
    private String courseCode;

}
