package com.josue.microservice_product.controllers;

import com.josue.microservice_product.entities.Product;
import com.josue.microservice_product.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/{version}/products", version = "v1")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public List<Product> findAll() {
        return productService.findAll();
    }

    @GetMapping(value = "/", version = "v2")
    public List<Product> findAllV2() {
        return List.of();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {

        Optional<Product> product = productService.findById(id);

        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }
}
