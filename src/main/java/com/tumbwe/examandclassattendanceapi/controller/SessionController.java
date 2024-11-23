package com.tumbwe.examandclassattendanceapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tumbwe.examandclassattendanceapi.dto.StartSession;
import com.tumbwe.examandclassattendanceapi.exception.ResourceNotFoundException;
import com.tumbwe.examandclassattendanceapi.model.AttendanceSession;
import com.tumbwe.examandclassattendanceapi.model.Course;
import com.tumbwe.examandclassattendanceapi.model.Student;
import com.tumbwe.examandclassattendanceapi.repository.AttendanceSessionRepository;
import com.tumbwe.examandclassattendanceapi.repository.CourseRepository;
import com.tumbwe.examandclassattendanceapi.service.AttendanceSessionService;
import com.tumbwe.examandclassattendanceapi.service.Impl.SessionServiceImpl;
import com.tumbwe.examandclassattendanceapi.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/session")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;


    @GetMapping(path = "/", produces = "application/json")
    public ResponseEntity<List<Student>> downloadStudents(@RequestParam String course, @RequestParam String sessionType) {
        List<Student> students = sessionService.downloadStudents(course, sessionType);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=students.json");

        return new ResponseEntity<>(students, headers, HttpStatus.OK);
    }

    @GetMapping(path = "/is-session-available", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkSession(@RequestParam(name = "deviceId", defaultValue = "default") String deviceId){

        try {
            return ResponseEntity.ok(sessionService.isSessionAvailable(deviceId));
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }


    @PostMapping("/attendance/start-session")
    public ResponseEntity<?> startAttendanceTaking(StartSession in) {
        try {
           return  ResponseEntity.ok(sessionService.startSession(in));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }


    }
}
