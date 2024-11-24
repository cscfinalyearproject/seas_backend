package com.tumbwe.examandclassattendanceapi.service.Impl;

import com.tumbwe.examandclassattendanceapi.dto.*;
import com.tumbwe.examandclassattendanceapi.model.Course;
import com.tumbwe.examandclassattendanceapi.model.Department;
import com.tumbwe.examandclassattendanceapi.model.School;
import com.tumbwe.examandclassattendanceapi.model.Student;
import com.tumbwe.examandclassattendanceapi.repository.*;
import com.tumbwe.examandclassattendanceapi.service.DeanDashBoard;
import com.tumbwe.examandclassattendanceapi.utils.CourseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DeanDashBoardImpl implements DeanDashBoard {

    private final SchoolRepository schoolRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;
    private final CourseUtils courseUtils;
    private final AttendanceSessionRepository attendanceSessionRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;

    @Override
    public Set<Student> getStudents(Long schoolId) {
        Optional<School> school = schoolRepository.findById(schoolId);

        if(school.isEmpty()){
            return new HashSet<>();
        }
        List<Department> departments = departmentRepository.findAllBySchool(school.get());
        Set<Student> students = new HashSet<>();
        for(Department department : departments){
            List<Course> courses = courseRepository.findAllByDepartment(department);
            for (var course : courses) {
                var studentRecords = courseRepository.findStudentsByCourseCode(course.getCourseCode());
                students.addAll(studentRecords);
            }
        }
        return students;
    }


    @Override
    public List<CourseResponseDto> getCourseBySchool(Long id, Integer year) {
        Optional<School> school = schoolRepository.findById(id);

        if(school.isEmpty()){
            return new ArrayList<>();
        }
        List<Department> departments = departmentRepository.findAllBySchool(school.get());

        List<CourseResponseDto> courseResponseDtos = new ArrayList<>();

        for(Department department : departments){
            List<Course> courses = null;
            if(year == null){
                courses = courseRepository.findAllByDepartment(department);
            }else{
                List<String> courseCodes = courseUtils.getCourseCodes(department.getId(), year);
                courses = courseRepository.findAllByCourseCodes(courseCodes);
            }

            for (var course : courses) {
                CourseResponseDto courseResponseDto = new CourseResponseDto();
                courseResponseDto.setCourseCode(course.getCourseCode());
                courseResponseDto.setCourseName(course.getCourseName());
                courseResponseDto.setDepartmentId(department.getId());
                courseResponseDtos.add(courseResponseDto);
            }
        }
        return courseResponseDtos;
    }

    @Override
    public List<NotificationDto> getLowAttendanceNotifications(Long schoolId) {
        Optional<School> school = schoolRepository.findById(schoolId);

        if(school.isEmpty()){
            return new ArrayList<>();
        }
        List<Department> departments = departmentRepository.findAllBySchool(school.get());
        List<NotificationDto> notifications = new ArrayList<>();

        for(Department department : departments){
            List<Object[]> results = attendanceRecordRepository.findLowAttendanceStudents(department.getId());
            for (Object[] result : results) {
                String studentId = (String) result[0];
                String fullName = (String) result[1];
                String courseCode = (String) result[2];
                String courseName = (String) result[3];
                BigDecimal attendancePercentageDecimal = (BigDecimal) result[4];
                double attendancePercentage = attendancePercentageDecimal.doubleValue();

                NotificationDto notification = new NotificationDto(studentId, fullName, courseCode, courseName, attendancePercentage);
                notifications.add(notification);
            }
        }
        return notifications;
    }

    @Override
    public List<CourseStatisticsDto> getCourseStatistics(Long schoolId, String from, String to, Integer year) {
        Optional<School> school = schoolRepository.findById(schoolId);

        if(school.isEmpty()){
            return new ArrayList<>();
        }

        List<CourseStatisticsDto> statistics = new ArrayList<>();
        List<Department> departments = departmentRepository.findAllBySchool(school.get());
        for(Department department : departments){

            List<Object[]> results = null;
            if(from == null){
                results = attendanceRecordRepository.findCourseStatistics(department.getId());
            }else{
                List<String> courseCodes = courseUtils.getCourseCodes(department.getId(),year);
                results = attendanceRecordRepository.findCourseStatistics(department.getId(),from,to,courseCodes);
            }

            for (Object[] result : results) {
                String courseCode = (String) result[0];
                String courseName = (String) result[1];
                int totalEnrolledStudents = ((Long) result[2]).intValue();
                double averageAttendancePercentage = ((BigDecimal) result[3]).doubleValue();
                int totalClassesHeld = ((Long) result[4]).intValue();
                CourseStatisticsDto dto = new CourseStatisticsDto(courseCode, courseName, totalEnrolledStudents, averageAttendancePercentage, totalClassesHeld);
                statistics.add(dto);
            }
        }

        return statistics;
    }

    @Override
    public List<Map<String, Object>> getCourseAttendanceTrends(Long schoolId, String from, String to, Integer year) {
        Optional<School> school = schoolRepository.findById(schoolId);

        if(school.isEmpty()){
            return new ArrayList<>();
        }

        Map<String, Map<String, Object>> courseSessions = new LinkedHashMap<>();

        List<Department> departments = departmentRepository.findAllBySchool(school.get());

        for(Department department : departments){

            List<Object[]> results = null;
            if(from == null){
                results = attendanceRecordRepository.findCourseAttendance(department.getId());
            }else{
                List<String> courseCodes = courseUtils.getCourseCodes(department.getId(),year);
                results = attendanceRecordRepository.findCourseAttendance(department.getId(), from, to, courseCodes);
            }

            for (Object[] row : results) {
                String courseCode = (String) row[0];
                String courseName = (String) row[1];
                Date sessionDate = (Date) row[2];
                BigDecimal attendancePercentageBigDecimal = (BigDecimal) row[3];

                Double attendancePercentage = attendancePercentageBigDecimal != null ? attendancePercentageBigDecimal.doubleValue() : null;

                courseSessions.computeIfAbsent(courseCode, k -> {
                    Map<String, Object> courseData = new LinkedHashMap<>();
                    courseData.put("courseCode", courseCode);
                    courseData.put("courseName", courseName);
                    courseData.put("sessions", new ArrayList<>());
                    return courseData;
                });

                Map<String, Object> sessionData = new HashMap<>();
                sessionData.put("sessionDate", sessionDate.toString());
                sessionData.put("attendancePercentage", attendancePercentage);

                ((List<Object>) courseSessions.get(courseCode).get("sessions")).add(sessionData);
            }
        }

        return new ArrayList<>(courseSessions.values());
    }

    @Override
    public List<SessionAttendanceDto> getSessionAttendance(Long schoolId, String from, String to, Integer year) {
        Optional<School> school = schoolRepository.findById(schoolId);

        if(school.isEmpty()){
            return new ArrayList<>();
        }
        List<Department> departments = departmentRepository.findAllBySchool(school.get());
        List<SessionAttendanceDto> sessionAttendanceList = new ArrayList<>();
        for(Department department : departments){
            List<Object[]> results = null;
            if(from == null){
                results = attendanceRecordRepository.findSessionAttendance(department.getId());
            }else{
                List<String> courseCodes = courseUtils.getCourseCodes(department.getId(),year);
                results = attendanceRecordRepository.findSessionAttendance(department.getId(), from, to,courseCodes);
            }

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

        }
        return sessionAttendanceList;
    }

    @Override
    public List<OverallStudentDto> getOverallAttendance(Long schoolId, int limit, String from, String to, Integer year) {
        Optional<School> school = schoolRepository.findById(schoolId);

        if (school.isEmpty()) {
            return new ArrayList<>();
        }

        List<Department> departments = departmentRepository.findAllBySchool(school.get());
        List<OverallStudentDto> overallAttendanceList = new ArrayList<>();

        // Process each department
        for (Department department : departments) {
            List<Object[]> results = null;
            if(from != null && year != null){
                List<String> courseCodes = courseUtils.getCourseCodes(department.getId(), year);
                results = attendanceRecordRepository.findTopThreeOverallAttendance(
                        department.getId(), limit, from, to, courseCodes
                );
            }
            if(from != null){
                results = attendanceRecordRepository.findTopThreeOverallAttendance(limit, department.getId(),from, to);
            }else if( year != null){
                List<String> courseCodes = courseUtils.getCourseCodes(department.getId(), year);
                results = attendanceRecordRepository.findTopThreeOverallAttendance(
                        department.getId(), limit, courseCodes
                );
            }
            else{
                // Fetch attendance data
                results = attendanceRecordRepository.findTopThreeOverallAttendance(
                        department.getId(), limit
                );
            }


            // Map results to DTOs and add to the final list
            results.stream()
                    .map(result -> new OverallStudentDto((String) result[0], (String) result[1] + "%"))
                    .forEach(overallAttendanceList::add);
        }

        return overallAttendanceList;
    }

    @Override
    public List<CourseStudentProjection> getStudentsByDepartmentAndIntake(Long schoolId, String intake) {
        List<CourseStudentProjection> courseStudentProjections = new ArrayList<>();

        // Handle the case where the school is not found
        Optional<School> school = schoolRepository.findById(schoolId);
        if (school.isEmpty()) {
            return courseStudentProjections; // Return an empty list if the school is not found
        }

        // Get all departments for the school
        List<Department> departments = departmentRepository.findAllBySchool(school.get());

        // Fetch students for each department
        for (Department department : departments) {
            List<CourseStudentProjection> students = attendanceRecordRepository.findStudentsByDepartmentAndIntake(department.getId(), intake);
            courseStudentProjections.addAll(students); // Aggregate results into the main list
        }

        return courseStudentProjections;
    }

}
