package com.tumbwe.examandclassattendanceapi.controller;

import com.tumbwe.examandclassattendanceapi.model.AuthenticationResponse;
import com.tumbwe.examandclassattendanceapi.model.User;
import com.tumbwe.examandclassattendanceapi.request.RegisterUser;
import com.tumbwe.examandclassattendanceapi.response.RegisterResponse;
import com.tumbwe.examandclassattendanceapi.service.Impl.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@CrossOrigin("*")
public class AuthenticationController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUser request) {
        try {
            RegisterResponse registerResponse = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"User registration failed: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user){
        AuthenticationResponse authenticationResponse = authService.authenticate(user);
        if(Objects.equals(authenticationResponse.getToken(), "User not found")){
            return ResponseEntity.status(401).body("{\"error\": \"login failed: ");
        }
        return ResponseEntity.status(200).body(authService.authenticate(user));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("{\"message\": \"logout successfully\"}");
    }

    @PostMapping("/verify/{token}")
    public ResponseEntity<?> verify(@PathVariable String token){
        try {
            AuthenticationResponse response = authService.verify(token);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"User registration failed: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/resend/verify/{user_id}")
    public ResponseEntity<?> resendVerification(@PathVariable UUID user_id){
        boolean response = authService.resendToken(user_id);
        if(response){
            return ResponseEntity.ok("{\"message\": \"Resend token successfully\"}");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"Failed to send token, user does not exist \"}");
    }

    @GetMapping("/")
    public ResponseEntity<?> test(){
        return ResponseEntity.status(HttpStatus.CREATED).body("Hello");
    }
}
