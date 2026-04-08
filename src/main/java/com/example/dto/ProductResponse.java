package com.example.dto;

import java.math.BigDecimal;

public record ProductResponse(
        Long productId,
        String name,
        String description,
        BigDecimal price,
        String categoryName) {
}
