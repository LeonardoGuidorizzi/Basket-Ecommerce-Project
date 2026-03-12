package dev.devdreamer.ecommerce.basketservice.service;

import dev.devdreamer.ecommerce.basketservice.client.ProductClient;
import dev.devdreamer.ecommerce.basketservice.client.response.PlatziProductResponse;
import dev.devdreamer.ecommerce.basketservice.domain.basket.Basket;
import dev.devdreamer.ecommerce.basketservice.domain.product.Product;
import dev.devdreamer.ecommerce.basketservice.domain.user.User;
import dev.devdreamer.ecommerce.basketservice.dto.basket.BasketResponseDTO;
import dev.devdreamer.ecommerce.basketservice.exception.custom.BusinessException;
import dev.devdreamer.ecommerce.basketservice.mapper.BasketMapper;
import dev.devdreamer.ecommerce.basketservice.repository.BasketRepository;
import dev.devdreamer.ecommerce.basketservice.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasketService {
    private final BasketRepository basketRepository;
    private final ProductClient productClient;

    public void addItem (
                         Long productId,
                         Integer quantity){
        User user = SecurityUtils.getAuthenticatedUserId();
        Basket basket = basketRepository
            .findByUserId(user.getId())
            .orElseGet(()-> Basket.create(user.getId()));

        PlatziProductResponse response = productClient.findById(productId);

        Product product = Product.fromExternal(
                response.id(),
                response.title(),
                response.price()
        );

        basket.addItem(product, quantity);

        basketRepository.save(basket);

    }
    public void updateQuantity(Long productId, Integer quantity){
        User user = SecurityUtils.getAuthenticatedUserId();
        Basket basket = basketRepository.findByUserId(user.getId()).orElseThrow(()-> new BusinessException("Basket not found"));
        basket.updateQuantity(productId, quantity);
        basketRepository.save(basket);
    }

    public void removeItem(Long productId){
        User user = SecurityUtils.getAuthenticatedUserId();
        Basket basket = basketRepository
                .findByUserId(user.getId())
                .orElseThrow(()-> new BusinessException("Basket not found"));
        basket.removeItem(productId);
        basketRepository.save(basket);

    }

    public BasketResponseDTO getMyBasket(){
        User user = SecurityUtils.getAuthenticatedUserId();
            Basket basket = basketRepository.findByUserId(user.getId()).orElseThrow(()-> new BusinessException("Basket not found"));
            return BasketMapper.toDto(basket);

    }

    public List<BasketResponseDTO> getAllBaskets (){
        List<Basket> baskets = basketRepository.findAll();
        return BasketMapper.toDtoList(baskets);
    }
}
