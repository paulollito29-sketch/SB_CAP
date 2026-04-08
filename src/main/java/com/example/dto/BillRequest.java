package com.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record BillRequest(
        @NotNull(message = "Issue date is required")
        LocalDate issueDate,

        @NotNull(message = "Total is required")
        @NotBlank(message = "Total cannot be blank")
        String status,

        @NotNull(message = "Payment method is required")
        @NotBlank(message = "Payment method cannot be blank")
        String paymentMethod,

        @NotNull(message = "Customer ID is required")
        @Min(value = 1, message = "Customer ID must be a positive number")
        Long customerId,

        List<BillDetailRequest> items
) {
}
