package dev.devdreamer.ecommerce.basketservice.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RegisterRequestDTO(
        @NotBlank(message = "Name is required.")
        String name,
        @NotBlank(message = "Email address is required.")
        String email,
        @NotBlank(message = "Password is required.")
        String password) {
}
