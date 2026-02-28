package dev.devdreamer.ecommerce.basketservice.mapper;

import dev.devdreamer.ecommerce.basketservice.domain.basket.Basket;
import dev.devdreamer.ecommerce.basketservice.dto.basket.BasketResponseDTO;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class BasketMapper {

    public static BasketResponseDTO toDto(Basket entity){

        return BasketResponseDTO.builder()
                .items(entity.getItems())
                .totalAmount(entity.getTotalAmount())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updateAt(entity.getUpdateAt())
                .build();
    }

    public static List<BasketResponseDTO> toDtoList(List<Basket> baskets) {
        return baskets.stream()
                .map(BasketMapper::toDto)
                .toList();
    }
}
