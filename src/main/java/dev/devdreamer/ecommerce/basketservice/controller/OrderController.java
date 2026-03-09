package dev.devdreamer.ecommerce.basketservice.controller;

import dev.devdreamer.ecommerce.basketservice.dto.order.OrderResponseDTO;
import dev.devdreamer.ecommerce.basketservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/basketservice/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponseDTO> checkout(){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.checkout());
    }

    @GetMapping("/me")
    public ResponseEntity<List<OrderResponseDTO>> getMyOrder(){
        return  ResponseEntity.ok(orderService.getMyOrders());
    }
}
