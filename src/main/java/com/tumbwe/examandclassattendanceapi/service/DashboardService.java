package com.tumbwe.examandclassattendanceapi.service;

import com.tumbwe.examandclassattendanceapi.model.AttendanceRecord;
import com.tumbwe.examandclassattendanceapi.model.Student;

import java.util.List;
import java.util.Set;

public interface DashboardService {
    public Set<Student> getStudentsByDepartment(Long id);
    public Set<Student> getStudentsByCourse(String course);
    public List<AttendanceRecord> getAttendanceRecordByCourse(String course);
    public List<AttendanceRecord> getAttendanceRecordByStudent(String id);
}
