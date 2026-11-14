package com.Backend.Jewellery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayStackInitializeRequest {

    private String email;
    private String amount;
    private String currency;
    private String reference;
    private String callback_url;
}
