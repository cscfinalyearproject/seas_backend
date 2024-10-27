package com.tumbwe.examandclassattendanceapi.service.Impl;

import com.tumbwe.examandclassattendanceapi.dto.AttendanceSummary;
import com.tumbwe.examandclassattendanceapi.dto.StudentDto;
import com.tumbwe.examandclassattendanceapi.dto.StudentInDto;
import com.tumbwe.examandclassattendanceapi.dto.StudentResponseDto;
import com.tumbwe.examandclassattendanceapi.exception.InternalServerException;
import com.tumbwe.examandclassattendanceapi.exception.ResourceNotFoundException;
import com.tumbwe.examandclassattendanceapi.model.Student;
import com.tumbwe.examandclassattendanceapi.repository.AttendanceRecordRepository;
import com.tumbwe.examandclassattendanceapi.repository.CourseRepository;
import com.tumbwe.examandclassattendanceapi.repository.DepartmentRepository;
import com.tumbwe.examandclassattendanceapi.repository.StudentRepository;
import com.tumbwe.examandclassattendanceapi.response.AttendanceRecordDTO;
import com.tumbwe.examandclassattendanceapi.service.StudentService;
import lombok.RequiredArgsConstructor;
<<<<<<< HEAD
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
=======
>>>>>>> origin/main
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;

    @Override
    public StudentDto addStudent(StudentDto studentDto) {

        Optional<Student> student = studentRepository.findById(studentDto.getStudentId());

        if (student.isEmpty())
        {
            System.out.println("in here");
            Student newStudent = new Student();
            newStudent.setStudentId(studentDto.getStudentId());
            if (!StringUtils.isBlank(studentDto.getFullname()))
                newStudent.setFullName(studentDto.getFullname());
            if (!StringUtils.isBlank(studentDto.getFingerprintTemplate()))
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

    @Override
    public List<StudentResponseDto> getAllStudentsByDepartment(Long id) {
        var courses = courseRepository.findAllByDepartment(departmentRepository.findById(id).orElse(null));
        Set<StudentInDto> students = new HashSet<>();
        List<StudentResponseDto> studentResponseDtos = new ArrayList<>();
        for (var course : courses) {
            var studentRecords = courseRepository.findStudentsByCourseCode(course.getCourseCode());
            for(var studentRec : studentRecords){
                StudentInDto s = new StudentInDto();
                s.setStudentId(studentRec.getStudentId());
                s.setStudentName(studentRec.getFullName());
                s.setCourseCode(course.getCourseCode());
                students.add(s);
            }
        }

        for (var student : students) {
            List<AttendanceSummary> attendanceSummaries = new ArrayList<>();
            StudentResponseDto studentResponseDto = new StudentResponseDto();
            studentResponseDto.setStudentId(student.getStudentId());
            studentResponseDto.setFullName(student.getStudentName());
            var course = courseRepository.findById(student.getCourseCode()).orElse(null);
            var st = studentRepository.findById(student.getStudentId()).orElse(null);
            var summary = attendanceRecordRepository.findAllByStudentAndCourseOrderByStudent(st,course);
            int d = 0;
            for (var record : summary) {
                AttendanceSummary attendanceSummary = new AttendanceSummary();
                attendanceSummary.setCourseCode(student.getCourseCode());
                attendanceSummary.setAvgAttendance(45+"");
                attendanceSummaries.add(attendanceSummary);
            }
            studentResponseDto.setAttendanceSummaryList(attendanceSummaries);
            studentResponseDtos.add(studentResponseDto);
        }

        return studentResponseDtos;
    }

}
