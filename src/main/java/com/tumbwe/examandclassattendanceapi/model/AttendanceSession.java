package com.tumbwe.examandclassattendanceapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "attendance_sessions")
public class AttendanceSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceSessionId;

    @ManyToOne
    @JoinColumn(name = "course_code")
    private Course course;
    private String attendanceType;
    private String deviceId;

    private String sessionStatus = SessionStatus.open + "";
    private LocalDate timeStamp;

    public AttendanceSession(Course course, String attendanceType, String deviceId){
        this.course = course;
        this.attendanceType = attendanceType;
        this.deviceId = deviceId;
        this.timeStamp = LocalDate.now();
    }
}
