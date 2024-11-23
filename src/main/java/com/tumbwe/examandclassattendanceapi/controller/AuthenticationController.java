package com.tumbwe.examandclassattendanceapi.controller;

import com.tumbwe.examandclassattendanceapi.dto.UserLoginDto;
import com.tumbwe.examandclassattendanceapi.dto.UserResponseDto;
import com.tumbwe.examandclassattendanceapi.model.AuthenticationResponse;
import com.tumbwe.examandclassattendanceapi.model.User;
import com.tumbwe.examandclassattendanceapi.request.RegisterUser;
import com.tumbwe.examandclassattendanceapi.response.RegisterResponse;
import com.tumbwe.examandclassattendanceapi.service.Impl.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
            return ResponseEntity.ok(registerResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"User registration failed: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto user){
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
    public ResponseEntity<?> resendVerification(@PathVariable Long user_id){
        boolean response = authService.resendToken(user_id);
        if(response){
            return ResponseEntity.ok("{\"message\": \"Resend token successfully\"}");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"Failed to send token, user does not exist \"}");
    }

    @GetMapping(value = "/getAllUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUsers() {
        try {
            return ResponseEntity.ok(authService.getUsers());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"Failed to get users: " + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            if(authService.deleteUser(id)){
                return ResponseEntity.ok("Deleted user successfully");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not exist");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"Delete failed: " + e.getMessage() + "\"}");
        }
    }

    @PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserResponseDto user) {
        try {
            UserResponseDto userResponseDto = authService.updateUser(id,user);
            if(userResponseDto.equals(user)){
                return ResponseEntity.ok(userResponseDto);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not exist");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"Update failed: " + e.getMessage() + "\"}");
        }
    }



    @GetMapping("/")
    public ResponseEntity<?> test(){
        return ResponseEntity.status(HttpStatus.CREATED).body("Hello");
    }
}
