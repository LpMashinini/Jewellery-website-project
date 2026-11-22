package com.Backend.Jewellery.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResendCodeRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "please enter a valid email address")
    private String email;
}
