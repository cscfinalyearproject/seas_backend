package com.tumbwe.examandclassattendanceapi.service;

import com.tumbwe.examandclassattendanceapi.model.AttendanceSessionInOut;
import com.tumbwe.examandclassattendanceapi.model.AttendanceType;
import com.tumbwe.examandclassattendanceapi.model.Student;
import com.tumbwe.examandclassattendanceapi.response.AttendanceRecordDTO;

import java.util.Set;

public interface AttendanceRecordService {


    Object addAttendanceRecord(AttendanceSessionInOut attendanceSessionIn);
}
