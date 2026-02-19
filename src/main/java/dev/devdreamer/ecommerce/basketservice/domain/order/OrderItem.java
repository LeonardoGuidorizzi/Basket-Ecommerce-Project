package dev.devdreamer.ecommerce.basketservice.domain.order;

import java.math.BigDecimal;

public class OrderItem {
    private Long productId;
    private Integer quantity;
    private BigDecimal unitPriceAtOrder;// Preço do produto no momento do pedido
    private String productNameAtOrder; // Nome do produto no momento do pedido
    private BigDecimal subtotalAtOrder; // Subtotal do item no momento do pedido
 }
