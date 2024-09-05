package com.tumbwe.examandclassattendanceapi.service.Impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudentServiceImplTest {

    @Autowired
    StudentServiceImpl studentServiceImpl;


    @Test
    void getAllStudentsByDepartment() {
        System.out.println(studentServiceImpl.getAllStudentsByDepartment(1L));
        assertEquals(studentServiceImpl.getAllStudentsByDepartment(1L).size(), 1);
    }
}