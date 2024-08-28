package com.tumbwe.examandclassattendanceapi.repository;

import com.tumbwe.examandclassattendanceapi.model.AttendanceSession;
import com.tumbwe.examandclassattendanceapi.model.AttendanceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendanceSessionRepository extends JpaRepository<AttendanceSession, UUID> {
    @Query("select s from AttendanceSession s where s.course.courseCode =:courseCode and s.attendanceType =:attendanceType and s.timeStamp =:date")
    Optional<AttendanceSession> findByCourseCode(String courseCode, AttendanceType attendanceType, LocalDate date);
}
