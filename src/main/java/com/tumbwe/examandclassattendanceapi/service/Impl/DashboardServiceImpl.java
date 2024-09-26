package com.tumbwe.examandclassattendanceapi.service.Impl;

import com.tumbwe.examandclassattendanceapi.dto.AttendanceRecordDto;
import com.tumbwe.examandclassattendanceapi.dto.AttendanceSessionDto;
import com.tumbwe.examandclassattendanceapi.model.AttendanceRecord;
import com.tumbwe.examandclassattendanceapi.model.AttendanceSession;
import com.tumbwe.examandclassattendanceapi.model.Course;
import com.tumbwe.examandclassattendanceapi.model.Student;
import com.tumbwe.examandclassattendanceapi.repository.*;
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
    private final AttendanceSessionRepository attendanceSessionRepository;

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


        return new ArrayList<>();
    }

    @Override
    public List<AttendanceRecordDto> getPresent(String courseCode, String attendanceType, String year) {
        List<Object[]> rawRecords = attendanceRecordRepository.getPresentStudents(courseCode, attendanceType, "%"+year+"%");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return new ArrayList<>();
    }

    @Override
    public List<AttendanceRecordDto> getAbsentStudents(String courseCode) {
        List<Object[]> rawRecords = attendanceRecordRepository.getAbsentStudents(courseCode);

        return new ArrayList<>();
    }

    @Override
    public List<AttendanceSessionDto> getSessions(String courseCode) {
        Course course = courseRepository.findByCourseCode(courseCode).orElseThrow(null);

        if(course == null) {
            return new ArrayList<>();
        }

        var sessions = attendanceSessionRepository.findAllByCourse(course);

        List<AttendanceSessionDto> attendanceSessions = new ArrayList<>();

        for (var session : sessions) {
            AttendanceSessionDto attendanceSession = new AttendanceSessionDto();
            attendanceSession.setSessionStatus(session.getSessionStatus().toString());
            attendanceSession.setAttendanceType(session.getAttendanceType().toString());
            attendanceSession.setCourseCode(session.getCourse().getCourseCode());
            attendanceSession.setTimeStamp(session.getTimeStamp().toString());
            attendanceSession.setAttendanceSessionId(session.getAttendanceSessionId());

            attendanceSessions.add(attendanceSession);
        }

        return attendanceSessions;
    }

    @Override
    public List<AttendanceRecordDto> getRecords(Long id) {
        AttendanceSession session = attendanceSessionRepository.findById(id).orElse(null);

        if (session == null){
            return new ArrayList<>();
        }
        var attendanceRecords = attendanceRecordRepository.getAttendanceRecordBySession(session);
        List<AttendanceRecordDto> attendanceRecordDtos = new ArrayList<>();

        for (var attendanceRecord : attendanceRecords) {
            AttendanceRecordDto attendanceRecordDto = new AttendanceRecordDto();
            attendanceRecordDto.setAttendanceType(attendanceRecord.getAttendanceType().toString());
            attendanceRecordDto.setStudentId(attendanceRecord.getStudent().getStudentId());
            studentRepository.findById(attendanceRecord.getStudent().getStudentId()).ifPresent(student -> attendanceRecordDto.setFullName(student.getFullName()));
            attendanceRecordDto.setCourseCode(attendanceRecord.getCourse().getCourseCode());
            attendanceRecordDto.setTimeStamp(attendanceRecord.getTimeStamp().toString());

            attendanceRecordDtos.add(attendanceRecordDto);
        }
        return attendanceRecordDtos;
    }

}
