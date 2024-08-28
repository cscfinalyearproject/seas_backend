package com.tumbwe.examandclassattendanceapi.service.Impl;

import com.tumbwe.examandclassattendanceapi.model.Course;
import com.tumbwe.examandclassattendanceapi.model.Student;
import com.tumbwe.examandclassattendanceapi.model.StudentTest;
import com.tumbwe.examandclassattendanceapi.repository.CourseRepository;
import com.tumbwe.examandclassattendanceapi.repository.StudentRepository;
import com.tumbwe.examandclassattendanceapi.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final CourseRepository courseRepository;
    private final WebClient.Builder webClientBuilder;

    @Override
    public List<Student> downloadStudents(String courseCode, String attendanceType) {
        List<Student> eligibleStudent = new ArrayList<>();
        Set<Student> allStudents = courseRepository.findStudentsByCourseCode(courseCode);
        for(Student student :allStudents){
            if(checkStudentStatus(student.getStudentId(),courseCode,attendanceType)){
                eligibleStudent.add(student);
            }
        }
        return eligibleStudent;
    }

    private boolean checkStudentStatus(String studentId, String courseCode, String attendanceType){
        String externalAPIUrl = "http://sis.unza.com/api/v1/student/"+studentId;

        List<Course> courseList= new ArrayList<>();
        Course course1 = new Course();
        course1.setCourseCode("CSC211");
        course1.setCourseName("Systems");
        courseList.add(course1);
        StudentTest studentTest = new StudentTest();
        studentTest.setStatus(true);
        studentTest.setStudentId("2020778764");
        studentTest.setCourseList(courseList);

        if (studentTest.isStatus() && !Objects.equals(attendanceType, "EXAM")){
            return true;
        } else if (studentTest.isStatus() && Objects.equals(attendanceType, "EXAM")) {
            for (Course course2: courseList){
                if(Objects.equals(course2.getCourseCode(), courseCode)){
                    return true;
                }
            }
        }
        return false;
    }

}
