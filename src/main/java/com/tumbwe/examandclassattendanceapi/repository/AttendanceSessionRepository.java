package com.tumbwe.examandclassattendanceapi.repository;

import com.tumbwe.examandclassattendanceapi.model.AttendanceSession;
import com.tumbwe.examandclassattendanceapi.model.AttendanceType;
import com.tumbwe.examandclassattendanceapi.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendanceSessionRepository extends JpaRepository<AttendanceSession, Long> {
    @Query("select s from AttendanceSession s where s.course.courseCode =:courseCode and s.attendanceType =:attendanceType and s.timeStamp =:date")
    Optional<AttendanceSession> findByCourseCode(String courseCode, AttendanceType attendanceType, LocalDate date);

    // Check for an open session by course, attendance type, and date
    @Query("SELECT a FROM AttendanceSession a WHERE a.course.courseCode = :courseCode AND a.attendanceType = :attendanceType AND a.sessionStatus = 'open' AND a.timeStamp = :date")
    AttendanceSession findOpenSession(String courseCode, String attendanceType, LocalDate date);

    @Query("SELECT a FROM AttendanceSession a WHERE a.sessionStatus = :status")
    Optional<AttendanceSession> findOpenSession(@Param("status") String status);


    List<AttendanceSession> findAllByCourse(Course course);



}
