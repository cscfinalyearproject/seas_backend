package com.tumbwe.examandclassattendanceapi.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RegisterResponse {
    private UUID id;
    private String name;
}
