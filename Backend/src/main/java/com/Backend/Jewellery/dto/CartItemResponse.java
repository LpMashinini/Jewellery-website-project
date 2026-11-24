package com.Backend.Jewellery.dto;

import com.Backend.Jewellery.entity.CartItem;
import com.Backend.Jewellery.entity.Product;

import java.math.BigDecimal;

public record CartItemResponse(
        Long productId,
        String productName,
        BigDecimal unitPrice,
        int quantity,
        BigDecimal lineTotal
) {
    static CartItemResponse from(CartItem item){

        Product product = item.getProduct();

        return new CartItemResponse(
               product.getId(),
                product.getName(),
                product.getPrice(),
                item.getQuantity(),
                product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
        );
    }
}
