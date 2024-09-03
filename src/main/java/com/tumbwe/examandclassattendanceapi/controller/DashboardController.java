package com.tumbwe.examandclassattendanceapi.controller;


import com.tumbwe.examandclassattendanceapi.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
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

    @GetMapping(path = "/getStudentsByDepartment/{courseCode}")
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



}
