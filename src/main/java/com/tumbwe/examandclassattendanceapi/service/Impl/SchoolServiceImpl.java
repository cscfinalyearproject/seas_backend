package com.tumbwe.examandclassattendanceapi.service.Impl;

import com.tumbwe.examandclassattendanceapi.model.School;
import com.tumbwe.examandclassattendanceapi.repository.SchoolRepository;
import com.tumbwe.examandclassattendanceapi.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;

    @Override
    public List<School> getAllSchools() {
        return schoolRepository.findAll();
    }

    @Override
    public School getSchoolById(long id) {
        return schoolRepository.findById(id).orElse(null);
    }

    @Override
    public boolean addSchool(School school) {
        School school1 = schoolRepository.save(school);
        return school1.getId() != null;
    }

    @Override
    public boolean updateSchool(School school) {
        School school1 = schoolRepository.findById(school.getId()).orElse(null);
        if(school1 != null) {
            school1.setName(school.getName());
        }else {
            return false;
        }
        School updated = schoolRepository.save(school1);
        return school.getName().equals(updated.getName());
    }

    @Override
    public boolean deleteSchool(long id) {
        School school = schoolRepository.findById(id).orElse(null);
        if(school != null) {
            schoolRepository.delete(school);
            return true;
        }
        return false;
    }
}
