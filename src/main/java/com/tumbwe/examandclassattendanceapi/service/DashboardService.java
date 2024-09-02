package com.tumbwe.examandclassattendanceapi.service;

import com.tumbwe.examandclassattendanceapi.model.Student;

import java.util.List;

public interface DashboardService {
    public List<Student> getStudentsByDepartment(String name);
}
