package dev.devdreamer.ecommerce.basketservice.mapper;

import dev.devdreamer.ecommerce.basketservice.client.response.PlatziProductResponse;
import dev.devdreamer.ecommerce.basketservice.domain.product.Product;

public class ProductMapper {
    public static Product toDomain(PlatziProductResponse response) {
        return Product.builder()
                .id(response.id())
                .name(response.title())
                .slug(response.slug())
                .price(response.price())
                .description(response.description())
                .build();

//        if (response == null) {
//            throw new IllegalArgumentException("Product response cannot be null");
//        }
//
//        if (response.price() == null) {
//            throw new IllegalArgumentException("Product price cannot be null");
//        }

    }
}
