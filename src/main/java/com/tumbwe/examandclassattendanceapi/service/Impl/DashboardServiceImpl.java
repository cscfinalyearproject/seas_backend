package com.tumbwe.examandclassattendanceapi.service.Impl;

import com.tumbwe.examandclassattendanceapi.dto.*;
import com.tumbwe.examandclassattendanceapi.model.AttendanceRecord;
import com.tumbwe.examandclassattendanceapi.model.AttendanceSession;
import com.tumbwe.examandclassattendanceapi.model.Course;
import com.tumbwe.examandclassattendanceapi.model.Student;
import com.tumbwe.examandclassattendanceapi.repository.*;
import com.tumbwe.examandclassattendanceapi.response.AttendanceRecordDTO;
import com.tumbwe.examandclassattendanceapi.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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

    @Override
    public List<CourseResponseDto> getCourseByDepartment(Long id) {
        var dep = departmentRepository.findById(id).orElse(null);
        if(dep == null) {
            return new ArrayList<>();
        }
        var courses = courseRepository.findAllByDepartment(dep);

        List<CourseResponseDto> courseResponseDtos = new ArrayList<>();

        for (var course : courses) {
            CourseResponseDto courseResponseDto = new CourseResponseDto();
            courseResponseDto.setCourseCode(course.getCourseCode());
            courseResponseDto.setCourseName(course.getCourseName());
            courseResponseDto.setDepartmentId(dep.getId());
            courseResponseDtos.add(courseResponseDto);
        }

        return courseResponseDtos;
    }

    @Override
    public StudentAttendanceDto getStudentAttendanceById(String id) {
            List<Object[]> results = attendanceRecordRepository.findAttendanceByStudentId(id);
            List<AttendanceResponseDto> attendanceRecords = new ArrayList<>();

            for (Object[] result : results) {
                String attendanceType = (String) result[1];
                String courseCode = (String) result[2];
                String sessionTime = result[3].toString();
                String status = (String) result[4];

                AttendanceResponseDto attendanceRecord = new AttendanceResponseDto(attendanceType, courseCode, sessionTime, status);
                attendanceRecords.add(attendanceRecord);
            }
        return new StudentAttendanceDto(id, attendanceRecords);
    }

    @Override
    public List<NotificationDto> getLowAttendanceNotifications() {
        List<Object[]> results = attendanceRecordRepository.findLowAttendanceStudents();
        List<NotificationDto> notifications = new ArrayList<>();

        for (Object[] result : results) {
            String studentId = (String) result[0];
            String fullName = (String) result[1];
            String courseCode = (String) result[2];
            String courseName = (String) result[3];

            // Cast to BigDecimal and convert to Double
            BigDecimal attendancePercentageDecimal = (BigDecimal) result[4];
            double attendancePercentage = attendancePercentageDecimal.doubleValue();

            NotificationDto notification = new NotificationDto(studentId, fullName, courseCode, courseName, attendancePercentage);
            notifications.add(notification);
        }

        return notifications;
    }

    @Override
    public List<CourseStatisticsDto> getCourseStatistics() {
        List<Object[]> results = attendanceRecordRepository.findCourseStatistics();
        List<CourseStatisticsDto> statistics = new ArrayList<>();

        for (Object[] result : results) {
            String courseCode = (String) result[0];
            String courseName = (String) result[1];

            int totalEnrolledStudents = ((Long) result[2]).intValue();

            // Ensure correct casting for BigDecimal
            double averageAttendancePercentage = ((BigDecimal) result[3]).doubleValue();

            // Cast to Long for total classes held
            int totalClassesHeld = ((Long) result[4]).intValue();

            CourseStatisticsDto dto = new CourseStatisticsDto(courseCode, courseName, totalEnrolledStudents, averageAttendancePercentage, totalClassesHeld);
            statistics.add(dto);
        }
        return statistics;
    }


    public List<YearDto> getDistinctYears() {
        List<Integer> results = attendanceRecordRepository.findDistinctYears();
        List<YearDto> years = new ArrayList<>();

        for (Integer year : results) {
            years.add(new YearDto(year));
        }
        return years;
    }

    @Override
    public List<CourseAttendanceDto> getCourseAttendance() {
        List<Object[]> results = attendanceRecordRepository.findCourseAttendance();
        List<CourseAttendanceDto> attendanceList = new ArrayList<>();

        for (Object[] result : results) {
            String courseCode = (String) result[0];
            String courseName = (String) result[1];

            // Cast the result to Long instead of BigInteger
            int presentStudents = ((Long) result[2]).intValue();
            int absentStudents = ((Long) result[3]).intValue();

            CourseAttendanceDto dto = new CourseAttendanceDto(courseCode, courseName, presentStudents, absentStudents);
            attendanceList.add(dto);
        }
        return attendanceList;
    }


    @Override
    public List<SessionAttendanceDto> getSessionAttendance() {
        List<Object[]> results = attendanceRecordRepository.findSessionAttendance();
        List<SessionAttendanceDto> sessionAttendanceList = new ArrayList<>();

        for (Object[] result : results) {
            String sessionDate = result[0].toString(); // Convert timestamp to string
            String courseCode = (String) result[1];
            String courseName = (String) result[2];

            // Cast to Long instead of BigInteger
            int presentStudents = ((Long) result[3]).intValue();
            int absentStudents = ((Long) result[4]).intValue();

            SessionAttendanceDto dto = new SessionAttendanceDto(sessionDate, courseCode, courseName, presentStudents, absentStudents);
            sessionAttendanceList.add(dto);
        }
        return sessionAttendanceList;
    }


    public List<DashboardCourseDto> getDistinctCourses() {
        List<Object[]> results = courseRepository.findDistinctCourses();
        List<DashboardCourseDto> courses = new ArrayList<>();

        for (Object[] result : results) {
            String courseCode = (String) result[0];
            String courseName = (String) result[1];

            DashboardCourseDto dto = new DashboardCourseDto(courseCode, courseName);
            courses.add(dto);
        }
        return courses;
    }

}
