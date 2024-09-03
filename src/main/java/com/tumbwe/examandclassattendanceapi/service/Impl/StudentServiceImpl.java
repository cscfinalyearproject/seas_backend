package com.tumbwe.examandclassattendanceapi.service.Impl;

import com.tumbwe.examandclassattendanceapi.dto.StudentDto;
import com.tumbwe.examandclassattendanceapi.exception.InternalServerException;
import com.tumbwe.examandclassattendanceapi.exception.ResourceNotFoundException;
import com.tumbwe.examandclassattendanceapi.model.Student;
import com.tumbwe.examandclassattendanceapi.repository.StudentRepository;
import com.tumbwe.examandclassattendanceapi.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public StudentDto addStudent(StudentDto studentDto) {

        Optional<Student> student = studentRepository.findById(studentDto.getStudentId());

        if (student.isEmpty())
        {
            Student newStudent = new Student();
            newStudent.setStudentId(studentDto.getStudentId());
            if (!studentDto.getFullname().isBlank())
                newStudent.setFullName(studentDto.getFullname());
            if (!studentDto.getFingerprintTemplate().isBlank())
                newStudent.setFingerprintTemplate(studentDto.getFingerprintTemplate());
            try {
                Student savedStudent = studentRepository.save(newStudent);
                String message = "Student saved successfully";
                return new StudentDto(savedStudent.getStudentId(), savedStudent.getFingerprintTemplate(),savedStudent.getFullName(), message);
            }
            catch (Exception e){
                throw new InternalServerException("Error saving student: " + e.getMessage());
            }
        }

        else {
            Student updateStudent = student.get();
            if (!studentDto.getFingerprintTemplate().isBlank())
                updateStudent.setFingerprintTemplate(studentDto.getFingerprintTemplate());

            Student savedStudent = studentRepository.save(updateStudent);
            String message = "Student Fingerprint Template added successfully";
            return new StudentDto(savedStudent.getStudentId(), savedStudent.getFingerprintTemplate(),savedStudent.getFullName(), message);
        }
    }

    @Override
    public List<Student> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        if (students.isEmpty())
            throw new ResourceNotFoundException("No Students exist in the database");
        return students;
    }
}
