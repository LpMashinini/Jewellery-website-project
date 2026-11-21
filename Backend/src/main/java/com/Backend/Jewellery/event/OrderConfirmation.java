package com.Backend.Jewellery.event;

import javax.sound.sampled.Line;
import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String toEmail,
        String customerName,
        Long orderId,
        List<Line> lines,
        BigDecimal total

) {
    public record Line(String productName, int quantity, BigDecimal unitPrice){}
}
