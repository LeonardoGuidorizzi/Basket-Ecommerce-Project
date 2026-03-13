package dev.devdreamer.ecommerce.basketservice.service;

import dev.devdreamer.ecommerce.basketservice.Enum.BasketStatus;
import dev.devdreamer.ecommerce.basketservice.domain.basket.Basket;
import dev.devdreamer.ecommerce.basketservice.domain.order.Order;
import dev.devdreamer.ecommerce.basketservice.domain.user.User;
import dev.devdreamer.ecommerce.basketservice.dto.order.OrderResponseDTO;
import dev.devdreamer.ecommerce.basketservice.mapper.OrderMapper;
import dev.devdreamer.ecommerce.basketservice.repository.BasketRepository;
import dev.devdreamer.ecommerce.basketservice.repository.OrderRepository;
import dev.devdreamer.ecommerce.basketservice.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService  {
    private final BasketRepository basketRepository;
    private final OrderRepository orderRepository;

    public OrderResponseDTO checkout (){
        User user = SecurityUtils.getAuthenticatedUserId();
        Basket basket = basketRepository.findByUserId(user.getId())
                .orElseThrow(()-> new RuntimeException("Basket not found"));

        if (basket.getItems() == null || basket.getItems().isEmpty()){
            throw new IllegalStateException("Cannot checkout an empty basket");
        }

        Order order = Order.from(basket);
        orderRepository.save(order);
        basket.clear();
        basket.updateBasketStatus(BasketStatus.CHECKED_OUT);
        basketRepository.save(basket);
        return OrderMapper.toDto(order);
    }
    public List<OrderResponseDTO> getMyOrders (){
        User user = SecurityUtils.getAuthenticatedUserId();
        return OrderMapper.toDtoList(orderRepository.findByUserId(user.getId()));
    }
    public List<OrderResponseDTO> getAllOrders (){
        return OrderMapper.toDtoList(orderRepository.findAll());
    }
}
