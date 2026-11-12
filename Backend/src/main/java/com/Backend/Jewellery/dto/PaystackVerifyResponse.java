package com.Backend.Jewellery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaystackVerifyResponse {

    private boolean status;
    private String message;
    private PaystackVerifyData Data;
}
