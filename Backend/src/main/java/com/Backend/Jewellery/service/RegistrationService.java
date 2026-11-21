package com.Backend.Jewellery.service;


import com.Backend.Jewellery.dto.UserRegistrationRequest;
import com.Backend.Jewellery.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    //This service ensure the user is committed before OTP is sent
    private final UserService userService;

    public User register(UserRegistrationRequest request){

        User user = userService.registerUser(request);
        userService.initiateEmailVerification(user.getEmail());

        return user;
    }
}
