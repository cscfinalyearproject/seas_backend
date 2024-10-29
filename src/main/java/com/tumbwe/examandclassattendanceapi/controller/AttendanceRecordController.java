package com.tumbwe.examandclassattendanceapi.controller;

import com.tumbwe.examandclassattendanceapi.dto.StartSession;
import com.tumbwe.examandclassattendanceapi.exception.ResourceNotFoundException;
import com.tumbwe.examandclassattendanceapi.model.AttendanceSessionOut;
import com.tumbwe.examandclassattendanceapi.service.AttendanceRecordService;
import com.tumbwe.examandclassattendanceapi.service.AttendanceSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(path = "/api/v1/attendances")
@RequiredArgsConstructor
public class AttendanceRecordController {

    private final AttendanceSessionService attendanceSessionService;
    private final AttendanceRecordService attendanceRecordService;


    @PostMapping(path = "/mark-attendance")
    public ResponseEntity<?> markAttendance(@RequestBody AttendanceSessionOut attendanceSessionIn){

        try {
          attendanceRecordService.addAttendanceRecord(attendanceSessionIn);
          return ResponseEntity.ok("Attendance marked successfully");

        }

        catch (Exception e){
            return  ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping(path = "/attendance-types")
    public ResponseEntity<?> getAttendaceTypes(){
        try {
            return ResponseEntity.ok(attendanceSessionService.getAttendaceStatuses());
        }
        catch (Exception e) {
            return ResponseEntity.ok(e.getMessage());
        }    }
}
