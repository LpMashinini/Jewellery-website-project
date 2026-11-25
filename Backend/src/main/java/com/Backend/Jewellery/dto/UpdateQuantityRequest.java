package com.Backend.Jewellery.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateQuantityRequest {

    @Min(value = 1, message = "quantity must be at least 1")
    @Max(value = 99, message = "Quantity cannot be more than 99")
    private int quntity;
}
