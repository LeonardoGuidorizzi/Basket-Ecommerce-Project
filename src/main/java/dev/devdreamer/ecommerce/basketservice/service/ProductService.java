package dev.devdreamer.ecommerce.basketservice.service;

import dev.devdreamer.ecommerce.basketservice.client.PlatziStoreClient;
import dev.devdreamer.ecommerce.basketservice.client.response.PlatziProductResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class ProductService {
    private final PlatziStoreClient platziStoreClient;

    public ProductService(PlatziStoreClient platziStoreClient) {
        this.platziStoreClient = platziStoreClient;
    }
    @Cacheable(value = "products")
    public List<PlatziProductResponse> getAllProducts (){
        log.info("getting all products");
        return platziStoreClient.getAllProducts();
    }
    @Cacheable(value = "product", key = "#id")
    public PlatziProductResponse getProductById (Long id){
        log.info("getting products by id:{}", id);
        return  platziStoreClient.getProductById(id);
    }

}
