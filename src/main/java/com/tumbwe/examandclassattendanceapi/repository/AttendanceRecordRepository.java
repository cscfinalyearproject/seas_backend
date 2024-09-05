package com.tumbwe.examandclassattendanceapi.repository;

import com.tumbwe.examandclassattendanceapi.model.AttendanceRecord;
import com.tumbwe.examandclassattendanceapi.model.Course;
import com.tumbwe.examandclassattendanceapi.model.Student;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

@Registered
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, UUID> {
//    public AttendanceRecord findAllByStudentOrderByStudent(UUID studentGroup);
    public List<AttendanceRecord> findAllByCourseOrderByStudent(Course course);
    public List<AttendanceRecord> findAllByStudentOrderByStudent(Student student);
    public List<AttendanceRecord> findAllByStudentAndCourseOrderByStudent(Student student, Course course);

}
