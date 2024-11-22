package com.tumbwe.examandclassattendanceapi.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserLoginDto {
    String email;
    String password;
}
