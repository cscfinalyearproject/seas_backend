package com.tumbwe.examandclassattendanceapi.controller;

import com.tumbwe.examandclassattendanceapi.service.DeanDashBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dean")
@RequiredArgsConstructor
@CrossOrigin("*")
public class DeanController {
    private final DeanDashBoard deanDashBoard;

    @GetMapping(path = "/getStudents/{schoolId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getStudents(@PathVariable Long schoolId) {
        try {
            return ResponseEntity.ok(deanDashBoard.getStudents(schoolId));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/getCourses/{schoolId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCoursesBySchool(@PathVariable Long schoolId, @RequestParam int year) {
        try {
            return ResponseEntity.ok(deanDashBoard.getCourseBySchool(schoolId, year));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/getLowAttendanceNotifications/{schoolId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLowAttendanceNotifications(@PathVariable Long schoolId) {
        try {
            return ResponseEntity.ok(deanDashBoard.getLowAttendanceNotifications(schoolId));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @GetMapping(path = "/statistics/course/{departmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getStatistics(@PathVariable Long departmentId, @RequestParam String from, @RequestParam String to, @RequestParam int year) {
        try {
            return ResponseEntity.ok(deanDashBoard.getCourseStatistics(departmentId,from,to,year));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/statistics/attendance-trends/{schoolId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCourseAttendance(@PathVariable Long schoolId, @RequestParam String from, @RequestParam String to, @RequestParam int year) {
        try {
            return ResponseEntity.ok(deanDashBoard.getCourseAttendanceTrends(schoolId, from, to, year));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/statistics/attendance-comparison/{schoolId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> attendanceComparison(@PathVariable Long schoolId, @RequestParam String to, @RequestParam String from, @RequestParam int year) {
        try {
            return ResponseEntity.ok(deanDashBoard.getSessionAttendance(schoolId, from,to,year));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/attendance/get-top-attendees/{schoolId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTopThree(@PathVariable Long schoolId, @RequestParam int limit, @RequestParam String from, @RequestParam String to, @RequestParam int year) {
        try {
            return ResponseEntity.ok(deanDashBoard.getOverallAttendance(schoolId, limit, from, to, year));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/attendance/get-by-department-and-intake/{intake}/{schoolId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getByDepartmentAndIntake(@PathVariable Long schoolId, @PathVariable String intake) {
        try {
            return ResponseEntity.ok(deanDashBoard.getStudentsByDepartmentAndIntake(schoolId,intake));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
