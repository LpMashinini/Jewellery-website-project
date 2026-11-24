package com.Backend.Jewellery.dto;

import com.Backend.Jewellery.entity.Product;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        boolean inStock,
        Long categoryId,
        String categoryName
) {
    public static ProductResponse from(Product product){
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity() > 0,
                product.getCategory().getId(),
                product.getCategory().getName()
        );
    }
}
