package com.tumbwe.examandclassattendanceapi.service.Impl;

import com.tumbwe.examandclassattendanceapi.model.AttendanceRecord;
import com.tumbwe.examandclassattendanceapi.model.Student;
import com.tumbwe.examandclassattendanceapi.repository.AttendanceRecordRepository;
import com.tumbwe.examandclassattendanceapi.repository.CourseRepository;
import com.tumbwe.examandclassattendanceapi.repository.DepartmentRepository;
import com.tumbwe.examandclassattendanceapi.repository.StudentRepository;
import com.tumbwe.examandclassattendanceapi.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;

    @Override
    public List<Student> getStudentsByDepartment(Long id) {
        return studentRepository.findAllByDepartment(departmentRepository.findById(id).orElse(null));
    }

    @Override
    public Set<Student> getStudentsByCourse(String course) {
        return courseRepository.findStudentsByCourseCode(course);
    }

    @Override
    public List<AttendanceRecord> getAttendanceRecordByCourse(String course) {
        var courseRecord = courseRepository.findByCourseCode(course).orElse(null);

        if(courseRecord == null) {
            return new ArrayList<>();
        }
        return attendanceRecordRepository.findAllByCourseOrderByStudent(courseRecord);
    }


}
