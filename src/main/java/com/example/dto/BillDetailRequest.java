package com.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record BillDetailRequest(
        @NotNull
        @Min(value = 1, message = "Product ID must be greater than 0")
        Long productId,

        @NotNull
        @Min(value = 1, message = "Quantity must be greater than 0")
        Integer quantity) {
}
