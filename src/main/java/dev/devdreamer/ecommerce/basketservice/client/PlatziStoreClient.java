package dev.devdreamer.ecommerce.basketservice.client;

import dev.devdreamer.ecommerce.basketservice.client.response.PlatziProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "PlatzStoreClient", url = "${basket.client.platzi}")
public interface PlatziStoreClient {
    @GetMapping("/products")
    public List<PlatziProductResponse> getAllProducts();
    @GetMapping("/products/{id}")
    public PlatziProductResponse getProductById(@PathVariable Long id);
}
