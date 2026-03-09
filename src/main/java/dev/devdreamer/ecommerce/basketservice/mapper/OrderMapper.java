package dev.devdreamer.ecommerce.basketservice.mapper;

import dev.devdreamer.ecommerce.basketservice.domain.basket.Basket;
import dev.devdreamer.ecommerce.basketservice.domain.order.Order;
import dev.devdreamer.ecommerce.basketservice.dto.basket.BasketResponseDTO;
import dev.devdreamer.ecommerce.basketservice.dto.order.OrderResponseDTO;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class OrderMapper {
    public static OrderResponseDTO toDto(Order entity){

        return OrderResponseDTO.builder()
                .items(entity.getItems())
                .totalAmount(entity.getTotalAmount())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updateAt(entity.getUpdateAt())
                .build();
    }

    public static List<OrderResponseDTO> toDtoList(List<Order> baskets) {
        return baskets.stream()
                .map(OrderMapper::toDto)
                .toList();
    }
}
