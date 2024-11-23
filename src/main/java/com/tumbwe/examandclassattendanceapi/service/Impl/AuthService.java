package com.tumbwe.examandclassattendanceapi.service.Impl;

import com.tumbwe.examandclassattendanceapi.config.EmailSender;
import com.tumbwe.examandclassattendanceapi.dto.UserLoginDto;
import com.tumbwe.examandclassattendanceapi.model.*;
import com.tumbwe.examandclassattendanceapi.repository.AccountRepository;
import com.tumbwe.examandclassattendanceapi.repository.DepartmentRepository;
import com.tumbwe.examandclassattendanceapi.repository.UserRepository;
import com.tumbwe.examandclassattendanceapi.repository.VerificationRepository;
import com.tumbwe.examandclassattendanceapi.request.RegisterUser;
import com.tumbwe.examandclassattendanceapi.response.RegisterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AccountRepository accountRepository;

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final EmailSender emailSender;
    private final VerificationRepository verificationRepository;
    private final VerificationService verificationService;
    private final DepartmentRepository departmentRepository;

    public RegisterResponse register(RegisterUser request){
        User user = new User();

        user.setUsername(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.valueOf(request.getRole()));
        user.setIsVerified(true);

        Department department = departmentRepository.findByName(request.getDepartment());

        if(department == null){
            throw new RuntimeException("Department is null");
        }
        user.setDepartment(department);
        Optional<User> existingUser = userRepository.findByUsername(request.getEmail());
        if (!isValidPassword(request.getPassword())){
            throw new RuntimeException("Password not strong");
        }

        if(existingUser.isPresent()){
            throw new RuntimeException("User already exists");
        }

        if(!isValidEmail(request.getEmail())){
            throw new RuntimeException("Invalid Email Address");
        }

        Account account = new Account();
        account.setFullName(request.getFullName());
        account.setPhoneNumber(request.getPhoneNumber());
        Account account1 = accountRepository.save(account);


//
//        Verification verification = new Verification();
//        verification.setUser(user);
//        verification.setExpiryTime(verificationService.getExpiration());
//        String token = verificationService.generateToken();
//
//        verification.setOtp(token);
//        verificationRepository.save(verification);

        user.setAccount(account1);
        User savedUser = userRepository.save(user);
//        emailSender.sendReservation(user,token);

        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setName(savedUser.getUsername());
        registerResponse.setId(savedUser.getId());
        return registerResponse;
    }

    public AuthenticationResponse authenticate(UserLoginDto request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()
        ));
        Optional<User> user = userRepository.findByUsername(request.getEmail());

        if(user.isPresent()){
            String token = jwtService.generateToken(user.get());
            AuthenticationResponse auth = new AuthenticationResponse();
            auth.setToken(token);
            if(user.get().getRole() == Role.ADMIN){
                auth.setDepartmentId(user.get().getDepartment().getId());
            }else if(user.get().getRole() == Role.DEAN){
                auth.setSchoolId(user.get().getDepartment().getSchool().getId());
            }
            auth.setRole(user.get().getRole().name());
            return auth;
        }
        return null;
    }

    //Check if the email is valid
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPassword(String password) {
        // Regular expression for validating the password
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.])[A-Za-z\\d@$!%*?&.]{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public AuthenticationResponse verify(String token){
        Optional<Verification> verification = verificationRepository.findByOtp(token);
        User user;
        if(verification.isPresent() && !verificationService.isTokenExpired(verification.get())){
            user = verification.get().getUser();

            user.setIsVerified(true);
            userRepository.save(user);
            verificationRepository.delete(verification.get());
            String code = jwtService.generateToken(user);
            AuthenticationResponse auth = new AuthenticationResponse();
            auth.setToken(code);
            return auth;
        }
        throw  new RuntimeException("Token expired or does not exist");
    }

    public boolean resendToken(UUID id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            Optional<Verification> verification = verificationRepository.findVerificationByUser(user.get());
            if(verification.isPresent()){
                Verification verification1 = verification.get();
                String token = verificationService.generateToken();
                verification1.setOtp(token);
                verification1.setExpiryTime(verificationService.getExpiration());
                verificationRepository.save(verification1);
                emailSender.sendReservation(user.get(),token);
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }


}
