package dev.devdreamer.ecommerce.basketservice.domain.basket;

import dev.devdreamer.ecommerce.basketservice.domain.product.Product;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public class BasketItem {
    private Long productId;
    private String productName;
    private BigDecimal unitPrice;
    private BigDecimal subTotal;
    private Integer quantity;

    public static BasketItem from (Product product, Integer quantity){
        return BasketItem.builder()
                .productId(product.getId())
                .productName(product.getName())
                .unitPrice(product.getUnitPrice())
                .quantity(quantity)
                .build();
    }

    public BigDecimal getSubtotal (){
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public void increaseQuantity(Integer amount) {
        this.quantity += amount;
    }
}
