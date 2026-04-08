package com.example.dto;

import java.math.BigDecimal;

public record BillItemResponse(
        Long id,
        Long productId,
        String productName,
        Integer quantity,
        BigDecimal price,
        BigDecimal subTotal
) {
}
