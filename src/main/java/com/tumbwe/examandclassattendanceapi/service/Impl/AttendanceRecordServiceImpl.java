package com.tumbwe.examandclassattendanceapi.service.Impl;

import com.tumbwe.examandclassattendanceapi.exception.InternalServerException;
import com.tumbwe.examandclassattendanceapi.exception.ResourceAlreadyExistsException;
import com.tumbwe.examandclassattendanceapi.exception.ResourceNotFoundException;
import com.tumbwe.examandclassattendanceapi.model.*;
import com.tumbwe.examandclassattendanceapi.repository.AttendanceRecordRepository;
import com.tumbwe.examandclassattendanceapi.repository.AttendanceSessionRepository;
import com.tumbwe.examandclassattendanceapi.repository.StudentRepository;
import com.tumbwe.examandclassattendanceapi.response.AttendanceRecordDTO;
import com.tumbwe.examandclassattendanceapi.service.AttendanceRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceRecordServiceImpl implements AttendanceRecordService {

    private final AttendanceRecordRepository attendanceRecordRepository;
    private final AttendanceSessionRepository attendanceSessionRepository;
    @Override
    public Set<AttendanceRecordDTO> addAttendanceRecord(AttendanceSessionInOut inOut) {
        AttendanceSession attendanceSession = attendanceSessionRepository.findById(inOut.getSession().getSessionId())
                .orElseThrow(() -> new ResourceNotFoundException("Attendance session not found"));
        if (attendanceSession.getSessionStatus() == SessionStatus.closed){
            throw new ResourceAlreadyExistsException("Attendance session for Course with Course Code: " +inOut.getSession().getCourseCode() + " already closed");
        }


        Set<AttendanceRecord> records = inOut.getStudents()
                              .stream()
                              .map(student -> new AttendanceRecord(student, attendanceSession.getCourse(), attendanceSession.getAttendanceType())
        ).map(attendanceRecordRepository::save).collect(Collectors.toSet());
        records.forEach(record -> log.info("record: {}", record.getStudent().getStudentId()));
        attendanceSession.setSessionStatus(SessionStatus.closed);
        attendanceSessionRepository.save(attendanceSession);

        return records.stream()
                .map(record -> new AttendanceRecordDTO(
                        record.getStudent().getStudentId(),
                        record.getCourse().getCourseCode(),
                        record.getAttendanceType()
                ))
                .collect(Collectors.toSet());

    }


}
