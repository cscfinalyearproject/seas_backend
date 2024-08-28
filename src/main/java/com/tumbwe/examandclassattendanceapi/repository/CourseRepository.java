package com.tumbwe.examandclassattendanceapi.repository;

import com.tumbwe.examandclassattendanceapi.model.Course;
import com.tumbwe.examandclassattendanceapi.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    Optional<Course> findByCourseCode(String courseCode);

    @Query("SELECT c.enrolledStudents FROM Course c WHERE c.courseCode = :courseCode")
    Set<Student> findStudentsByCourseCode(@Param("courseCode") String courseCode);


}

