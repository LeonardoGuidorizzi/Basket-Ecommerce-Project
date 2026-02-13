package dev.devdreamer.ecommerce.basketservice.client;

import dev.devdreamer.ecommerce.basketservice.client.response.PlatzProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "PlatzStoreClient", url = "${basket.client.platzi}")
public interface PlatzStoreClient {
    @GetMapping("/products")
    public List<PlatzProductResponse> getAllProducts();
    @GetMapping("/products/{id}")
    public PlatzProductResponse getProductById(@PathVariable Long id);
}
