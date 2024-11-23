package com.tumbwe.examandclassattendanceapi.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActiveSession {
        private boolean reply;
        private String sessionId;
        private String courseCode;
        private String attendanceType;
    }


