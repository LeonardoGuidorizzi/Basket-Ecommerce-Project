package dev.devdreamer.ecommerce.basketservice.controller;

import dev.devdreamer.ecommerce.basketservice.dto.basket.BasketResponseDTO;
import dev.devdreamer.ecommerce.basketservice.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        basketService.addItem(
                productId,
                quantity
        );
        return ResponseEntity.ok().build();
    }

    @PutMapping("/items")
    public ResponseEntity<Void> updateQuantity(
            @RequestParam Long productId,
            @RequestParam Integer quantity
    ){
        basketService.updateQuantity(
                productId,
                quantity
        );
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/items")
    public ResponseEntity<Void> deleteItem(
            @RequestParam Long productId
    ){
        basketService.removeItem(
                productId
        );
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/items")
    public ResponseEntity<List<BasketResponseDTO>> getAllBaskets(
    ){
        return ResponseEntity.ok(basketService.getAllBaskets());
    }


    @GetMapping("/me")
    public ResponseEntity<BasketResponseDTO> getBasket(
    ){
        return ResponseEntity.ok(basketService.getMyBasket());
    }

}
