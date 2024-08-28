package com.tumbwe.examandclassattendanceapi.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AuthenticationResponse {
    private final String token;
}
