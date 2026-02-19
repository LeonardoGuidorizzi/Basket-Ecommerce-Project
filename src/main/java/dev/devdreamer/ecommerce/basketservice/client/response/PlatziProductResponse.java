package dev.devdreamer.ecommerce.basketservice.client.response;

import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
public record PlatziProductResponse(
        Long id,
        String title,
        String slug,
        BigDecimal price,
        String description
) implements Serializable {
}