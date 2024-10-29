package com.tumbwe.examandclassattendanceapi.service;

import com.tumbwe.examandclassattendanceapi.model.AttendanceSessionOut;

public interface AttendanceRecordService {


    void addAttendanceRecord(AttendanceSessionOut attendanceSessionIn);
}
