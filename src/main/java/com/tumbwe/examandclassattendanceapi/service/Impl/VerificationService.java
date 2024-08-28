package com.tumbwe.examandclassattendanceapi.service.Impl;

import com.tumbwe.examandclassattendanceapi.model.Verification;
import com.tumbwe.examandclassattendanceapi.repository.VerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final VerificationRepository verificationRepository;


    public String generateToken() {
        // Generate a random 4-digit number
        Random random = new Random();
        int token1 = random.nextInt(9000) + 1000;
        String token = String.valueOf(token1);// Generates a random number between 1000 and 9999
        Optional<Verification> verification = verificationRepository.findByOtp(token);

        if(verification.isPresent()){
            generateToken();
        }
        return token;
    }

    public Instant getExpiration() {
        return Instant.now().plus(Duration.ofMinutes(15));
    }

    public boolean isTokenExpired(Verification verification) {
        Instant expirationTime = verification.getExpiryTime();
        return Instant.now().isAfter(expirationTime);
    }


}
