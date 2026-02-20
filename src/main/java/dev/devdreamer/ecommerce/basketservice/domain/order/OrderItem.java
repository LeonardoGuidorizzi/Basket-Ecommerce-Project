package dev.devdreamer.ecommerce.basketservice.domain.order;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public class OrderItem {
    private Long productId;
    private String productNameAtOrder; // Nome do produto no momento do pedido
    private BigDecimal unitPriceAtOrder;// Preço do produto no momento do pedido
    private Integer quantity;
    private BigDecimal subtotalAtOrder; // Subtotal do item no momento do pedido
 }
