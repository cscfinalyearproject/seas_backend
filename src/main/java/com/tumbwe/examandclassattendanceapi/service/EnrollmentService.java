package com.tumbwe.examandclassattendanceapi.service;

import com.tumbwe.examandclassattendanceapi.dto.EnrollmentDto;
import com.tumbwe.examandclassattendanceapi.dto.EnrollmentResponse;
import com.tumbwe.examandclassattendanceapi.model.Student;

import java.util.Set;

public interface EnrollmentService {
    Set<Student> getAllCourseStudents(String courseCode);
    EnrollmentResponse addStudentToCourse(EnrollmentDto enrollmentDto);
}
