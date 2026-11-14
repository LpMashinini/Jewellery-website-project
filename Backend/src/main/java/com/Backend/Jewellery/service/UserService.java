package com.Backend.Jewellery.service;

import com.Backend.Jewellery.dto.UserRegistrationRequest;
import com.Backend.Jewellery.entity.Otp;
import com.Backend.Jewellery.entity.OtpType;
import com.Backend.Jewellery.entity.Role;
import com.Backend.Jewellery.entity.User;
import com.Backend.Jewellery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final EmailService emailService;


    @Transactional
    public User registerUser(UserRegistrationRequest request){

        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(email);
        user.setRole(Role.ROLE_CUSTOMER);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setVerified(false);


        try {
            return userRepository.saveAndFlush(user); //
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }
    }

    public void initiateEmailVerification(String email){
        userRepository.findByEmail(email.trim().toLowerCase())
                .filter(user ->  !user.getVerified())
                .ifPresent(user -> {
                    Otp otp = otpService.createOtp(user, OtpType.EMAIL_VERIFICATION);

                    emailService.sendOtpEmail(user.getEmail(), otp.getCode(), OtpService.OTP_EXPIRATION_MINUTES);
                });
    }


    @Transactional
    public boolean verifyEmailOtp(String email, String code){

        return userRepository.findByEmail(email.trim().toLowerCase())
                .map(user -> {
                    boolean valid = otpService.validateOtp(user,code, OtpType.EMAIL_VERIFICATION);

                    if (valid) {
                        user.setVerified(true);
                    }
                    return valid;
                })
                .orElse(false);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email.trim().toLowerCase()).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole().name())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.getVerified())
                .build();
    }
}
