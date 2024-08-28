package com.tumbwe.examandclassattendanceapi.service;


import com.tumbwe.examandclassattendanceapi.model.Student;

import java.util.List;

public interface SessionService {
    List<Student> downloadStudents(String course, String attendanceType);
}
