package com.tumbwe.examandclassattendanceapi.repository;

import com.tumbwe.examandclassattendanceapi.dto.CourseStudentProjection;
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
    List<Object[]> getAttendanceCount(@Param("courseCode") String courseCode,
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

    @Query(value = "SELECT s.student_id, s.full_name, c.course_code, c.course_name, " +
            "ROUND(COUNT(CASE WHEN ar.time_stamp IS NOT NULL THEN 1 END) * 100.0 / COUNT(ats.time_stamp), 2) AS attendance_percentage " +
            "FROM students s " +
            "JOIN course_student cs ON cs.student_id = s.student_id " +
            "JOIN courses c ON cs.course_code = c.course_code " +
            "JOIN attendance_sessions ats ON ats.course_code = c.course_code " +
            "LEFT JOIN attendance_record ar ON ar.course_code = ats.course_code AND ar.student_id = s.student_id " +
            "WHERE c.department_id = :department " +
            "AND c.course_code IN :courseCodes " +
            "GROUP BY s.student_id, c.course_code, c.course_name " +
            "HAVING attendance_percentage < 80 " +
            "AND COUNT(ats.time_stamp) >= 0.7 * (SELECT COUNT(*) FROM attendance_sessions ats2 WHERE ats2.course_code = c.course_code);",
            nativeQuery = true)
    List<Object[]> findLowAttendanceStudents(@Param("department") Long department,
                                             @Param("courseCodes") List<String> courseCodes);


    @Query(value = "SELECT c.course_code AS courseCode, " +
            "c.course_name AS courseName, " +
            "COUNT(DISTINCT cs.student_id) AS totalEnrolledStudents, " +
            "IFNULL(AVG(( " +
            "    SELECT COUNT(ar.time_stamp) " +
            "    FROM attendance_record ar " +
            "    WHERE ar.course_code = c.course_code " +
            "    AND ar.student_id = cs.student_id " +
            "    AND ar.time_stamp BETWEEN :from AND :to " +
            ") / ( " +
            "    SELECT COUNT(DISTINCT ats.time_stamp) " +
            "    FROM attendance_sessions ats " +
            "    WHERE ats.course_code = c.course_code " +
            "    AND ats.time_stamp BETWEEN :from AND :to " +
            ") * 100), 0) AS averageAttendancePercentage, " +
            "COUNT(DISTINCT ats.time_stamp) AS totalClassesHeld " +
            "FROM courses c " +
            "JOIN course_student cs ON cs.course_code = c.course_code " +
            "LEFT JOIN attendance_sessions ats ON ats.course_code = c.course_code " +
            "WHERE c.department_id = :department " +
            "AND c.course_code IN (:courseCodes) " +
            "GROUP BY c.course_code, c.course_name",
            nativeQuery = true)
    List<Object[]> findCourseStatistics(@Param("department") Long department,
                                        @Param("from") String from,
                                        @Param("to") String to,
                                        @Param("courseCodes") List<String> courseCodes);

    @Query(value = "SELECT c.course_code AS courseCode, " +
            "c.course_name AS courseName, " +
            "COUNT(DISTINCT cs.student_id) AS totalEnrolledStudents, " +
            "IFNULL(AVG(( " +
            "    SELECT COUNT(ar.time_stamp) " +
            "    FROM attendance_record ar " +
            "    WHERE ar.course_code = c.course_code " +
            "    AND ar.student_id = cs.student_id " +
            "    AND ar.time_stamp BETWEEN :from AND :to " +
            ") / ( " +
            "    SELECT COUNT(DISTINCT ats.time_stamp) " +
            "    FROM attendance_sessions ats " +
            "    WHERE ats.course_code = c.course_code " +
            "    AND ats.time_stamp BETWEEN :from AND :to " +
            ") * 100), 0) AS averageAttendancePercentage, " +
            "COUNT(DISTINCT ats.time_stamp) AS totalClassesHeld " +
            "FROM courses c " +
            "JOIN course_student cs ON cs.course_code = c.course_code " +
            "LEFT JOIN attendance_sessions ats ON ats.course_code = c.course_code " +
            "WHERE c.department_id = :department " +
            "GROUP BY c.course_code, c.course_name",
            nativeQuery = true)
    List<Object[]> findCourseStatistics(@Param("department") Long department,
                                                   @Param("from") String from,
                                                   @Param("to") String to);

    @Query(value = "SELECT c.course_code AS courseCode, " +
            "c.course_name AS courseName, " +
            "COUNT(DISTINCT cs.student_id) AS totalEnrolledStudents, " +
            "IFNULL(AVG(( " +
            "    SELECT COUNT(ar.time_stamp) " +
            "    FROM attendance_record ar " +
            "    WHERE ar.course_code = c.course_code " +
            "    AND ar.student_id = cs.student_id " +
            "    AND ar.time_stamp IS NOT NULL " +
            ") / ( " +
            "    SELECT COUNT(DISTINCT ats.time_stamp) " +
            "    FROM attendance_sessions ats " +
            "    WHERE ats.course_code = c.course_code " +
            "    AND ats.time_stamp IS NOT NULL " +
            ") * 100), 0) AS averageAttendancePercentage, " +
            "COUNT(DISTINCT ats.time_stamp) AS totalClassesHeld " +
            "FROM courses c " +
            "JOIN course_student cs ON cs.course_code = c.course_code " +
            "LEFT JOIN attendance_sessions ats ON ats.course_code = c.course_code " +
            "WHERE c.department_id = :department " +
            "AND c.course_code IN (:courseCodes) " +
            "GROUP BY c.course_code, c.course_name",
            nativeQuery = true)
    List<Object[]> findCourseStatistics(@Param("department") Long department,
                                                     @Param("courseCodes") List<String> courseCodes);

    @Query(value = "SELECT c.course_code AS courseCode, " +
            "c.course_name AS courseName, " +
            "COUNT(DISTINCT cs.student_id) AS totalEnrolledStudents, " +
            "IFNULL(AVG(( " +
            "    SELECT COUNT(ar.time_stamp) " +
            "    FROM attendance_record ar " +
            "    WHERE ar.course_code = c.course_code " +
            "    AND ar.student_id = cs.student_id " +
            ") / ( " +
            "    SELECT COUNT(DISTINCT ats.time_stamp) " +
            "    FROM attendance_sessions ats " +
            "    WHERE ats.course_code = c.course_code " +
            ") * 100), 0) AS averageAttendancePercentage, " +
            "COUNT(DISTINCT ats.time_stamp) AS totalClassesHeld " +
            "FROM courses c " +
            "JOIN course_student cs ON cs.course_code = c.course_code " +
            "LEFT JOIN attendance_sessions ats ON ats.course_code = c.course_code " +
            "WHERE c.department_id = :department " +
            "GROUP BY c.course_code, c.course_name",
            nativeQuery = true)
    List<Object[]> findCourseStatistics(@Param("department") Long department);



    @Query(value = "SELECT DISTINCT YEAR(ats.time_stamp) AS year FROM attendance_sessions ats ORDER BY year DESC", nativeQuery = true)
    List<Integer> findDistinctYears();

    @Query(value = "SELECT c.course_code AS courseCode, " +
            "c.course_name AS courseName, " +
            "ats.time_stamp AS sessionDate, " +
            "ROUND(COUNT(CASE WHEN ar.time_stamp IS NOT NULL THEN 1 END) * 100.0 / COUNT(s.student_id), 2) AS attendancePercentage " +
            "FROM courses c " +
            "JOIN course_student cs ON cs.course_code = c.course_code " +
            "JOIN students s ON s.student_id = cs.student_id " +
            "JOIN attendance_sessions ats ON ats.course_code = c.course_code " +
            "LEFT JOIN attendance_record ar ON ar.course_code = ats.course_code " +
            "AND ar.time_stamp = ats.time_stamp " +
            "AND ar.student_id = s.student_id " +
            "WHERE c.department_id = :departmentId " +
            "AND ats.time_stamp BETWEEN :from AND :to " +
            "AND c.course_code IN :courseCodes " +
            "GROUP BY c.course_code, c.course_name, ats.time_stamp " +
            "ORDER BY c.course_code, ats.time_stamp",
            nativeQuery = true)
    List<Object[]> findCourseAttendance(@Param("departmentId") Long departmentId,
                                        @Param("from") String from,
                                        @Param("to") String to,
                                        @Param("courseCodes") List<String> courseCodes);

    @Query(value = "SELECT c.course_code AS courseCode, " +
            "c.course_name AS courseName, " +
            "ats.time_stamp AS sessionDate, " +
            "ROUND(COUNT(CASE WHEN ar.time_stamp IS NOT NULL THEN 1 END) * 100.0 / COUNT(s.student_id), 2) AS attendancePercentage " +
            "FROM courses c " +
            "JOIN course_student cs ON cs.course_code = c.course_code " +
            "JOIN students s ON s.student_id = cs.student_id " +
            "JOIN attendance_sessions ats ON ats.course_code = c.course_code " +
            "LEFT JOIN attendance_record ar ON ar.course_code = ats.course_code " +
            "AND ar.time_stamp = ats.time_stamp " +
            "AND ar.student_id = s.student_id " +
            "WHERE c.department_id = :departmentId " +
            "AND c.course_code IN :courseCodes " +
            "GROUP BY c.course_code, c.course_name, ats.time_stamp " +
            "ORDER BY c.course_code, ats.time_stamp",
            nativeQuery = true)
    List<Object[]> findCourseAttendance(@Param("departmentId") Long departmentId,
                                                     @Param("courseCodes") List<String> courseCodes);

    @Query(value = "SELECT c.course_code AS courseCode, " +
            "c.course_name AS courseName, " +
            "ats.time_stamp AS sessionDate, " +
            "ROUND(COUNT(CASE WHEN ar.time_stamp IS NOT NULL THEN 1 END) * 100.0 / COUNT(s.student_id), 2) AS attendancePercentage " +
            "FROM courses c " +
            "JOIN course_student cs ON cs.course_code = c.course_code " +
            "JOIN students s ON s.student_id = cs.student_id " +
            "JOIN attendance_sessions ats ON ats.course_code = c.course_code " +
            "LEFT JOIN attendance_record ar ON ar.course_code = ats.course_code " +
            "AND ar.time_stamp = ats.time_stamp " +
            "AND ar.student_id = s.student_id " +
            "WHERE c.department_id = :departmentId " +
            "AND ats.time_stamp BETWEEN :from AND :to " +
            "GROUP BY c.course_code, c.course_name, ats.time_stamp " +
            "ORDER BY c.course_code, ats.time_stamp",
            nativeQuery = true)
    List<Object[]> findCourseAttendance(@Param("departmentId") Long departmentId,
                                                   @Param("from") String from,
                                                   @Param("to") String to);


    @Query(value = "SELECT c.course_code AS courseCode, " +
            "c.course_name AS courseName, " +
            "ats.time_stamp AS sessionDate, " +
            "ROUND(COUNT(CASE WHEN ar.time_stamp IS NOT NULL THEN 1 END) * 100.0 / COUNT(s.student_id), 2) AS attendancePercentage " +
            "FROM courses c " +
            "JOIN course_student cs ON cs.course_code = c.course_code " +
            "JOIN students s ON s.student_id = cs.student_id " +
            "JOIN attendance_sessions ats ON ats.course_code = c.course_code " +
            "LEFT JOIN attendance_record ar ON ar.course_code = ats.course_code " +
            "AND ar.time_stamp = ats.time_stamp " +
            "AND ar.student_id = s.student_id " +
            "WHERE c.department_id = :departmentId " +
            "GROUP BY c.course_code, c.course_name, ats.time_stamp " +
            "ORDER BY c.course_code, ats.time_stamp",
            nativeQuery = true)
    List<Object[]> findCourseAttendance(@Param("departmentId") Long departmentId);


    @Query(value = "SELECT ats.time_stamp AS session_date, " +
            "c.course_code, " +
            "c.course_name, " +
            "COUNT(DISTINCT CASE WHEN ar.time_stamp IS NOT NULL THEN ar.student_id END) AS present_students, " +
            "COUNT(DISTINCT cs.student_id) - COUNT(DISTINCT CASE WHEN ar.time_stamp IS NOT NULL THEN ar.student_id END) AS absent_students " +
            "FROM attendance_sessions ats " +
            "JOIN courses c ON c.course_code = ats.course_code " +
            "JOIN course_student cs ON cs.course_code = c.course_code " +
            "LEFT JOIN attendance_record ar ON ar.course_code = ats.course_code AND ar.time_stamp = ats.time_stamp AND ar.student_id = cs.student_id " +
            "WHERE c.department_id = :department " +
            "AND ats.time_stamp BETWEEN :from AND :to " +
            "AND c.course_code IN :courseCodes " +
            "GROUP BY ats.time_stamp, c.course_code, c.course_name " +
            "ORDER BY ats.time_stamp", nativeQuery = true)
    List<Object[]> findSessionAttendance(@Param("department") Long department,
                                         @Param("from") String from,
                                         @Param("to") String to,
                                         @Param("courseCodes") List<String> courseCodes);

    @Query(value = "SELECT ats.time_stamp AS session_date, " +
            "c.course_code, " +
            "c.course_name, " +
            "COUNT(DISTINCT CASE WHEN ar.time_stamp IS NOT NULL THEN ar.student_id END) AS present_students, " +
            "COUNT(DISTINCT cs.student_id) - COUNT(DISTINCT CASE WHEN ar.time_stamp IS NOT NULL THEN ar.student_id END) AS absent_students " +
            "FROM attendance_sessions ats " +
            "JOIN courses c ON c.course_code = ats.course_code " +
            "JOIN course_student cs ON cs.course_code = c.course_code " +
            "LEFT JOIN attendance_record ar ON ar.course_code = ats.course_code AND ar.time_stamp = ats.time_stamp AND ar.student_id = cs.student_id " +
            "WHERE c.department_id = :department " +
            "AND c.course_code IN :courseCodes " +
            "GROUP BY ats.time_stamp, c.course_code, c.course_name " +
            "ORDER BY ats.time_stamp", nativeQuery = true)
    List<Object[]> findSessionAttendance(@Param("department") Long department,
                                                      @Param("courseCodes") List<String> courseCodes);

    @Query(value = "SELECT ats.time_stamp AS session_date, " +
            "c.course_code, " +
            "c.course_name, " +
            "COUNT(DISTINCT CASE WHEN ar.time_stamp IS NOT NULL THEN ar.student_id END) AS present_students, " +
            "COUNT(DISTINCT cs.student_id) - COUNT(DISTINCT CASE WHEN ar.time_stamp IS NOT NULL THEN ar.student_id END) AS absent_students " +
            "FROM attendance_sessions ats " +
            "JOIN courses c ON c.course_code = ats.course_code " +
            "JOIN course_student cs ON cs.course_code = c.course_code " +
            "LEFT JOIN attendance_record ar ON ar.course_code = ats.course_code AND ar.time_stamp = ats.time_stamp AND ar.student_id = cs.student_id " +
            "WHERE c.department_id = :department " +
            "AND ats.time_stamp BETWEEN :from AND :to " +
            "GROUP BY ats.time_stamp, c.course_code, c.course_name " +
            "ORDER BY ats.time_stamp", nativeQuery = true)
    List<Object[]> findSessionAttendance(@Param("department") Long department,
                                                    @Param("from") String from,
                                                    @Param("to") String to);


    @Query(value = "SELECT ats.time_stamp AS session_date, " +
            "c.course_code, " +
            "c.course_name, " +
            "COUNT(DISTINCT CASE WHEN ar.time_stamp IS NOT NULL THEN ar.student_id END) AS present_students, " +
            "COUNT(DISTINCT cs.student_id) - COUNT(DISTINCT CASE WHEN ar.time_stamp IS NOT NULL THEN ar.student_id END) AS absent_students " +
            "FROM attendance_sessions ats " +
            "JOIN courses c ON c.course_code = ats.course_code " +
            "JOIN course_student cs ON cs.course_code = c.course_code " +
            "LEFT JOIN attendance_record ar ON ar.course_code = ats.course_code AND ar.time_stamp = ats.time_stamp AND ar.student_id = cs.student_id " +
            "WHERE c.department_id = :department " +
            "GROUP BY ats.time_stamp, c.course_code, c.course_name " +
            "ORDER BY ats.time_stamp", nativeQuery = true)
    List<Object[]> findSessionAttendance(@Param("department") Long department);


    @Query(value = "SELECT s.full_name AS name, " +
            "CAST(ROUND(AVG(IF(ar.time_stamp IS NOT NULL, 100.0, 0)), 2) AS CHAR) AS attendance " +
            "FROM students s " +
            "JOIN course_student cs ON cs.student_id = s.student_id " +
            "JOIN courses c ON cs.course_code = c.course_code " +
            "LEFT JOIN attendance_record ar ON ar.course_code = c.course_code AND ar.student_id = s.student_id " +
            "WHERE c.department_id = :departmentId " +
            "AND ar.time_stamp BETWEEN :from AND :to " +
            "AND c.course_code IN :courseCodes " +
            "GROUP BY s.student_id " +
            "ORDER BY attendance DESC " +
            "LIMIT :limit",
            nativeQuery = true)
    List<Object[]> findTopThreeOverallAttendance(@Param("departmentId") Long departmentId,
                                                      @Param("limit") int limit,
                                                      @Param("from") String from,
                                                      @Param("to") String to,
                                                      @Param("courseCodes") List<String> courseCodes);

    @Query(value = "SELECT s.full_name AS name, " +
            "CAST(ROUND(AVG(IF(ar.time_stamp IS NOT NULL, 100.0, 0)), 2) AS CHAR) AS attendance " +
            "FROM students s " +
            "JOIN course_student cs ON cs.student_id = s.student_id " +
            "JOIN courses c ON cs.course_code = c.course_code " +
            "LEFT JOIN attendance_record ar ON ar.course_code = c.course_code AND ar.student_id = s.student_id " +
            "WHERE c.department_id = :departmentId " +
            "AND c.course_code IN :courseCodes " +
            "GROUP BY s.student_id " +
            "ORDER BY attendance DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<Object[]> findTopThreeOverallAttendance(@Param("departmentId") Long departmentId,
                                                  @Param("limit") int limit,
                                                  @Param("courseCodes") List<String> courseCodes);

    @Query(value = "SELECT s.full_name AS name, " +
            "CAST(ROUND(AVG(IF(ar.time_stamp IS NOT NULL, 100.0, 0)), 2) AS CHAR) AS attendance " +
            "FROM students s " +
            "JOIN course_student cs ON cs.student_id = s.student_id " +
            "JOIN courses c ON cs.course_code = c.course_code " +
            "LEFT JOIN attendance_record ar ON ar.course_code = c.course_code AND ar.student_id = s.student_id " +
            "WHERE c.department_id = :departmentId " +
            "AND ar.time_stamp BETWEEN :from AND :to " +
            "GROUP BY s.student_id " +
            "ORDER BY attendance DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<Object[]> findTopThreeOverallAttendance(@Param("limit") int limit,
                                                @Param("departmentId") Long departmentId,
                                                @Param("from") String from,
                                                @Param("to") String to);



    @Query(value = "SELECT s.full_name AS name, " +
            "CAST(ROUND(AVG(IF(ar.time_stamp IS NOT NULL, 100.0, 0)), 2) AS CHAR) AS attendance " +
            "FROM students s " +
            "JOIN course_student cs ON cs.student_id = s.student_id " +
            "JOIN courses c ON cs.course_code = c.course_code " +
            "LEFT JOIN attendance_record ar ON ar.course_code = c.course_code AND ar.student_id = s.student_id " +
            "WHERE c.department_id = :departmentId " +
            "GROUP BY s.student_id " +
            "ORDER BY attendance DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<Object[]> findTopThreeOverallAttendance(@Param("departmentId") Long departmentId,
                                            @Param("limit") int limit);



    @Query(value = "SELECT c.course_code AS courseCode, c.course_name AS courseName, " +
            "cs.student_id AS studentId, s.full_name AS fullName, s.intake AS intake, " +
            "YEAR(CURDATE()) - CAST(SUBSTRING(s.intake, 4, 4) AS UNSIGNED) AS yearOfStudy " +
            "FROM courses c " +
            "JOIN course_student cs ON cs.course_code = c.course_code " +
            "JOIN students s ON s.student_id = cs.student_id " +
            "WHERE c.department_id = :departmentId AND s.intake LIKE CONCAT('%', :intake, '%') " +
            "ORDER BY c.course_name, s.full_name",
            nativeQuery = true)
    List<CourseStudentProjection> findStudentsByDepartmentAndIntake(@Param("departmentId") Long departmentId,
                                                     @Param("intake") String intake);



}
