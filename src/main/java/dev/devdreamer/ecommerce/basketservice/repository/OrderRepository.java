package dev.devdreamer.ecommerce.basketservice.repository;

import dev.devdreamer.ecommerce.basketservice.domain.basket.Basket;
import dev.devdreamer.ecommerce.basketservice.domain.order.Order;
import dev.devdreamer.ecommerce.basketservice.domain.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByUserId(String userId);
    Optional<Order> findByIdAndUserId(String id, String userId);
}
