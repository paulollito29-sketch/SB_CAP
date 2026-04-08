package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryRequest (
        @NotNull(message = "Name is required in body")
        @NotBlank(message = "Name cannot be blank")
        @Size(min = 3, max = 5, message = "Name must be between 3 and 5 characters")
        String name,

        @NotNull(message = "Description is required in body")
        @NotBlank(message = "Description cannot be blank")
        @Size(min = 5, max = 30, message = "Name must be between 5 and 30 characters")
        String description) {
}
