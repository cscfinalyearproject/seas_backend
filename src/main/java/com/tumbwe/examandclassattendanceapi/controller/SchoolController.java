package com.tumbwe.examandclassattendanceapi.controller;


import com.tumbwe.examandclassattendanceapi.model.School;
import com.tumbwe.examandclassattendanceapi.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/school")
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping("/")
    public ResponseEntity<?> getAllSchools() {
       return ResponseEntity.ok(schoolService.getAllSchools());
    }

    @PostMapping("/")
    public ResponseEntity<?> addSchool(@RequestBody School school) {
        return ResponseEntity.ok(schoolService.addSchool(school));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSchool(@PathVariable long id) {
        return ResponseEntity.ok(schoolService.deleteSchool(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSchool(@PathVariable long id, @RequestBody School school) {
        school.setId(id);
        return ResponseEntity.ok(schoolService.updateSchool(school));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getSchoolById(@PathVariable long id) {
        return ResponseEntity.ok(schoolService.getSchoolById(id));
    }

}
