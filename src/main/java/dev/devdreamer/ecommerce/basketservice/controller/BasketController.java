package dev.devdreamer.ecommerce.basketservice.controller;

import dev.devdreamer.ecommerce.basketservice.security.util.SecurityUtils;
import dev.devdreamer.ecommerce.basketservice.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/basketservice/basket")
@RequiredArgsConstructor
public class BasketController {
    private final BasketService basketService;

    @PostMapping("/items")
    public ResponseEntity<Void> addItem(
            @RequestParam Long productId,
            @RequestParam Integer quantity
    ){
        String userId = SecurityUtils.getAuthenticatedUserId();
        basketService.addItem(
                userId,
                productId,
                quantity
        );
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/items")
    public ResponseEntity<Void> deleteItem(
            @RequestParam Long productId
    ){
        String userId = SecurityUtils.getAuthenticatedUserId();
        basketService.removeItem(
                userId,
                productId
        );
        return ResponseEntity.noContent().build();
    }
}
