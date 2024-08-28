package com.tumbwe.examandclassattendanceapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@Table(name = "Students")
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    private String studentId;
    private String fingerprintTemplate;
    private String fullName;

    

}
