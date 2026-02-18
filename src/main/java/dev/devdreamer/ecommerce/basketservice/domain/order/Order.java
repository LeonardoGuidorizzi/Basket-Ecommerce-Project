package dev.devdreamer.ecommerce.basketservice.domain;

import dev.devdreamer.ecommerce.basketservice.domain.Enum.BasketStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private String cartId; // Referência à cesta que originou o pedido
    private List<OrderItem> orderItems;
    private BigDecimal totalAmount;
    private LocalDateTime orderDate;
    private BasketStatus status; // Pode ser COMPLETED ou CANCELED
}
