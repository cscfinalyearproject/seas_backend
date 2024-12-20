package com.tumbwe.examandclassattendanceapi.service.Impl;

import com.tumbwe.examandclassattendanceapi.exception.ResourceAlreadyExistsException;
import com.tumbwe.examandclassattendanceapi.exception.ResourceNotFoundException;
import com.tumbwe.examandclassattendanceapi.model.*;
import com.tumbwe.examandclassattendanceapi.repository.AttendanceRecordRepository;
import com.tumbwe.examandclassattendanceapi.repository.AttendanceSessionRepository;
import com.tumbwe.examandclassattendanceapi.service.AttendanceRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceRecordServiceImpl implements AttendanceRecordService {

    private final AttendanceRecordRepository attendanceRecordRepository;
    private final AttendanceSessionRepository attendanceSessionRepository;
    @Override
    public void addAttendanceRecord(AttendanceSessionOut inOut) {
        AttendanceSession attendanceSession = attendanceSessionRepository.findById(Long.parseLong(inOut.getSessionId()))
                .orElseThrow(() -> new ResourceNotFoundException("Attendance session not found"));

        if (Objects.equals(attendanceSession.getSessionStatus(), SessionStatus.closed + "")){
            System.out.println("session Closed");
            throw new ResourceAlreadyExistsException("Attendance session for Course with Course Code: " +inOut.getSessionId() + " already closed");
        }



        Set<Student> students = new HashSet<>(inOut.getStudents());

        Set<AttendanceRecord> records = students
                              .stream()
                              .map(student -> new AttendanceRecord(student, attendanceSession.getCourse(), attendanceSession.getAttendanceType(), attendanceSession)
        ).map(attendanceRecordRepository::save).collect(Collectors.toSet());
        records.forEach(record -> log.info("record: {}", record.getStudent().getStudentId()));
        attendanceSession.setSessionStatus(SessionStatus.closed.toString());
        attendanceSessionRepository.save(attendanceSession);

    }


}
