package com.testing.expense_tracker.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "User required")
        String username,
        @NotBlank(message = "Email required")
        @Size(max = 255, message = "Email must be less than 256 characters")
        String email,
        @NotBlank(message = "Password required")
        String password
) {
}
