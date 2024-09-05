package com.tumbwe.examandclassattendanceapi.service;

import com.tumbwe.examandclassattendanceapi.dto.StudentDto;
import com.tumbwe.examandclassattendanceapi.dto.StudentResponseDto;
import com.tumbwe.examandclassattendanceapi.model.Student;

import java.util.List;

public interface StudentService   {
    StudentDto addStudent(StudentDto studentDto);

    List<Student> getAllStudents();

    List<StudentResponseDto> getAllStudentsByDepartment(Long id);
}
