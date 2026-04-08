package com.example.dto;

import jakarta.validation.constraints.*;

public record CustomerRequest(
        @NotNull(message = "First name is required")
        @NotBlank(message = "First name cannot be blank")
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
        String firstName,

        @NotNull(message = "Last name is required")
        @NotBlank(message = "Last name cannot be blank")
        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
        String lastName,

        @NotNull(message = "Email is required")
        @NotBlank(message = "Email cannot be blank")
        //@Email(message = "Email should be valid: demo@test.com")
        @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Email should be valid: demo@test.com"
        )
        @Size(max = 100, message = "Email must be at most 100 characters")
        String email) {
}
