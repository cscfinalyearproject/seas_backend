package com.tumbwe.examandclassattendanceapi.service;

import com.tumbwe.examandclassattendanceapi.model.Student;

import java.util.List;
import java.util.Set;

public interface DashboardService {
    public List<Student> getStudentsByDepartment(String name);
    public Set<Student> getStudentsByCourse(String course);
}
