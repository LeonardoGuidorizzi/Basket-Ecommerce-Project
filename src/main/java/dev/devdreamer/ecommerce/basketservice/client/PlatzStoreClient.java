package dev.devdreamer.ecommerce.basketservice.client.response;

import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

@FeignClient(name = "PlatzStoreClient", url = "${basket.client.platzi}")
public interface PlatzStoreClient {
    public List<PlatziProductResponse> getAllProducts();
    public PlatziProductResponse getProductById(Long id);
}
