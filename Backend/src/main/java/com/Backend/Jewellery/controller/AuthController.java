package com.Backend.Jewellery.controller;

import com.Backend.Jewellery.dto.RegisterResponse;
import com.Backend.Jewellery.dto.UserRegistrationRequest;
import com.Backend.Jewellery.entity.User;
import com.Backend.Jewellery.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegistrationService registrationService;

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
}
