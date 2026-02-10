package dev.devdreamer.ecommerce.basketservice.service;

import dev.devdreamer.ecommerce.basketservice.client.PlatzStoreClient;
import dev.devdreamer.ecommerce.basketservice.client.response.PlatziProductResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class ProductService {
    private final PlatzStoreClient platzStoreClient;

    public ProductService(PlatzStoreClient platzStoreClient) {
        this.platzStoreClient = platzStoreClient;
    }
    @Cacheable(value = "products")
    public List<PlatziProductResponse> getAllProducts (){
        log.info("getting all products");
        return platzStoreClient.getAllProducts();
    }
    @Cacheable(value = "product", key = "#id")
    public PlatziProductResponse getProductById (Long id){
        log.info("getting products by id:{}", id);
        return  platzStoreClient.getProductById(id);
    }

}
