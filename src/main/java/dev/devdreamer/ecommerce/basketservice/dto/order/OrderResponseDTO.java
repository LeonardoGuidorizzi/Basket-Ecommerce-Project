package dev.devdreamer.ecommerce.basketservice.dto.order;

import dev.devdreamer.ecommerce.basketservice.Enum.BasketStatus;
import dev.devdreamer.ecommerce.basketservice.Enum.OrderStatus;
import dev.devdreamer.ecommerce.basketservice.domain.order.OrderItem;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Builder
public record OrderResponseDTO (
        List<OrderItem> items,
        BigDecimal totalAmount,
        OrderStatus status,
        LocalDateTime createdAt,
        LocalDateTime updateAt
){

}
