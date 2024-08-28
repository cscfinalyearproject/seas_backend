package com.tumbwe.examandclassattendanceapi.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUser {

    private String fullName;

    private String phoneNumber;

    private String biography;

    private String email;

    private String password;

    private String role;

}
