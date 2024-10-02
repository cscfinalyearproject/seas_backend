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


    @Query(value = "SELECT s.student_id AS studentId, " +
            "ats.attendance_type AS attendanceType, " +
            "c.course_code AS courseCode, " +
            "ats.time_stamp AS sessionTime, " +
            "IF(ar.time_stamp IS NULL, 'Absent', 'Present') AS status " +
            "FROM attendance_sessions ats " +
            "JOIN courses c ON ats.course_code = c.course_code " +
            "JOIN course_student cs ON cs.course_code = c.course_code " +
            "JOIN students s ON cs.student_id = s.student_id " +
            "LEFT JOIN attendance_record ar ON ar.student_id = s.student_id " +
            "AND ar.course_code = ats.course_code " +
            "AND ar.time_stamp = ats.time_stamp " +
            "WHERE s.student_id = :studentId",
            nativeQuery = true)
    List<Object[]> findAttendanceByStudentId(String studentId);


        @Query(value = "SELECT s.student_id, s.full_name, c.course_code, c.course_name, " +
                "ROUND(COUNT(CASE WHEN ar.time_stamp IS NOT NULL THEN 1 END) * 100.0 / COUNT(ats.time_stamp), 2) AS attendance_percentage " +
                "FROM students s " +
                "JOIN course_student cs ON cs.student_id = s.student_id " +
                "JOIN courses c ON cs.course_code = c.course_code " +
                "JOIN attendance_sessions ats ON ats.course_code = c.course_code " +
                "LEFT JOIN attendance_record ar ON ar.course_code = ats.course_code AND ar.student_id = s.student_id " +
                "WHERE c.department_id = :department " +
                "GROUP BY s.student_id, c.course_code, c.course_name " +
                "HAVING attendance_percentage < 80 " +
                "AND COUNT(ats.time_stamp) >= 0.7 * (SELECT COUNT(*) FROM attendance_sessions ats2 WHERE ats2.course_code = c.course_code);", nativeQuery = true)
        List<Object[]> findLowAttendanceStudents(Long department);

    @Query(value = "SELECT c.course_code, c.course_name, COUNT(cs.student_id) AS total_enrolled_students, " +
            "AVG(IF(ar.time_stamp IS NOT NULL, 1, 0)) * 100 AS average_attendance_percentage, " +
            "COUNT(DISTINCT ats.time_stamp) AS total_classes_held " +
            "FROM courses c " +
            "JOIN course_student cs ON cs.course_code = c.course_code " +
            "LEFT JOIN attendance_sessions ats ON ats.course_code = c.course_code " +
            "LEFT JOIN attendance_record ar ON ar.course_code = ats.course_code AND ar.student_id = cs.student_id " +
            "WHERE c.department_id = :department " +
            "GROUP BY c.course_code, c.course_name", nativeQuery = true)
    List<Object[]> findCourseStatistics(Long department);

    @Query(value = "SELECT DISTINCT YEAR(ats.time_stamp) AS year FROM attendance_sessions ats ORDER BY year DESC", nativeQuery = true)
    List<Integer> findDistinctYears();

    @Query(value = "SELECT c.course_code, c.course_name, " +
            "COUNT(CASE WHEN ar.time_stamp IS NOT NULL THEN 1 END) AS present_students, " +
            "COUNT(CASE WHEN ar.time_stamp IS NULL THEN 1 END) AS absent_students " +
            "FROM courses c " +
            "JOIN course_student cs ON cs.course_code = c.course_code " +
            "LEFT JOIN attendance_record ar ON ar.course_code = c.course_code AND ar.student_id = cs.student_id " +
            "WHERE c.department_id = :department " +
            "GROUP BY c.course_code, c.course_name", nativeQuery = true)
    List<Object[]> findCourseAttendance(Long department);

    @Query(value = "SELECT ats.time_stamp AS session_date, c.course_code, c.course_name, " +
            "COUNT(CASE WHEN ar.time_stamp IS NOT NULL THEN 1 END) AS present_students, " +
            "COUNT(CASE WHEN ar.time_stamp IS NULL THEN 1 END) AS absent_students " +
            "FROM attendance_sessions ats " +
            "JOIN courses c ON c.course_code = ats.course_code " +
            "LEFT JOIN attendance_record ar ON ar.course_code = ats.course_code AND ar.time_stamp = ats.time_stamp " +
            "WHERE c.department_id = :department " +
            "GROUP BY ats.time_stamp, c.course_code, c.course_name " +
            "ORDER BY ats.time_stamp", nativeQuery = true)
    List<Object[]> findSessionAttendance(Long department);


}
