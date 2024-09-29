package com.tumbwe.examandclassattendanceapi.service;

import com.tumbwe.examandclassattendanceapi.dto.*;
import com.tumbwe.examandclassattendanceapi.model.AttendanceRecord;
import com.tumbwe.examandclassattendanceapi.model.AttendanceSession;
import com.tumbwe.examandclassattendanceapi.model.Course;
import com.tumbwe.examandclassattendanceapi.model.Student;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface DashboardService {
    public Set<Student> getStudentsByDepartment(Long id);
    public Set<Student> getStudentsByCourse(String course);
    public List<AttendanceRecord> getAttendanceRecordByCourse(String course);
    public List<AttendanceRecord> getAttendanceRecordByStudent(String id);
    public List<AttendanceRecordDto> getAttendanceCount(String courseCode, String attendanceType, String year);
    public List<AttendanceRecordDto> getPresent(String courseCode, String attendanceType, String year);
    List<AttendanceRecordDto> getAbsentStudents(String courseCode);
    List<AttendanceSessionDto> getSessions(String courseCode);
    List<AttendanceRecordDto> getRecords(Long id);
    List<CourseResponseDto> getCourseByDepartment(Long id);
    StudentAttendanceDto getStudentAttendanceById(String id);
    public List<NotificationDto> getLowAttendanceNotifications();
    public List<CourseStatisticsDto> getCourseStatistics();
    public List<YearDto> getDistinctYears();
    public List<CourseAttendanceDto> getCourseAttendance();
    public List<SessionAttendanceDto> getSessionAttendance();
    public List<DashboardCourseDto> getDistinctCourses();
}
