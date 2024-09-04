package com.tumbwe.examandclassattendanceapi.service.Impl;

import com.tumbwe.examandclassattendanceapi.dto.DepartmentDto;
import com.tumbwe.examandclassattendanceapi.model.Department;
import com.tumbwe.examandclassattendanceapi.model.School;
import com.tumbwe.examandclassattendanceapi.repository.DepartmentRepository;
import com.tumbwe.examandclassattendanceapi.service.DepartmentService;
import com.tumbwe.examandclassattendanceapi.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final SchoolService schoolService;

    @Override
    public List<DepartmentDto> getAllDepartments() {

        List<Department> departments = departmentRepository.findAll();
        List<DepartmentDto> departmentDtos = new ArrayList<>();
        for (Department department : departments) {
            DepartmentDto departmentDto = new DepartmentDto();
            departmentDto.setId(department.getId());
            departmentDto.setName(department.getName());
            departmentDto.setSchoolId(department.getSchool().getId());
            departmentDtos.add(departmentDto);
        }
        return departmentDtos;
    }

    @Override
    public Department getDepartmentById(long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    @Override
    public boolean addDepartment(DepartmentDto departmentDto) {
        Department department = new Department();
        department.setName(departmentDto.getName());
        School school = schoolService.getSchoolById(departmentDto.getSchoolId());
        department.setSchool(school);
        Department dept = departmentRepository.save(department);
        return dept.getId() != null;
    }

    @Override
    public boolean updateDepartment(DepartmentDto departmentDto) {
        Department department = new Department();
        department.setName(departmentDto.getName());
        School school = schoolService.getSchoolById(departmentDto.getSchoolId());
        department.setSchool(school);
        Department dept = departmentRepository.save(department);

        return Objects.equals(dept.getName(), departmentDto.getName());
    }

    @Override
    public boolean deleteDepartment(long id) {
        Department department = departmentRepository.findById(id).orElse(null);
        if (department != null) {
            departmentRepository.delete(department);
            return true;
        }
        return false;
    }
}
