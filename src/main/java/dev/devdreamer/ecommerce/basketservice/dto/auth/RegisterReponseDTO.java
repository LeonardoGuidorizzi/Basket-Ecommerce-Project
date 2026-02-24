package dev.devdreamer.ecommerce.basketservice.dto.auth;

import lombok.Builder;

@Builder
public record RegisterReponseDTO(Long id, String name, String email) {
}
