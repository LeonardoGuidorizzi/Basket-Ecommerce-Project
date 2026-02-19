package dev.devdreamer.ecommerce.basketservice.domain.order;

import dev.devdreamer.ecommerce.basketservice.Enum.BasketStatus;
import dev.devdreamer.ecommerce.basketservice.Enum.OrderStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    @Indexed
    private String userId;
    private String basketId;
    private List<OrderStatus> orderItems;
    private BigDecimal totalAmount;
    private LocalDateTime orderDate;
    private BasketStatus status; // Pode ser COMPLETED ou CANCELED
}
