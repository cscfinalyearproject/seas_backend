package com.tumbwe.examandclassattendanceapi.service;

import com.tumbwe.examandclassattendanceapi.dto.*;
import com.tumbwe.examandclassattendanceapi.model.Student;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DeanDashBoard {
    Set<Student> getStudents(Long schoolId);
    List<CourseResponseDto> getCourseBySchool(Long id, Integer year);
    List<NotificationDto> getLowAttendanceNotifications(Long schoolId);
    List<CourseStatisticsDto> getCourseStatistics(Long schoolId, String from, String to, Integer year);
    List<Map<String, Object>> getCourseAttendanceTrends(Long schoolId, String from, String to, Integer year);
    List<SessionAttendanceDto> getSessionAttendance(Long schoolId, String from, String to, Integer year);
    List<OverallStudentDto> getOverallAttendance(Long schoolId, int limit, String from, String to, Integer year);
    List<CourseStudentProjection> getStudentsByDepartmentAndIntake(Long schoolId, String intake);
}
