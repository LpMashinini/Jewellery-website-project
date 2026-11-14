package com.Backend.Jewellery.service;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.phone-number}")
    private String twilioNumber;

    @PostConstruct
    void init() {
        Twilio.init(accountSid, authToken);
    }

    @Async
    public void sendOtpSms(String phoneNumber, String otpCode, String expiryMinutes){

        String body = "Your Verification code is " + otpCode + " . It expires in " + expiryMinutes + " minutes";

        try {

            Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber(twilioNumber),
                    body
            ).create();

        } catch (ApiException e) {
            log.error("Failed to send OTP SMS to {}", phoneNumber, e);
        }
    }
}
