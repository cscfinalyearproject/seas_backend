package com.tumbwe.examandclassattendanceapi.controller;


import com.tumbwe.examandclassattendanceapi.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@CrossOrigin("*")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping(path = "/getStudentsByDepartment/{id}")
    public ResponseEntity<?> findByDepartment(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(dashboardService.getStudentsByDepartment(id));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/getStudentsByCourse/{courseCode}")
    public ResponseEntity<?> findByCourse(@PathVariable("courseCode") String courseCode) {
        try {
            return ResponseEntity.ok(dashboardService.getStudentsByCourse(courseCode));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/getAttendanceByCourse/{courseCode}")
    public ResponseEntity<?> findByAttendanceByCourse(@PathVariable("courseCode") String courseCode) {
        try {
            return ResponseEntity.ok(dashboardService.getAttendanceRecordByCourse(courseCode));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/getAttendanceByStudent/{studentId}")
    public ResponseEntity<?> findByAttendanceByStudent(@PathVariable("studentId") String studentId) {
        try {
            return ResponseEntity.ok(dashboardService.getAttendanceRecordByStudent(studentId));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/getStudentByCourse")
    public ResponseEntity<?> findByStudentByCourse(@RequestParam("courseCode") String courseCode, @RequestParam("attendanceType") String attendanceType, @RequestParam("year") String year) {
        try {
            return ResponseEntity.ok(dashboardService.getAttendanceCount(courseCode,attendanceType,year));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/getPresent")
    public ResponseEntity<?> findPresent(@RequestParam("courseCode") String courseCode, @RequestParam("attendanceType") String attendanceType, @RequestParam("date") String date) {
        try {
            return ResponseEntity.ok(dashboardService.getPresent(courseCode,attendanceType,date));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/getAbsent")
    public ResponseEntity<?> findAbsent(@RequestParam("courseCode") String courseCode) {
        try {
            return ResponseEntity.ok(dashboardService.getAbsentStudents(courseCode));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/getRecords/{sessionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRecords(@PathVariable Long sessionId) {
        try {
            return ResponseEntity.ok(dashboardService.getRecords(sessionId));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/getSessions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSessions(@RequestParam("courseCode") String courseCode) {
        try {
            return ResponseEntity.ok(dashboardService.getSessions(courseCode));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/getCourseByDepartment/{departmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCourseByDepartment(@PathVariable Long departmentId) {
        try {
            return ResponseEntity.ok(dashboardService.getCourseByDepartment(departmentId));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/getStudentAttendance/{studentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getStudentAttendance(@PathVariable String studentId) {
        try {
            return ResponseEntity.ok(dashboardService.getStudentAttendanceById(studentId));
        }catch (Exception e) {

            String name = "hfhjfd";

            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/notifications/{departmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getNotifications(@PathVariable Long departmentId) {
        try {
            return ResponseEntity.ok(dashboardService.getLowAttendanceNotifications(departmentId));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/statistics/course/{departmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getStatistics(@PathVariable Long departmentId) {
        try {
            return ResponseEntity.ok(dashboardService.getCourseStatistics(departmentId));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/filters/years", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFilterYear() {
        try {
            return ResponseEntity.ok(dashboardService.getDistinctYears());
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/statistics/attendance-trends/{departmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCourseAttendance(@PathVariable Long departmentId) {
        try {
            return ResponseEntity.ok(dashboardService.getCourseAttendance(departmentId));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/statistics/attendance-comparison/{departmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> attendanceComparison(@PathVariable Long departmentId) {
        try {
            return ResponseEntity.ok(dashboardService.getSessionAttendance(departmentId));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/filters/courses", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> filterCourses() {
        try {
            return ResponseEntity.ok(dashboardService.getDistinctCourses());
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/attendance/get-top-attendees", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTopThree() {
        try {
            return ResponseEntity.ok(dashboardService.getOverallAttendance());
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    @PostMapping(path = "/import/students", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importStudents(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(dashboardService.saveStudent(file));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping(path = "/import/courses", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importCourses(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(dashboardService.saveCourse(file));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping(path = "/import/sessions", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importSessions(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(dashboardService.saveAttendanceSession(file));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping(path = "/import/record", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importRecord(@RequestParam("file") MultipartFile file, @RequestParam("session_id") Long session_id) {
        try {
            return ResponseEntity.ok(dashboardService.saveAttendanceRecord(file, session_id));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }




}
