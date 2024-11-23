package com.tumbwe.examandclassattendanceapi.dto;

import com.tumbwe.examandclassattendanceapi.model.Department;
import com.tumbwe.examandclassattendanceapi.model.Role;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String email;
    private String department;
    private String role;
    private String fullName;
}
