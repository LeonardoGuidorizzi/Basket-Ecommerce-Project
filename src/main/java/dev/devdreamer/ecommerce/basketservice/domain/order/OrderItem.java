package dev.devdreamer.ecommerce.basketservice.domain.order;

import dev.devdreamer.ecommerce.basketservice.domain.basket.BasketItem;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
@Data
@Builder
public class OrderItem implements Serializable  {
    private Long productId;
    private String productNameAtOrder; // Nome do produto no momento do pedido
    private BigDecimal unitPriceAtOrder;// Preço do produto no momento do pedido
    private Integer quantity;
    private BigDecimal subtotalAtOrder; // Subtotal do item no momento do pedido

    public static OrderItem fromBasketItem(BasketItem item) {
        return OrderItem.builder()
                .productId(item.getProductId())
                .productNameAtOrder(item.getProductName())
                .quantity(item.getQuantity())
                .subtotalAtOrder(item.getSubtotal())
                .unitPriceAtOrder(item.getUnitPrice())
                .build();
    }
 }
