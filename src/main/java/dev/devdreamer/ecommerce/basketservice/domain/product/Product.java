package dev.devdreamer.ecommerce.basketservice.domain.product;

import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

public class Product {
    @Id
    private Long id;
    private String name;
    private BigDecimal unitPrice;
}
