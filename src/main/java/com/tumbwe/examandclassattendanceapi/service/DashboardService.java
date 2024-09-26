package com.tumbwe.examandclassattendanceapi.service;

import com.tumbwe.examandclassattendanceapi.dto.AttendanceRecordDto;
import com.tumbwe.examandclassattendanceapi.dto.AttendanceSessionDto;
import com.tumbwe.examandclassattendanceapi.model.AttendanceRecord;
import com.tumbwe.examandclassattendanceapi.model.AttendanceSession;
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
}
