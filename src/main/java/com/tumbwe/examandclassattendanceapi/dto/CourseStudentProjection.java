package com.tumbwe.examandclassattendanceapi.dto;

public interface CourseStudentProjection {
    String getCourseCode();
    String getCourseName();
    Long getStudentId();
    String getFullName();
    String getIntake();
    String getYearOfStudy();
}
