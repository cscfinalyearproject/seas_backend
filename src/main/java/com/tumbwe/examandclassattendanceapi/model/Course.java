package com.tumbwe.examandclassattendanceapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "courses")
public class Course {
    @Id
    private String courseCode;
    private String courseName;

    @ManyToMany
    @JoinTable(name = "course_student",
            joinColumns = @JoinColumn(name = "course_code"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private Set<Student> enrolledStudents = new HashSet<>();

    public Course(String courseName, String courseCode) {
    }
}
