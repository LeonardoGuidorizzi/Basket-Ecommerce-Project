package dev.devdreamer.ecommerce.basketservice.service;

import dev.devdreamer.ecommerce.basketservice.Enum.BasketStatus;
import dev.devdreamer.ecommerce.basketservice.domain.basket.Basket;
import dev.devdreamer.ecommerce.basketservice.domain.order.Order;
import dev.devdreamer.ecommerce.basketservice.domain.user.User;
import dev.devdreamer.ecommerce.basketservice.dto.order.OrderResponseDTO;
import dev.devdreamer.ecommerce.basketservice.exception.custom.BusinessException;
import dev.devdreamer.ecommerce.basketservice.mapper.OrderMapper;
import dev.devdreamer.ecommerce.basketservice.repository.BasketRepository;
import dev.devdreamer.ecommerce.basketservice.repository.OrderRepository;
import dev.devdreamer.ecommerce.basketservice.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static reactor.netty.http.HttpConnectionLiveness.log;

@Service
@RequiredArgsConstructor
public class OrderService  {
    private final BasketRepository basketRepository;
    private final OrderRepository orderRepository;

    public OrderResponseDTO checkout (){
        User user = SecurityUtils.getAuthenticatedUserId();
        Basket basket = basketRepository.findByUserId(user.getId())
                .orElseThrow(()-> new BusinessException("Basket not found"));

        if (basket.getItems() == null || basket.getItems().isEmpty()){
            throw new BusinessException("Cannot checkout an empty basket");
        }

        Order order = Order.from(basket);
        orderRepository.save(order);
        basket.clear();
        basket.updateBasketStatus(BasketStatus.CHECKED_OUT);
        basketRepository.save(basket);
        return OrderMapper.toDto(order);
    }
    @Cacheable(cacheNames = "userOrder")
    public List<OrderResponseDTO> getMyOrders (){
        log.info("get user order");
        User user = SecurityUtils.getAuthenticatedUserId();
        return OrderMapper.toDtoList(orderRepository.findByUserId(user.getId()));
    }
    @Cacheable(cacheNames = "adminOrder")
    public List<OrderResponseDTO> getAllOrders (){
        log.info("get all orders");
        return OrderMapper.toDtoList(orderRepository.findAll());
    }
}
