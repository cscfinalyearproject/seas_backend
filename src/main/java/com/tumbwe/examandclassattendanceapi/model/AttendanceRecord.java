package com.tumbwe.examandclassattendanceapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class AttendanceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_code")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "attendance_session_id")
    private AttendanceSession session;

    private LocalDate timeStamp;
    private String attendanceType;

    public AttendanceRecord(Student student, Course course, String attendanceType, AttendanceSession session ) {
        this.student = student;
        this.course = course;
        this.attendanceType = attendanceType;
        this.timeStamp = LocalDate.now();
        this.session = session;
    }
}
