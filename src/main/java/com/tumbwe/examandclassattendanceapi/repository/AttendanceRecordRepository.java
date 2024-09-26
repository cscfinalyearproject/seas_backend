package com.tumbwe.examandclassattendanceapi.repository;

import com.tumbwe.examandclassattendanceapi.model.AttendanceRecord;
import com.tumbwe.examandclassattendanceapi.model.AttendanceSession;
import com.tumbwe.examandclassattendanceapi.model.Course;
import com.tumbwe.examandclassattendanceapi.model.Student;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
//    public AttendanceRecord findAllByStudentOrderByStudent(UUID studentGroup);
    public List<AttendanceRecord> findAllByCourseOrderByStudent(Course course);
    public List<AttendanceRecord> findAllByStudentOrderByStudent(Student student);
    public List<AttendanceRecord> findAllByStudentAndCourseOrderByStudent(Student student, Course course);

    @Query(value = "SELECT COUNT(ar.student_id) AS attendance_count, ar.student_id, ar.course_code, ar.time_stamp, ar.attendance_type, s.full_name " +
            "FROM attendance_record ar " +
            "JOIN students s ON ar.student_id = s.student_id " +
            "WHERE ar.course_code = :courseCode AND ar.attendance_type = :attendanceType AND ar.time_stamp LIKE CONCAT('%', :year, '%') " +
            "GROUP BY ar.student_id, ar.course_code, ar.attendance_type, s.full_name, ar.time_stamp",
            nativeQuery = true)
    public List<Object[]> getAttendanceCount(@Param("courseCode") String courseCode,
                                      @Param("attendanceType") String attendanceType,
                                      @Param("year") String year);

    @Query(value = "SELECT ar.*, s.full_name " +
            "FROM students s " +
            "JOIN course_student cs ON cs.student_id = s.student_id " +
            "LEFT JOIN attendance_record ar ON ar.student_id = s.student_id " +
            "WHERE ar.course_code = :courseCode " +
            "AND ar.attendance_type = :attendanceType " +
            "AND ar.time_stamp LIKE :date",
            nativeQuery = true)
    List<Object[]> getPresentStudents(@Param("courseCode") String courseCode,
                                      @Param("attendanceType") String attendanceType,
                                      @Param("date") String date);

    @Query(value = "SELECT s.full_name, s.student_id  " +
            "FROM students s " +
            "JOIN course_student cs ON cs.student_id = s.student_id " +
            "LEFT JOIN attendance_record ar ON ar.student_id = s.student_id " +
            "WHERE cs.course_code = :courseCode " +
            "AND ar.attendance_id IS NULL",
            nativeQuery = true)
    List<Object[]> getAbsentStudents(@Param("courseCode") String courseCode);

    List<AttendanceRecord> getAttendanceRecordBySession(AttendanceSession session);

}
