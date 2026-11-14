package com.Backend.Jewellery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaystackData {

    private String authorization_url;
    private String access_code;
    private String reference;
}
