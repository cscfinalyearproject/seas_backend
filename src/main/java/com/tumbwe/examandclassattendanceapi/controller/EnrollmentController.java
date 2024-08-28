package com.tumbwe.examandclassattendanceapi.controller;

import com.tumbwe.examandclassattendanceapi.dto.CourseDto;
import com.tumbwe.examandclassattendanceapi.dto.EnrollmentDto;
import com.tumbwe.examandclassattendanceapi.dto.EnrollmentResponse;
import com.tumbwe.examandclassattendanceapi.repository.CourseRepository;
import com.tumbwe.examandclassattendanceapi.service.CourseService;
import com.tumbwe.examandclassattendanceapi.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/enroll")
@RequiredArgsConstructor
public class EnrollmentController {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    @PostMapping(path = "/add-student-to-course")
    public ResponseEntity<?> addStudentToCourse(@RequestBody EnrollmentDto enrollmentDto){
        try {
            EnrollmentResponse response = enrollmentService.addStudentToCourse(enrollmentDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping(path = "/all-course-students")
    public ResponseEntity<?> getAllCourseStudents(@RequestParam String courseCode){
        try{
            return  ResponseEntity.status(HttpStatus.FOUND).body(enrollmentService.getAllCourseStudents(courseCode));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


}
