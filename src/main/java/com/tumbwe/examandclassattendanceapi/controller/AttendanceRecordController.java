package com.tumbwe.examandclassattendanceapi.controller;

import com.tumbwe.examandclassattendanceapi.dto.StartSession;
import com.tumbwe.examandclassattendanceapi.exception.ResourceNotFoundException;
import com.tumbwe.examandclassattendanceapi.model.AttendanceRecord;
import com.tumbwe.examandclassattendanceapi.model.AttendanceSessionInOut;
import com.tumbwe.examandclassattendanceapi.model.AttendanceType;
import com.tumbwe.examandclassattendanceapi.model.Student;
import com.tumbwe.examandclassattendanceapi.response.AttendanceRecordDTO;
import com.tumbwe.examandclassattendanceapi.service.AttendanceRecordService;
import com.tumbwe.examandclassattendanceapi.service.AttendanceSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/attendances")
@RequiredArgsConstructor
public class AttendanceRecordController {

    private final AttendanceSessionService attendanceSessionService;
    private final AttendanceRecordService attendanceRecordService;

    @PostMapping("/start-session")
    public ResponseEntity<?> startAttendanceTaking(@RequestBody StartSession in){
        try {
            AttendanceSessionInOut response = attendanceSessionService.startSession(in);
            return ResponseEntity.ok(response);
        }
        catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
         catch (Exception e){
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Saving session to database");
         }
    }

    @PostMapping("/mark-attendance")
    public ResponseEntity<?> markAttendance(@RequestBody AttendanceSessionInOut attendanceSessionIn){

        try {
          return  ResponseEntity.status(HttpStatus.CREATED).body(attendanceRecordService.addAttendanceRecord(attendanceSessionIn));

        }

        catch (Exception e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Students absent");
        }
    }
}
