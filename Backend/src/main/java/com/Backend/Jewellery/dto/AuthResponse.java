package com.Backend.Jewellery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private Long userId;
    private String name;
    private String email;
    private Boolean verified;

    public AuthResponse(String token, Long id, String name, String email) {
    }
}
