package com.tumbwe.examandclassattendanceapi.service.Impl;

import com.tumbwe.examandclassattendanceapi.dto.CourseDto;
import com.tumbwe.examandclassattendanceapi.dto.EnrollmentDto;
import com.tumbwe.examandclassattendanceapi.dto.EnrollmentResponse;
import com.tumbwe.examandclassattendanceapi.exception.InternalServerException;
import com.tumbwe.examandclassattendanceapi.exception.ResourceNotFoundException;
import com.tumbwe.examandclassattendanceapi.model.Course;
import com.tumbwe.examandclassattendanceapi.model.Department;
import com.tumbwe.examandclassattendanceapi.model.Student;
import com.tumbwe.examandclassattendanceapi.repository.CourseRepository;
import com.tumbwe.examandclassattendanceapi.repository.DepartmentRepository;
import com.tumbwe.examandclassattendanceapi.repository.StudentRepository;
import com.tumbwe.examandclassattendanceapi.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public CourseDto addCourse(CourseDto courseDto) {
        if(!courseRepository.existsById(courseDto.getCourseCode())) {
            Course course = new Course();

            Department department = departmentRepository.findById(courseDto.getDepartmentId()).orElse(null);
            course.setCourseCode(courseDto.getCourseCode());
            course.setCourseName(courseDto.getCourseName());
            course.setSemester(course.getSemester());
            if(department == null) {
                throw new ResourceNotFoundException("Department not found");
            }
            course.setDepartment(department);
            try {
                Course savedCourse = courseRepository.save(course);
                return new CourseDto(savedCourse.getCourseName(), courseDto.getCourseCode(),courseDto.getDepartmentId(),courseDto.getSemester());
            }
            catch (Exception e){
                throw new InternalServerException(e.getMessage());
            }
        }
        else {
            throw new DataIntegrityViolationException("Course with course code: " + courseDto.getCourseCode()
                    +" already exists");
        }
         }



    @Override
    public List<CourseDto> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        if (!courses.isEmpty())
        return courses.stream().map(course ->
                new CourseDto(course.getCourseName(), course.getCourseCode(),course.getDepartment().getId(),course.getSemester())).collect(Collectors.toList());

        else
            throw new ResourceNotFoundException("Courses not found");
    }

    @Override
    public boolean existsByCourseCode(String courseCode) {
        return courseRepository.existsById(courseCode);
    }


}
