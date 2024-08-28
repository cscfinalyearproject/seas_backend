package com.tumbwe.examandclassattendanceapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class AccountController {
    @GetMapping("/hello")
    public ResponseEntity<String> index(){
        return ResponseEntity.ok("Hello World");
    }
}
