package dev.devdreamer.ecommerce.basketservice.controller;

import dev.devdreamer.ecommerce.basketservice.client.ProductClient;
import dev.devdreamer.ecommerce.basketservice.domain.user.User;
import dev.devdreamer.ecommerce.basketservice.security.util.SecurityUtils;
import dev.devdreamer.ecommerce.basketservice.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/basketservice/basket")
@RequiredArgsConstructor
public class BasketController {
    private final BasketService basketService;

    @PostMapping("/items")
    public ResponseEntity<Void> addItem(
            @RequestParam Long productId,
            @RequestParam Integer quantity,
            @AuthenticationPrincipal User user
    ){
        String userId = SecurityUtils.getAuthenticatedUserId();
        basketService.addItem(
                userId,
                productId,
                quantity
        );
        return ResponseEntity.ok().build();
    }
}
