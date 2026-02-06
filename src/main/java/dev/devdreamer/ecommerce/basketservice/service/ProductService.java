package dev.devdreamer.ecommerce.basketservice.service;

import dev.devdreamer.ecommerce.basketservice.client.PlatzStoreClient;
import dev.devdreamer.ecommerce.basketservice.client.response.PlatziProductResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class ProductService {
    private final PlatzStoreClient platzStoreClient;

    public ProductService(PlatzStoreClient platzStoreClient) {
        this.platzStoreClient = platzStoreClient;
    }
    @Cacheable(value = "products")
    public List<PlatziProductResponse> getAllProducts (){
        return platzStoreClient.getAllProducts();
    }
    @Cacheable(value = "product", key = "#id")
    public PlatziProductResponse getProductById (Long id){
        return  platzStoreClient.getProductById(id);
    }

}
