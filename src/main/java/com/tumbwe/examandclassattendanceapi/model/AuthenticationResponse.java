package com.tumbwe.examandclassattendanceapi.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationResponse {
    private String token;
    private Long departmentId;
}
