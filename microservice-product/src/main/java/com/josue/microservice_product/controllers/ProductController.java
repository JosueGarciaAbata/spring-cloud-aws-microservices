package com.josue.microservice_product.controllers;

import com.josue.microservice_product.entities.Product;
import com.josue.microservice_product.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
    public ResponseEntity<Product> findById(@PathVariable Long id) throws InterruptedException {

        if (id.equals(10L)) {
            throw new IllegalStateException("Intentional error");
        }

        if (id.equals(20L)) {
            TimeUnit.SECONDS.sleep(6L); // Ajustar si es para probar o un slow call o un timeout.
        }

        Optional<Product> product = productService.findById(id);

        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build()); // Feign lanza excepciones para errores 4.x.x

    }
}
