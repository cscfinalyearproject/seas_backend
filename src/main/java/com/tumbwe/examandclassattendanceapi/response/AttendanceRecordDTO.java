package com.tumbwe.examandclassattendanceapi.response;


import com.tumbwe.examandclassattendanceapi.model.AttendanceType;
import lombok.Data;


import java.time.LocalDate;

@Data
public class AttendanceRecordDTO {
        private  String studentId;
        private String courseCode;
        private LocalDate timestamp;
        private String attendanceType;

        public AttendanceRecordDTO(String studentId, String courseCode, String attendanceType) {
            this.studentId = studentId;
            this.courseCode = courseCode;
            this.timestamp = LocalDate.now();
            this.attendanceType = attendanceType;
        }

        // Getters and setters
    }


