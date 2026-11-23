package com.Backend.Jewellery.service;

import com.Backend.Jewellery.dto.AuthRequest;
import com.Backend.Jewellery.dto.AuthResponse;
import com.Backend.Jewellery.entity.User;
import com.Backend.Jewellery.jwtAuth.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthResponse login(AuthRequest request){

        String email = request.getEmail().trim().toLowerCase();

        Authentication authentication;

        try{

            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email,
                            request.getPassword()
                    )
            );

        } catch (BadCredentialsException | DisabledException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect email or password. just registered ? verify your email first");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);
        User user = userService.getUserByEmail(email);

        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), user.getVerified());

    }
}
