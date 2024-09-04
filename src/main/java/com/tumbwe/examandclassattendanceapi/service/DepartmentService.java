package com.tumbwe.examandclassattendanceapi.service;

import com.tumbwe.examandclassattendanceapi.dto.DepartmentDto;
import com.tumbwe.examandclassattendanceapi.model.Department;
import com.tumbwe.examandclassattendanceapi.model.School;

import java.util.List;

public interface DepartmentService {
    public List<DepartmentDto> getAllDepartments();
    public Department getDepartmentById(long id);
    public boolean addDepartment(DepartmentDto departmentDto);
    public boolean updateDepartment(DepartmentDto departmentDto);
    public boolean deleteDepartment(long id);
}
