package com.tumbwe.examandclassattendanceapi.service;

import com.tumbwe.examandclassattendanceapi.dto.CourseDto;
import com.tumbwe.examandclassattendanceapi.dto.EnrollmentDto;
import com.tumbwe.examandclassattendanceapi.dto.EnrollmentResponse;
import com.tumbwe.examandclassattendanceapi.model.Student;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface CourseService {
    CourseDto addCourse(CourseDto courseDto);

    List<CourseDto> getAllCourses();

    boolean existsByCourseCode(String courseCode);
}
