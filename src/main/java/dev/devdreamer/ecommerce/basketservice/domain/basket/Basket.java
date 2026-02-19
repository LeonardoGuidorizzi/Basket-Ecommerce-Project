package dev.devdreamer.ecommerce.basketservice.domain.basket;


import dev.devdreamer.ecommerce.basketservice.Enum.BasketStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "basket")
public class Basket {
    @Id
    private String id;
    @Indexed
    private String userId;

    private List<BasketItem> items = new ArrayList<>();

    private BigDecimal totalAmount = BigDecimal.ZERO;

    private BasketStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;

}
