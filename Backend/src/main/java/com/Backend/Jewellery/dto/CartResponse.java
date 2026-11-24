package com.Backend.Jewellery.dto;

import com.Backend.Jewellery.entity.Cart;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

public record CartResponse(
        List<CartItemResponse> items,
        BigDecimal total,
        int totalItems
) {
    public static CartResponse empty(){
        return new CartResponse(List.of(), BigDecimal.ZERO, 0);
    }

    public static CartResponse from(Cart cart){
        List<CartItemResponse> items = cart.getItems().stream()
                .map(CartItemResponse::from)
                .sorted(Comparator.comparing(CartItemResponse::productName)
                        .thenComparing(CartItemResponse::productId)
                ).toList();

        BigDecimal total = items.stream()
                .map(CartItemResponse::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalItems = items.stream()
                .mapToInt(CartItemResponse::quantity)
                .sum();

        return new CartResponse(items, total,totalItems);
    }
}
