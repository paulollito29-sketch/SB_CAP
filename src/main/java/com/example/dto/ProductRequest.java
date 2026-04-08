package com.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequest(
        @NotNull(message = "Name is required")
        @NotBlank(message = "Name cannot be blank")
        String name,

        @NotNull(message = "Description is required")
        @NotBlank(message = "Description cannot be blank")
        String description,

        @NotNull(message = "Price is required")
        @Min(value = 0, message = "Price must be non-negative")
        BigDecimal price,

        @NotNull(message = "Category ID is required")
        @Min(value = 1, message = "Category ID must be a positive number")
        Long categoryId) {
}
