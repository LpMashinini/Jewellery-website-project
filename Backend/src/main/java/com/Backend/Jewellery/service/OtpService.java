package com.Backend.Jewellery.service;

import com.Backend.Jewellery.entity.Otp;
import com.Backend.Jewellery.entity.OtpType;
import com.Backend.Jewellery.entity.User;
import com.Backend.Jewellery.repository.OtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OtpService {

    public static final int OTP_EXPIRATION_MINUTES = 5;

    private final OtpRepository otpRepository;
    private final SecureRandom secureRandom = new SecureRandom();


    @Transactional
    public Otp createOtp(User user, OtpType type) {
        //Invalidate any earlier unused codes of this type
        otpRepository.deleteByUser_IdAndTypeAndVerifiedIsFalse(user.getId(), type);


        LocalDateTime now  = LocalDateTime.now();

        Otp otp = new Otp();

        otp.setCode(generateOtp());
        otp.setCreatedAt(now);
        otp.setExpiresAt(now.plusMinutes(OTP_EXPIRATION_MINUTES));
        otp.setVerified(false);
        otp.setType(type);
        otp.setUser(user);

        return otpRepository.save(otp);
    }

    @Transactional
    private String generateOtp(){
        return String.valueOf(100000 + secureRandom.nextInt(900000));
    }

    public boolean validateOtp(User user, String code, OtpType type){

        return otpRepository.findByCodeAndUser_IdAndTypeAndVerifiedIsFalseAndExpiresAtAfter(
                 code, user.getId(), type, LocalDateTime.now()
        ).map(otp -> {
            otp.setVerified(true);
            return true;
        }).orElse(false);


    }


}
