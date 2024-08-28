package com.tumbwe.examandclassattendanceapi.service.Impl;

import com.tumbwe.examandclassattendanceapi.dto.StartSession;
import com.tumbwe.examandclassattendanceapi.exception.ResourceNotFoundException;
import com.tumbwe.examandclassattendanceapi.model.*;
import com.tumbwe.examandclassattendanceapi.repository.AttendanceSessionRepository;
import com.tumbwe.examandclassattendanceapi.repository.CourseRepository;
import com.tumbwe.examandclassattendanceapi.service.AttendanceSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceSessionServiceImpl implements AttendanceSessionService {

    private final AttendanceSessionRepository attendanceSessionRepository;
    private final CourseRepository courseRepository;
    @Override
    public AttendanceSessionInOut startSession(StartSession in) {
        Course course = courseRepository.findByCourseCode(in.getCourseCode()).orElseThrow(() -> new ResourceNotFoundException("Course not found exception"));
        AttendanceSession attendanceSession = new AttendanceSession(course, in.getType());
        attendanceSessionRepository.save(attendanceSession);
        in.setSessionId(attendanceSession.getAttendanceSessionId());


        Set<Student> students = courseRepository.findStudentsByCourseCode(course.getCourseCode());
        if (!students.isEmpty()){
            return new AttendanceSessionInOut(in, students);
        }
        else {
            throw new ResourceNotFoundException("No Students enrolled to the course");
        }

        }


}

