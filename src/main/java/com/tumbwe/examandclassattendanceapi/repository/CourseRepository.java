package com.tumbwe.examandclassattendanceapi.repository;

import com.tumbwe.examandclassattendanceapi.model.Course;
import com.tumbwe.examandclassattendanceapi.model.Department;
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



    @Query("SELECT c FROM Course c JOIN FETCH c.enrolledStudents WHERE c.courseCode = :courseCode")
    Optional<Course> findByCourseCodeWithStudents(@Param("courseCode") String courseCode);

    @Query("SELECT c.enrolledStudents FROM Course c WHERE c.courseCode = :courseCode")
    Set<Student> findStudentsByCourseCode(@Param("courseCode") String courseCode);

    List<Course> findAllByDepartment(Department department);

    @Query("SELECT c FROM Course c WHERE c.courseCode IN :courseCodes")
    List<Course> findAllByCourseCodes(@Param("courseCodes") List<String> courseCodes);

    @Query("SELECT distinct(c.enrolledStudents) FROM Course c WHERE c.courseCode = :courseCode")
    List<Student> findStudentsByCourse(@Param("courseCode") String courseCode);

    List<Course> findCourseByDepartment(Department department);

    @Query(value = "SELECT DISTINCT c.course_code, c.course_name FROM courses c ORDER BY c.course_name", nativeQuery = true)
    List<Object[]> findDistinctCourses();

    Course findCourseByEnrolledStudents(Set<Student> enrolledStudents);

}

