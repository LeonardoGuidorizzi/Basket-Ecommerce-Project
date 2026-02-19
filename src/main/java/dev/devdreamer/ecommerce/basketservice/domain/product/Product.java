package dev.devdreamer.ecommerce.basketservice.domain.product;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
@UtilityClass
@Builder
@Getter
public class Product {
    @Id
    private Long id;
    private String name;
    private BigDecimal unitPrice;
}
