package dev.devdreamer.ecommerce.basketservice.domain.product;

import lombok.*;

import org.springframework.data.annotation.Id;
import java.math.BigDecimal;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    private Long id;
    private String name;
    private BigDecimal unitPrice;

    public static Product fromExternal(
            Long id,
            String name,
            BigDecimal price
    ) {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        return Product.builder()
                .id(id)
                .name(name)
                .unitPrice(price)
                .build();
    }
}
