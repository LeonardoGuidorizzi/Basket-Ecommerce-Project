package dev.devdreamer.ecommerce.basketservice.client;

import dev.devdreamer.ecommerce.basketservice.client.response.PlatziProductResponse;
import dev.devdreamer.ecommerce.basketservice.exception.custom.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductClient {

    private final WebClient platziWebClient;

    public PlatziProductResponse findById(Long id) {
        return platziWebClient.get()
                .uri("products/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new ResourceNotFoundException("Product", id)) )
                .bodyToMono(PlatziProductResponse.class)
                .block();
    }

}
