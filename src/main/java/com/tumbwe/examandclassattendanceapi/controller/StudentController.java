package com.tumbwe.examandclassattendanceapi.controller;

import com.tumbwe.examandclassattendanceapi.dto.StudentDto;
import com.tumbwe.examandclassattendanceapi.model.Student;
import com.tumbwe.examandclassattendanceapi.service.StudentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/students")
public class StudentController {

    private final StudentService studentService;
    @PostMapping("/add-student")
    public ResponseEntity<?> addStudent(@Valid @RequestBody StudentDto studentDto){

        try {
            return ResponseEntity.ok(studentService.addStudent(studentDto));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }



    @GetMapping(path = "/all-students")
    public ResponseEntity<?> getAllStudents(){
        try {
            List<Student> students = studentService.getAllStudents();
            return ResponseEntity.ok(students);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

}
