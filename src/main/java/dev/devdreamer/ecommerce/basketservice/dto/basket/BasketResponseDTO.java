package dev.devdreamer.ecommerce.basketservice.dto.basket;

import dev.devdreamer.ecommerce.basketservice.Enum.BasketStatus;
import dev.devdreamer.ecommerce.basketservice.domain.basket.BasketItem;
import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record BasketResponseDTO  (
                List<BasketItem> items,
                BigDecimal totalAmount,
                BasketStatus status,
                LocalDateTime createdAt,
                LocalDateTime updateAt
)implements Serializable {
}
