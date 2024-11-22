package com.tumbwe.examandclassattendanceapi.utils;

import com.tumbwe.examandclassattendanceapi.model.Course;
import com.tumbwe.examandclassattendanceapi.model.Department;
import com.tumbwe.examandclassattendanceapi.repository.CourseRepository;
import com.tumbwe.examandclassattendanceapi.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseUtils {
    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;
    public List<String> getCourseCodes(Long departmentId, int year){
        Optional<Department> department = departmentRepository.findById(departmentId);

        if(department.isEmpty()){
            return new ArrayList<>();
        }
        List<Course> courses = courseRepository.findAllByDepartment(department.get());
        return courses.stream()
                .filter(course -> {
                    String courseCode = course.getCourseCode();
                    for (char c : courseCode.toCharArray()) {
                        if (Character.isDigit(c)) { // Check if the character is a digit
                            int courseYear = Character.getNumericValue(c); // Convert to a number
                            return courseYear == year; // Match with the given year
                        }
                    }
                    return false;
                })
                .map(Course::getCourseCode) // Extract the course code
                .toList();
    }
}
