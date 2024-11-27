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
    public ResponseEntity<?> getStudents(@PathVariable Long schoolId, @RequestParam(required = false) Integer year) {
        try {
            return ResponseEntity.ok(deanDashBoard.getStudents(schoolId, year));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/getDepartmentBySchool/{schoolId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getStudents(@PathVariable Long schoolId) {
        try {
            return ResponseEntity.ok(deanDashBoard.getAllDepartmentsBySchool(schoolId));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/getCourses/{schoolId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCoursesBySchool(@PathVariable Long schoolId, @RequestParam(required = false) Integer year) {
        try {
            return ResponseEntity.ok(deanDashBoard.getCourseBySchool(schoolId,year));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/getLowAttendanceNotifications/{schoolId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLowAttendanceNotifications(@PathVariable Long schoolId, @RequestParam(required = false) Integer year) {
        try {
            return ResponseEntity.ok(deanDashBoard.getLowAttendanceNotifications(schoolId, year));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @GetMapping(path = "/statistics/course/{schoolId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getStatistics(@PathVariable Long schoolId, @RequestParam(required = false) String from, @RequestParam(required = false) String to, @RequestParam(required = false) Integer year) {
        try {
            return ResponseEntity.ok(deanDashBoard.getCourseStatistics(schoolId,from,to,year));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/statistics/attendance-trends/{schoolId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCourseAttendance(@PathVariable Long schoolId, @RequestParam(required = false) String from, @RequestParam(required = false) String to, @RequestParam(required = false) Integer year) {
        try {
            return ResponseEntity.ok(deanDashBoard.getCourseAttendanceTrends(schoolId, from, to, year));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/statistics/attendance-comparison/{schoolId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> attendanceComparison(@PathVariable Long schoolId, @RequestParam(required = false) String to, @RequestParam(required = false) String from, @RequestParam(required = false) Integer year) {
        try {
            return ResponseEntity.ok(deanDashBoard.getSessionAttendance(schoolId, from,to,year));
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(path = "/attendance/get-top-attendees/{schoolId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTopThree(@PathVariable Long schoolId, @RequestParam int limit, @RequestParam(required = false) String from, @RequestParam(required = false) String to, @RequestParam(required = false) Integer year) {
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
