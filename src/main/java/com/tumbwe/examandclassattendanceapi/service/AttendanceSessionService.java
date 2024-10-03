package com.tumbwe.examandclassattendanceapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tumbwe.examandclassattendanceapi.dto.StartSession;
import com.tumbwe.examandclassattendanceapi.model.AttendanceSessionOut;

public interface AttendanceSessionService {



    Object getAttendaceStatuses();
}
