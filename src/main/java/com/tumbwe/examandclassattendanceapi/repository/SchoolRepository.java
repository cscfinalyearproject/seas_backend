package com.tumbwe.examandclassattendanceapi.repository;


import com.tumbwe.examandclassattendanceapi.model.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {
}
