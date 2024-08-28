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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID attendanceId;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_code")
    private Course course;
    private LocalDate timeStamp;
    @Enumerated(value = EnumType.STRING)
    private AttendanceType attendanceType;

    public AttendanceRecord(Student student, Course course, AttendanceType attendanceType) {
        this.student = student;
        this.course = course;
        this.attendanceType = attendanceType;
        this.timeStamp = LocalDate.now();
    }
}
