package com.Backend.Jewellery.dto;

import lombok.Data;

@Data
public class PaystackVerifyData {

    private Long id;
    private String status;
    private String reference;
    private String amount;
    private String currrency;
    private String paid_at;
}
