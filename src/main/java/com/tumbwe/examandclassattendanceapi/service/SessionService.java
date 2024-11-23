package com.tumbwe.examandclassattendanceapi.service;


import com.tumbwe.examandclassattendanceapi.dto.ActiveSession;
import com.tumbwe.examandclassattendanceapi.dto.StartSession;
import com.tumbwe.examandclassattendanceapi.model.Student;

import java.util.List;

public interface SessionService {
    List<Student> downloadStudents(String course, String attendanceType);

    ActiveSession isSessionAvailable();

    String startSession(StartSession in);
}
