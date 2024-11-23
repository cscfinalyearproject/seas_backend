package com.tumbwe.examandclassattendanceapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Valid
public class StartSession {

    @NotBlank(message = "attendance type is required")
    private String type;
    @NotBlank(message = "Course Code is required")
    private String courseCode;
    @NotBlank(message = "ESP32 Identifier should be specified")
    private String deviceId;
}
