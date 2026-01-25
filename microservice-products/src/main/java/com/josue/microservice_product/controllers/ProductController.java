package com.josue.microservice_product.controllers;

import com.josue.microservice_product.entities.Product;
import com.josue.microservice_product.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(path = "/api/{version}/products", version = "v1")
public class ProductController {

    private final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public List<Product> findAll() {
        logger.info("Request getting all products");
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

        Product product = productService.findById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping("/")
    public ResponseEntity<Product> save(@RequestBody Product product) {
        logger.info("Request saving product {}", product);
        Product saved = productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product product) {
        logger.info("Request updating product {} with id {}", product, id);
        return  ResponseEntity.ok(productService.update(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        logger.info("Request deleting product {}", id);
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
