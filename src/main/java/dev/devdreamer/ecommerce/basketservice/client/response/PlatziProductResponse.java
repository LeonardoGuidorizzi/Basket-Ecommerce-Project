package dev.devdreamer.ecommerce.basketservice.client.response;

import java.math.BigDecimal;

public record PlatziProductResponse(Long id, String title, String slug, BigDecimal price, String description) {
}