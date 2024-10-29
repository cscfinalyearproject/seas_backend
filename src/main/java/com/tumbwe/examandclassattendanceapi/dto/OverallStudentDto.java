package com.tumbwe.examandclassattendanceapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OverallStudentDto {
    private String name;
    private String attendance;
}
