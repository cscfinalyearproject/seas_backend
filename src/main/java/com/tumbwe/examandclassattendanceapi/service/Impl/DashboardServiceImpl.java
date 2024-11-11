package com.tumbwe.examandclassattendanceapi.service.Impl;

import com.tumbwe.examandclassattendanceapi.dto.*;
import com.tumbwe.examandclassattendanceapi.model.*;
import com.tumbwe.examandclassattendanceapi.repository.*;
import com.tumbwe.examandclassattendanceapi.response.AttendanceRecordDTO;
import com.tumbwe.examandclassattendanceapi.service.DashboardService;
import com.tumbwe.examandclassattendanceapi.service.EnrollmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final AttendanceSessionRepository attendanceSessionRepository;
    private final EnrollmentService enrollmentService;
    private final SchoolRepository schoolRepository;


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
    public List<NotificationDto> getLowAttendanceNotifications(Long department) {
        List<Object[]> results = attendanceRecordRepository.findLowAttendanceStudents(department);
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
    public List<CourseStatisticsDto> getCourseStatistics(Long department) {
        List<Object[]> results = attendanceRecordRepository.findCourseStatistics(department);
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


    @Override
    public List<YearDto> getDistinctYears() {
        List<Integer> results = attendanceRecordRepository.findDistinctYears();
        List<YearDto> years = new ArrayList<>();

        for (Integer year : results) {
            years.add(new YearDto(year));
        }
        return years;
    }

    @Override
    public List<Map<String, Object>> getCourseAttendanceTrends(Long departmentId) {
        List<Object[]> results = attendanceRecordRepository.findCourseAttendance(departmentId);
        Map<String, Map<String, Object>> courseSessions = new LinkedHashMap<>();

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

        return new ArrayList<>(courseSessions.values());
    }



    @Override
    public List<SessionAttendanceDto> getSessionAttendance(Long department) {
        List<Object[]> results = attendanceRecordRepository.findSessionAttendance(department);
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

    @Transactional
    @Override
    public List<String> saveStudent(MultipartFile file) throws Exception{
        Workbook workbook = new XSSFWorkbook(file.getInputStream());

            int numberOfSheets = workbook.getNumberOfSheets();
            List<String> savedStudents = new ArrayList<>();
            // Iterate through each sheet in the workbook
            for (int i = 0; i < numberOfSheets; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName();
                System.out.println("Processing sheet: " + sheetName);

                // Iterate through each row in the current sheet
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) {
                        continue; // Skip header row
                    }

                    String studentId = getCellValueAsString(row.getCell(0));
                    String fullName = getCellValueAsString(row.getCell(1));
                    String intake = getCellValueAsString(row.getCell(2));
                    Student student = new Student();
                    student.setStudentId(studentId);
                    student.setIntake(intake);
                    student.setFullName(fullName);

                    var saved = studentRepository.save(student);


                    if(!saved.equals(student)) {
                        for (int j = 3; j < row.getLastCellNum(); j++) {
                            String courseCode = getCellValueAsString(row.getCell(j)).trim(); // Course Code
                            if (!courseCode.isEmpty()) {
                                EnrollmentDto enrollmentDto = new EnrollmentDto();
                                enrollmentDto.setStudentId(studentId);
                                enrollmentDto.setCourseCode(courseCode);
                                enrollmentService.addStudentToCourse(enrollmentDto);
                            }
                        }
                        savedStudents.add(studentId);
                    }
                }
            }
        return savedStudents;
    }

    @Override
    public List<String> saveCourse(MultipartFile file) throws Exception {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());

        int numberOfSheets = workbook.getNumberOfSheets();
        List<String> savedStudents = new ArrayList<>();
        // Iterate through each sheet in the workbook
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            String sheetName = sheet.getSheetName();
            System.out.println("Processing sheet: " + sheetName);

            // Iterate through each row in the current sheet
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Skip header row
                }

                String courseName = getCellValueAsString(row.getCell(0));
                String courseCode = getCellValueAsString(row.getCell(1));
                String semester = getCellValueAsString(row.getCell(2));
                String departmentName = getCellValueAsString(row.getCell(3));
                String schoolName = Objects.requireNonNull(file.getOriginalFilename()).substring(0, file.getOriginalFilename().lastIndexOf('.'));
                Course course = new Course();
                course.setCourseName(courseName);
                course.setCourseCode(courseCode);
                course.setSemester(Byte.parseByte(semester));
                Department department = departmentRepository.findByName(departmentName);
                School school = schoolRepository.findByName(schoolName);

                if(school == null){
                    School school1 = new School();
                    school1.setName(schoolName);
                    school = schoolRepository.save(school1);
                }
                if (department == null) {
                    Department newDepartment = new Department();
                    newDepartment.setName(departmentName);
                    newDepartment.setSchool(school);
                    departmentRepository.save(newDepartment);
                    department = newDepartment;
                }



                course.setDepartment(department);
                var saved = courseRepository.save(course);
                if(!saved.equals(course)) {
                    savedStudents.add(courseCode);
                }
            }
        }
        return savedStudents;
    }

    @Override
    public List<Long> saveAttendanceSession(MultipartFile file) throws Exception {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());

        int numberOfSheets = workbook.getNumberOfSheets();
        List<Long> savedStudents = new ArrayList<>();
        // Iterate through each sheet in the workbook
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            String sheetName = sheet.getSheetName();
            System.out.println("Processing sheet: " + sheetName);

            // Iterate through each row in the current sheet
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Skip header row
                }

                String attendanceType = getCellValueAsString(row.getCell(0));
                String status = getCellValueAsString(row.getCell(1));
                String courseCode = getCellValueAsString(row.getCell(2));
                Course course = courseRepository.findByCourseCode(courseCode).get();
                AttendanceSession session = new AttendanceSession();
                session.setAttendanceType(attendanceType);
                session.setSessionStatus(status);
                session.setCourse(course);
                session.setTimeStamp(LocalDate.now());
                var saved = attendanceSessionRepository.save(session);

                if(!saved.equals(session)) {
                    savedStudents.add(saved.getAttendanceSessionId());
                }
            }
        }
        return savedStudents;
    }

    @Override
    public List<OverallStudentDto> getOverallAttendance(Long departmentId, int limit) {
        List<Object[]> results = attendanceRecordRepository.findTopThreeOverallAttendance(departmentId, limit);
        return results.stream()
                .map(result -> new OverallStudentDto((String) result[0], (String) result[1] + "%"))
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseStudentProjection> getStudentsByDepartmentAndIntake(Long departmentId, String intake) {
        return attendanceRecordRepository.findStudentsByDepartmentAndIntake(departmentId, intake);
    }

    @Override
    public List<Long> saveAttendanceRecord(MultipartFile file, Long session_id) throws Exception {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());

        int numberOfSheets = workbook.getNumberOfSheets();
        List<Long> savedStudents = new ArrayList<>();
        // Iterate through each sheet in the workbook
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            String sheetName = sheet.getSheetName();
            System.out.println("Processing sheet: " + sheetName);

            // Iterate through each row in the current sheet
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Skip header row
                }

                String attendanceType = getCellValueAsString(row.getCell(0));
                String studentId = getCellValueAsString(row.getCell(2));
                String courseCode = getCellValueAsString(row.getCell(1));
                Course course = courseRepository.findByCourseCode(courseCode).get();
                AttendanceRecord record = new AttendanceRecord();
                record.setAttendanceType(attendanceType);
                Student student = studentRepository.findById(studentId).get();
                record.setStudent(student);
                record.setCourse(course);
                record.setTimeStamp(LocalDate.now());
                var saved = attendanceRecordRepository.save(record);

                if(!saved.equals(record)) {
                    savedStudents.add(saved.getAttendanceId());
                }
            }
        }
        return savedStudents;
    }

    private String getCellValueAsString(org.apache.poi.ss.usermodel.Cell cell) {
        if (cell == null) {
            return ""; // If the cell is empty, return an empty string
        }

        CellType cellType = cell.getCellType();

        return switch (cellType) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue()); // Handle numeric values
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> ""; // Default to empty string for other types
        };
    }

}
