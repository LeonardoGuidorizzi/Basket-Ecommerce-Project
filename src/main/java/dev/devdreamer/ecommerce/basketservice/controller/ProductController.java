package dev.devdreamer.ecommerce.basketservice.controller;


import dev.devdreamer.ecommerce.basketservice.client.response.PlatziProductResponse;
import dev.devdreamer.ecommerce.basketservice.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/basketservice/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    public ResponseEntity<List<PlatziProductResponse>> getAllProducts (){
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlatziProductResponse> getProductById (@PathVariable Long id){
        return ResponseEntity.ok(productService.getProductById(id));
    }

}
