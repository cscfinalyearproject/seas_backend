package com.tumbwe.examandclassattendanceapi.service.Impl;

import com.tumbwe.examandclassattendanceapi.dto.AttendanceRecordDto;
import com.tumbwe.examandclassattendanceapi.model.AttendanceRecord;
import com.tumbwe.examandclassattendanceapi.model.Student;
import com.tumbwe.examandclassattendanceapi.repository.AttendanceRecordRepository;
import com.tumbwe.examandclassattendanceapi.repository.CourseRepository;
import com.tumbwe.examandclassattendanceapi.repository.DepartmentRepository;
import com.tumbwe.examandclassattendanceapi.repository.StudentRepository;
import com.tumbwe.examandclassattendanceapi.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;

    @Override
    public Set<Student> getStudentsByDepartment(Long id) {
        var courses = courseRepository.findAllByDepartment(departmentRepository.findById(id).orElse(null));
        Set<Student> students = new HashSet<>();

        for (var course : courses) {
            var studentRecords = courseRepository.findStudentsByCourseCode(course.getCourseCode());
            students.addAll(studentRecords);
        }

        return students;
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

    @Override
    public List<AttendanceRecord> getAttendanceRecordByStudent(String id) {
        Student student = studentRepository.findByStudentId(id);
        if(student == null) {
            return new ArrayList<>();
        }
        return attendanceRecordRepository.findAllByStudentOrderByStudent(student);
    }

    @Override
    public List<AttendanceRecordDto> getAttendanceCount(String courseCode, String attendanceType, String year) {
        List<Object[]> rawRecords = attendanceRecordRepository.getAttendanceCount(courseCode, attendanceType, year);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


        return rawRecords.stream()
                .map(record -> new AttendanceRecordDto(
                        ((Number) record[0]).longValue(), // attendanceCount
                        (String) record[1],               // studentId
                        (String) record[2],               // courseCode
                        ((Date) record[3]).toLocalDate().format(formatter),               // timeStamp
                        (String) record[4],               // attendanceType
                        (String) record[5]                // fullName
                ))
                .toList();
    }

    @Override
    public List<AttendanceRecordDto> getPresent(String courseCode, String attendanceType, String year) {
        List<Object[]> rawRecords = attendanceRecordRepository.getPresentStudents(courseCode, attendanceType, "%"+year+"%");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return rawRecords.stream()
                .map(record -> new AttendanceRecordDto(
                        ((Number) 0).longValue(), // attendanceCount
                        (String) record[4],               // studentId
                        (String) record[3],               // courseCode
                        ((Date) record[2]).toLocalDate().format(formatter),               // timeStamp
                        (String) record[1],               // attendanceType
                        (String) record[5]                // fullName
                ))
                .toList();
    }

    @Override
    public List<AttendanceRecordDto> getAbsentStudents(String courseCode) {
        List<Object[]> rawRecords = attendanceRecordRepository.getAbsentStudents(courseCode);

        return rawRecords.stream()
                .map(record -> new AttendanceRecordDto(
                        ((Number) 0).longValue(), // attendanceCount
                        (String) record[1],               // studentId
                        (String) null,               // courseCode
                        (String) null,               // timeStamp
                        (String) null,               // attendanceType
                        (String) record[0]                // fullName
                ))
                .toList();
    }

}
