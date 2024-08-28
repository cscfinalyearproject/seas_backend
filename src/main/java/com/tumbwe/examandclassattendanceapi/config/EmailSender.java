package com.tumbwe.examandclassattendanceapi.config;


import com.tumbwe.examandclassattendanceapi.model.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service
public class EmailSender {


    private final JavaMailSender javaMailSender;


    public EmailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendReservation(User user, String code){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getUsername());
        mailMessage.setSubject("Verification Code");
        mailMessage.setText(
                "Your verification code is "+ code + ", it will expire in 15 minutes"
        );
        mailMessage.setSentDate(new Date());
        javaMailSender.send(mailMessage);
    }

}
