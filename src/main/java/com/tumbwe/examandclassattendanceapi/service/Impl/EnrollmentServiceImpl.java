package com.tumbwe.examandclassattendanceapi.service.Impl;

import com.tumbwe.examandclassattendanceapi.dto.EnrollmentDto;
import com.tumbwe.examandclassattendanceapi.dto.EnrollmentResponse;
import com.tumbwe.examandclassattendanceapi.exception.InternalServerException;
import com.tumbwe.examandclassattendanceapi.exception.ResourceNotFoundException;
import com.tumbwe.examandclassattendanceapi.model.Course;
import com.tumbwe.examandclassattendanceapi.model.Student;
import com.tumbwe.examandclassattendanceapi.repository.CourseRepository;
import com.tumbwe.examandclassattendanceapi.repository.StudentRepository;
import com.tumbwe.examandclassattendanceapi.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    @Override
    public Set<Student> getAllCourseStudents(String courseCode) {
        Course course = courseRepository.findByCourseCode(courseCode)
                .orElseThrow(()->new ResourceNotFoundException("Course with course code: " + courseCode + " not found"));
        Set<Student> courseStudents = course.getEnrolledStudents();
        if (courseStudents.isEmpty())
            throw new ResourceNotFoundException("No students enrolled to Course with Course Code: "+ courseCode);
        return courseStudents;
    }

    @Override
    public EnrollmentResponse addStudentToCourse(EnrollmentDto enrollmentDto) {
        Course course = courseRepository.findByCourseCode(enrollmentDto.getCourseCode()).orElseThrow(()
                -> new ResourceNotFoundException("Course with course code: " + enrollmentDto.getCourseCode() + " not found"));
        Student student = studentRepository.findById(enrollmentDto.getStudentId()).orElseThrow(()->
                new ResourceNotFoundException("Student with id: "+ enrollmentDto.getStudentId() + " not found"));
        if (course.getEnrolledStudents().contains(student))
            throw new DataIntegrityViolationException("student with id :" + enrollmentDto.getStudentId() + " already enrolled in course");
        course.getEnrolledStudents().add(student);
        try {
            Course savedCourse = courseRepository.save(course);
            String message = "student added successfully to course";
            return new EnrollmentResponse(student.getStudentId(), savedCourse.getCourseCode(), message);
        }
        catch (Exception e){
            throw new InternalServerException(e.getMessage());
        }

    }
}
