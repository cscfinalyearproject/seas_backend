package com.tumbwe.examandclassattendanceapi.service;

import com.tumbwe.examandclassattendanceapi.model.School;

import java.util.List;

public interface SchoolService {
    public List<School> getAllSchools();
    public School getSchoolById(long id);
    public boolean addSchool(School school);
    public boolean updateSchool(School school);
    public boolean deleteSchool(long id);
}
