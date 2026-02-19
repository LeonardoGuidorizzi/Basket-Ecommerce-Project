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
    private String slug;
    private BigDecimal price;
    private String description;
}
