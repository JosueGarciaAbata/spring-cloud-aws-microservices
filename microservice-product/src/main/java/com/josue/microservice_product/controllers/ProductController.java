package com.josue.microservice_product.controllers;

import com.josue.microservice_product.entities.Product;
import com.josue.microservice_product.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(version = "1")
    public List<Product> findAll() {
        return productService.findAll();
    }

    @GetMapping(path = "/{id}", version = "1")
    public ResponseEntity<Product> findById(@PathVariable Long id) {

        Optional<Product> product = productService.findById(id);

        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

}
