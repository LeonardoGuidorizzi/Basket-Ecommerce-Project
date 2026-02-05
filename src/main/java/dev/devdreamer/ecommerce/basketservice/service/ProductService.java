package dev.devdreamer.ecommerce.basketservice.service;

import dev.devdreamer.ecommerce.basketservice.client.PlatzStoreClient;
import dev.devdreamer.ecommerce.basketservice.client.response.PlatziProductResponse;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class ProductService {
    private final PlatzStoreClient platzStoreClient;

    public ProductService(PlatzStoreClient platzStoreClient) {
        this.platzStoreClient = platzStoreClient;
    }

    public List<PlatziProductResponse> getAllProducts (){
        return platzStoreClient.getAllProducts();
    }

    public PlatziProductResponse getProductById (Long id){
        return  platzStoreClient.getProductById(id);
    }

}
