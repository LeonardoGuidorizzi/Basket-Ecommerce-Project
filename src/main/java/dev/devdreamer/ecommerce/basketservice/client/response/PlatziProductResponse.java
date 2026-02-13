package dev.devdreamer.ecommerce.basketservice.client.response;

import java.io.Serializable;
import java.math.BigDecimal;

public record PlatzProductResponse (
        Long id,
        String title,
        String slug,
        BigDecimal price,
        String description
) implements Serializable {
}