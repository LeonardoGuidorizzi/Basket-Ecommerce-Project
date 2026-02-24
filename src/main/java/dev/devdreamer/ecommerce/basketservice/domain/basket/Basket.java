package dev.devdreamer.ecommerce.basketservice.domain.basket;


import dev.devdreamer.ecommerce.basketservice.Enum.BasketStatus;
import dev.devdreamer.ecommerce.basketservice.domain.product.Product;
import lombok.Builder;
import lombok.Data;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Document(collection = "basket")
@Data
@Builder
public class Basket {
    @Id
    private String id;
    @Indexed
    private String userId;

    private List<BasketItem> items;
    private BigDecimal totalAmount;
    private BasketStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;

    public static Basket create (String userId){
        LocalDateTime now = LocalDateTime.now();
        return Basket.builder()
                .userId(userId)
                .items(new ArrayList<>())
                .totalAmount(BigDecimal.ZERO)
                .createdAt(now)
                .updateAt(now)
                .build();
    }

    public void addItem( Product product, Integer quantity){
        if(quantity <= 0){
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        Optional<BasketItem> existing = items.stream()
                .filter(i -> i.getProductId().equals(product.getId()))
                .findFirst();
        if(existing.isPresent()){
            existing.get().increaseQuantity(quantity);
        } else {
            items.add(BasketItem.from(product, quantity));
        }
        recalculateTotal();
        touch();
    }

    private void recalculateTotal() {
        this.totalAmount = items.stream()
                .map(BasketItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void clear(){
        this.items.clear();
        touch();
    }

    private void touch(){
        this.updateAt = LocalDateTime.now();
    }

    public void updateBasketStatus(BasketStatus newStatus){
        this.status = newStatus;
        this.updateAt = LocalDateTime.now();
    }

}
