package com.tumbwe.examandclassattendanceapi.service;

import com.tumbwe.examandclassattendanceapi.dto.*;
import com.tumbwe.examandclassattendanceapi.model.AttendanceRecord;
import com.tumbwe.examandclassattendanceapi.model.AttendanceSession;
import com.tumbwe.examandclassattendanceapi.model.Course;
import com.tumbwe.examandclassattendanceapi.model.Student;
import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DashboardService {
    Set<Student> getStudentsByDepartment(Long id, Integer year);
    Set<Student> getStudentsByCourse(String course);
    List<AttendanceRecord> getAttendanceRecordByCourse(String course);
    List<AttendanceRecord> getAttendanceRecordByStudent(String id);
    List<AttendanceRecordDto> getAttendanceCount(String courseCode, String attendanceType, String year);
    List<AttendanceRecordDto> getPresent(String courseCode, String attendanceType, String year);
    List<AttendanceRecordDto> getAbsentStudents(String courseCode);
    List<AttendanceSessionDto> getSessions(String courseCode);
    List<AttendanceRecordDto> getRecords(Long id);
    List<CourseResponseDto> getCourseByDepartment(Long id, Integer year);
    StudentAttendanceDto getStudentAttendanceById(String id);
    List<NotificationDto> getLowAttendanceNotifications(Long department);
    List<CourseStatisticsDto> getCourseStatistics(Long department, String from, String to, Integer year);
    List<YearDto> getDistinctYears();
    List<Map<String, Object>> getCourseAttendanceTrends(Long departmentId, String from, String to, Integer year);
    List<SessionAttendanceDto> getSessionAttendance(Long department, String from, String to, Integer year);
    List<DashboardCourseDto> getDistinctCourses();
    List<String> saveStudent(MultipartFile file) throws Exception;
    List<String> saveCourse(MultipartFile file) throws Exception;
    List<Long> saveAttendanceRecord(MultipartFile file, Long session_id) throws Exception;
    List<String> saveAttendanceSession(MultipartFile file) throws Exception;
    List<OverallStudentDto> getOverallAttendance(Long departmentId, int limit, String from, String to, Integer year);
    List<CourseStudentProjection> getStudentsByDepartmentAndIntake(Long departmentId, String intake);
}
