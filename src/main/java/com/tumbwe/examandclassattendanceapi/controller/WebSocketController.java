package com.tumbwe.examandclassattendanceapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tumbwe.examandclassattendanceapi.dto.StartSession;
import com.tumbwe.examandclassattendanceapi.exception.ResourceNotFoundException;
import com.tumbwe.examandclassattendanceapi.model.AttendanceSession;
import com.tumbwe.examandclassattendanceapi.model.AttendanceSessionOut;
import com.tumbwe.examandclassattendanceapi.model.Course;
import com.tumbwe.examandclassattendanceapi.model.Student;
import com.tumbwe.examandclassattendanceapi.repository.AttendanceSessionRepository;
import com.tumbwe.examandclassattendanceapi.repository.CourseRepository;
import com.tumbwe.examandclassattendanceapi.service.AttendanceRecordService;
import com.tumbwe.examandclassattendanceapi.service.AttendanceSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

    Logger logger = LoggerFactory.getLogger(WebSocketController.class);
    private final AttendanceSessionService attendanceSessionService;
    private final CourseRepository courseRepository;
    private final AttendanceSessionRepository attendanceSessionRepository;
    private  final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/attendance/start-session")
    public void startAttendanceTaking(StartSession in) throws JsonProcessingException {
        try {

            logger.info(in.getCourseCode());
            Course course = courseRepository.findByCourseCode(in.getCourseCode()).orElseThrow(() -> new ResourceNotFoundException("Course not found exception"));
            AttendanceSession attendanceSession = new AttendanceSession(course, in.getType());
            attendanceSessionRepository.save(attendanceSession);
            AttendanceSessionOut attendanceSessionOut = new AttendanceSessionOut();
            if (attendanceSession.getAttendanceSessionId() == null)
                throw new RuntimeException("Failed to save session to the database");
            attendanceSessionOut.setSessionId(attendanceSession.getAttendanceSessionId() + "");

            Set<Student> students = courseRepository.findStudentsByCourseCode(course.getCourseCode());
            if (students.isEmpty()) {
                throw new ResourceNotFoundException("No Students enrolled to the course");
            }

            attendanceSessionOut.setStudents(students);


            messagingTemplate.convertAndSend("/topic/sessions/" + in.getDeviceId(), attendanceSessionOut);



        } catch (ResourceNotFoundException e) {
            messagingTemplate.convertAndSend("/topic/errors", e.getMessage());
        } catch (Exception e) {
            messagingTemplate.convertAndSend("/topic/errors", "Error saving session to database");
        }


    }


}
