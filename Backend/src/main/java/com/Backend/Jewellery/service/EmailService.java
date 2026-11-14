package com.Backend.Jewellery.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Async
    public void sendOtpEmail(String toEmail,String otpCode, int expiryMinutes){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(toEmail);
        message.setSubject("Your verification code");
        message.setText("Your verification code : " + otpCode + "\n expires in " + expiryMinutes);


        try {
            mailSender.send(message);
        } catch (MailException e) {
            log.error("Failed to send OTP email to {}", toEmail, e);
        }
    }
}
