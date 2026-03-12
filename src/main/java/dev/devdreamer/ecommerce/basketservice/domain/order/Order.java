package dev.devdreamer.ecommerce.basketservice.domain.order;

import dev.devdreamer.ecommerce.basketservice.Enum.BasketStatus;
import dev.devdreamer.ecommerce.basketservice.Enum.OrderStatus;
import dev.devdreamer.ecommerce.basketservice.domain.basket.Basket;
import dev.devdreamer.ecommerce.basketservice.domain.basket.BasketItem;
import dev.devdreamer.ecommerce.basketservice.exception.custom.BusinessException;
import lombok.Builder;
import lombok.Data;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "order")
@Builder
@Data
public class Order {
    @Id
    private String id;
    @Indexed
    private String userId;
    private String basketId;
    private List<OrderItem> items;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt; // Pode ser COMPLETED ou CANCELED



    public static Order from (Basket basket){
        if(basket.getItems().isEmpty()){
            throw new BusinessException("Cannot create order from empty basket");
        }
        LocalDateTime now = LocalDateTime.now();

        List<OrderItem> orderItems = basket.getItems().stream()
                .map(OrderItem::fromBasketItem)
                .toList();

        return Order.builder()
                .userId(basket.getUserId())
                .basketId(basket.getId())
                .items(orderItems)
                .totalAmount(basket.getTotalAmount())
                .status(OrderStatus.PENDING)
                .updateAt(now)
                .createdAt(now)
                .build();

    }


    public void updateBasketStatus(OrderStatus newStatus){
        this.status = newStatus;
        this.updateAt = LocalDateTime.now();
    }
}


//public class EntityA {
//    private String field1;
//    private int field2;
//    // Getters and setters
//}
//
//public class EntityB {
//    private String field1;
//    private int field2;
//    // Getters and setters
//}
//
//// In a service or utility class:
//public EntityB convertAToB(EntityA entityA) {
//    EntityB entityB = new EntityB();
//    entityB.setField1(entityA.getField1());
//    entityB.setField2(entityA.getField2());
//    return entityB;
//}