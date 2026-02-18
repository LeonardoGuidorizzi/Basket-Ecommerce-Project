package dev.devdreamer.ecommerce.basketservice.domain;


import dev.devdreamer.ecommerce.basketservice.domain.Enum.BasketStatus;
import dev.devdreamer.ecommerce.basketservice.domain.basket.BasketItem;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "basket")
public class Basket {
    @Id
    private Long id;
    private List<BasketItem> itens;
    private BasketStatus status = BasketStatus.OPEN;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;

}
