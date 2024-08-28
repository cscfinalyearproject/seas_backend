package com.tumbwe.examandclassattendanceapi.repository;

import com.tumbwe.examandclassattendanceapi.model.AttendanceRecord;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

@Registered
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, UUID> {
}
