package com.tumbwe.examandclassattendanceapi.controller;

import com.tumbwe.examandclassattendanceapi.dto.CourseDto;
import com.tumbwe.examandclassattendanceapi.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    @PostMapping("/add-course")
    public ResponseEntity<?> addCourse(@RequestBody CourseDto courseDto){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(courseService.addCourse(courseDto));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @GetMapping(path = "/all-courses")
    public ResponseEntity<?> getAllCourses(){
       try {
            List<CourseDto> courseDtos = courseService.getAllCourses();
            return ResponseEntity.ok(courseDtos);
        }
    catch (Exception e){
           return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
    }

    @GetMapping(path = "/is-available-course")
    public ResponseEntity<?> isCourseAvailable(@RequestParam String courseCode){
        try { return  ResponseEntity.status(HttpStatus.FOUND).body(courseService.existsByCourseCode(courseCode));}
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }



}
