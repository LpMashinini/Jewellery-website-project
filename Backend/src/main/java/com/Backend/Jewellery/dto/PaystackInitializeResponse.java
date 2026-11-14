package com.Backend.Jewellery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaystackInitializeResponse {

    private Boolean status;
    private String message;
    private PaystackData data;
}
