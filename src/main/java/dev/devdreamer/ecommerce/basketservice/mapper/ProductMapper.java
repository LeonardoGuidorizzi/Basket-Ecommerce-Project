package dev.devdreamer.ecommerce.basketservice.mapper;

import dev.devdreamer.ecommerce.basketservice.client.response.PlatziProductResponse;
import dev.devdreamer.ecommerce.basketservice.domain.product.Product;
import dev.devdreamer.ecommerce.basketservice.exception.custom.BusinessException;

public class ProductMapper {
    public static Product toDomain(PlatziProductResponse response) {

        if (response == null) {
            throw new IllegalArgumentException("Product response cannot be null");
        }

        if (response.price() == null) {
            throw new BusinessException("Product price cannot be null");
        }

        return Product.builder()
                .id(response.getId())
                .title(response.getTitle())
                .price(response.getPrice())
                .build();
    }
}
