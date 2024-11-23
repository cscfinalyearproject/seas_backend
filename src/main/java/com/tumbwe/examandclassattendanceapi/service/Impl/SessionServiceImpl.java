package com.tumbwe.examandclassattendanceapi.service.Impl;

import com.tumbwe.examandclassattendanceapi.dto.ActiveSession;
import com.tumbwe.examandclassattendanceapi.dto.StartSession;
import com.tumbwe.examandclassattendanceapi.exception.ResourceNotFoundException;
import com.tumbwe.examandclassattendanceapi.model.*;
import com.tumbwe.examandclassattendanceapi.repository.AttendanceSessionRepository;
import com.tumbwe.examandclassattendanceapi.repository.CourseRepository;
import com.tumbwe.examandclassattendanceapi.repository.StudentRepository;
import com.tumbwe.examandclassattendanceapi.service.AttendanceSessionService;
import com.tumbwe.examandclassattendanceapi.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final CourseRepository courseRepository;
    private final AttendanceSessionRepository attendanceSessionRepository;

    @Override
    public List<Student> downloadStudents(String courseCode, String attendanceType) {
        List<Student> eligibleStudent = new ArrayList<>();
        List<Student> allStudents = courseRepository.findStudentsByCourse(courseCode);
        for(Student student :allStudents){
            if(checkStudentStatus(student.getStudentId(),courseCode,attendanceType)){
                eligibleStudent.add(student);
            }
        }
        return eligibleStudent;
    }

    @Override
    public ActiveSession isSessionAvailable() {
        Optional<AttendanceSession> openSession = attendanceSessionRepository.findOpenSession(SessionStatus.open.toString());
        if (openSession.isPresent()) {
            // An open session exists
            AttendanceSession attendanceSession = openSession.get();
          return new ActiveSession(
                    true, attendanceSession.getAttendanceSessionId() + "" , attendanceSession.getCourse().getCourseCode(),  attendanceSession.getAttendanceType()
            );

        } else {
            return new ActiveSession(false, "", "", "");

        }


    }

    private boolean isSessionAlreadyStarted(String courseCode, String attendanceType) {
        LocalDate today = LocalDate.now(); // Current date
        AttendanceSession openSession = attendanceSessionRepository.findOpenSession(courseCode, attendanceType, today);
        return openSession != null; // Return true if a session exists
    }
    @Override
    public String startSession(StartSession in) {

        if (isSessionAlreadyStarted(in.getCourseCode(), in.getType())) {
            throw new IllegalStateException("A session for this course and attendance type is already open today!");
        }

        Course course = courseRepository.findByCourseCode(in.getCourseCode()).orElseThrow(() -> new ResourceNotFoundException("Course not found exception"));
        AttendanceSession attendanceSession = new AttendanceSession(course, in.getType());
        attendanceSessionRepository.save(attendanceSession);

        if (attendanceSession.getAttendanceSessionId() == null)
            throw new RuntimeException("Failed to save session to the database");

        return "Session Started Successfully";
    }

    private boolean checkStudentStatus(String studentId, String courseCode, String attendanceType){
        String externalAPIUrl = "http://sis.unza.com/api/v1/student/"+studentId;

        List<Course> courseList= new ArrayList<>();
        Course course1 = new Course();
        course1.setCourseCode("CSC211");
        course1.setCourseName("Systems");
        courseList.add(course1);
        StudentTest studentTest = new StudentTest();
        studentTest.setStatus(true);
        studentTest.setStudentId("2020778764");
        studentTest.setCourseList(courseList);

        if (studentTest.isStatus() && !Objects.equals(attendanceType, "EXAM")){
            return true;
        } else if (studentTest.isStatus() && Objects.equals(attendanceType, "EXAM")) {
            for (Course course2: courseList){
                if(Objects.equals(course2.getCourseCode(), courseCode)){
                    return true;
                }
            }
        }
        return false;
    }

}
