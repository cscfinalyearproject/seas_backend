package com.tumbwe.examandclassattendanceapi.controller;


import com.tumbwe.examandclassattendanceapi.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
