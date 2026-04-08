package com.example.dto;

import java.math.BigDecimal;

public record BillDetailResponse(
        Long id,
        String productName,
        Integer quantity
) {
}
