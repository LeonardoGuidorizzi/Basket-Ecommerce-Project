package dev.devdreamer.ecommerce.basketservice.service;

import dev.devdreamer.ecommerce.basketservice.client.ProductClient;
import dev.devdreamer.ecommerce.basketservice.client.response.PlatziProductResponse;
import dev.devdreamer.ecommerce.basketservice.domain.basket.Basket;
import dev.devdreamer.ecommerce.basketservice.domain.product.Product;
import dev.devdreamer.ecommerce.basketservice.dto.basket.BasketResponseDTO;
import dev.devdreamer.ecommerce.basketservice.mapper.BasketMapper;
import dev.devdreamer.ecommerce.basketservice.repository.BasketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasketService {
    private final BasketRepository basketRepository;
    private final ProductClient productClient;

    public void addItem (String userId,
                         Long productId,
                         Integer quantity){

    Basket basket = basketRepository
            .findByUserId(userId)
            .orElseGet(()-> Basket.create(userId));

        PlatziProductResponse response = productClient.findById(productId);

        Product product = Product.fromExternal(
                response.id(),
                response.title(),
                response.price()
        );

        basket.addItem(product, quantity);

        basketRepository.save(basket);

    }

    public void removeItem(String userId,
                           Long productId){
        Basket basket = basketRepository.findByUserId(userId).orElseThrow(()-> new RuntimeException("Basket not found"));
        basket.removeItem(productId);
        basketRepository.save(basket);

    }

    public BasketResponseDTO getBasket(String useId){
        Basket basket = basketRepository.findByUserId(useId).orElseThrow(()-> new RuntimeException("Basket not found"));
        return BasketMapper.toDto(basket);
    }
}
