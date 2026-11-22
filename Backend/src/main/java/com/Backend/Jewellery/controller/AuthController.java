package com.Backend.Jewellery.controller;

import com.Backend.Jewellery.dto.*;
import com.Backend.Jewellery.entity.User;
import com.Backend.Jewellery.service.AuthService;
import com.Backend.Jewellery.service.RegistrationService;
import com.Backend.Jewellery.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegistrationService registrationService;
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public RegisterResponse register(@Valid @RequestBody UserRegistrationRequest request){

        User user = registrationService.register(request);

        return new RegisterResponse(
                user.getId(),
                user.getName(),
                user.getVerified(),
                "Registration successful. Please check your email for verification code."
        );

    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest request){
        return authService.login(request);
    }

    @PostMapping("/verify-email")
    public MessageResponse verifyEmail(@Valid @RequestBody EmailVerificationRequest request){

        boolean verified = userService.verifyEmailOtp(request.getEmail(), request.getCode());

        if (!verified){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid or expired verification code");
        }

        return new MessageResponse("Email verified. you can log in.");
    }
}
