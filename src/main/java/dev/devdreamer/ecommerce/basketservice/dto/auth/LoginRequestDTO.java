package dev.devdreamer.ecommerce.basketservice.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "Email address is required.")
        String email,
        @NotBlank(message = "Password is required.")
        String password) {
}