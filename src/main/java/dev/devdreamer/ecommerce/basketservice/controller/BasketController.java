package dev.devdreamer.ecommerce.basketservice.controller;

import dev.devdreamer.ecommerce.basketservice.dto.basket.BasketResponseDTO;
import dev.devdreamer.ecommerce.basketservice.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/basketservice/basket")
@RequiredArgsConstructor
public class BasketController {
    private final BasketService basketService;
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/items")
    public ResponseEntity<Void> addItem(
            @RequestParam Long productId,
            @RequestParam Integer quantity
    ){
        basketService.addItem(
                productId,
                quantity
        );
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/items")
    public ResponseEntity<Void> updateQuantity(
            @RequestParam Long productId,
            @RequestParam Integer quantity
    ){
        basketService.updateQuantity(
                productId,
                quantity
        );
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/items")
    public ResponseEntity<Void> deleteItem(
            @RequestParam Long productId
    ){
        basketService.removeItem(
                productId
        );
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<List<BasketResponseDTO>> getAllBaskets(
    ){
        return ResponseEntity.ok(basketService.getAllBaskets());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me")
    public ResponseEntity<BasketResponseDTO> getBasket(
    ){
        return ResponseEntity.ok(basketService.getMyBasket());
    }



}
