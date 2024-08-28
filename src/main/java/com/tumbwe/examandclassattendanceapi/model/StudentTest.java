package com.tumbwe.examandclassattendanceapi.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StudentTest {
    String studentId;
    boolean status;
    List<Course> courseList;
}
