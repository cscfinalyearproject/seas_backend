package com.tumbwe.examandclassattendanceapi.controller;

import com.tumbwe.examandclassattendanceapi.dto.DepartmentDto;
import com.tumbwe.examandclassattendanceapi.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/department")
@CrossOrigin("*")
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping("/")
    public ResponseEntity<?> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @PostMapping("/")
    public ResponseEntity<?> addDepartment(@RequestBody DepartmentDto department) {
        return ResponseEntity.ok(departmentService.addDepartment(department));
    }

    @PutMapping("/")
    public ResponseEntity<?> updateDepartment(@RequestBody DepartmentDto department) {
        return ResponseEntity.ok(departmentService.updateDepartment(department));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable long id) {
        return ResponseEntity.ok(departmentService.deleteDepartment(id));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getDepartmentById(@PathVariable long id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }
}
