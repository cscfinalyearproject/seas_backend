package com.tumbwe.examandclassattendanceapi.repository;

import com.tumbwe.examandclassattendanceapi.model.Department;
import com.tumbwe.examandclassattendanceapi.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    public List<Student> findAllByDepartment(Department department);
    public Student findByStudentId(String studentId);
}
