package com.josue.microservice_item.controllers;

import com.josue.microservice_item.entities.Item;
import com.josue.microservice_item.models.ProductDto;
import com.josue.microservice_item.services.ItemService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RefreshScope
@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    private final ItemService itemService;
    private final CircuitBreakerFactory circuitBreakerFactory;
    private final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Value("${refreshScope:thisIsByDefault}")
    private String refreshScope;

    public  ItemController(@Qualifier("itemFeignServiceImpl") ItemService itemService, CircuitBreakerFactory circuitBreakerFactory) {
        this.itemService = itemService;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @GetMapping("/gatewayErrorFallback")
    public ResponseEntity<String> gatewayErrorFallback() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/")
    public List<Item> findAll() {
        logger.info("Request refreshSCope {}", refreshScope);
        return itemService.findAll();
    }

    @GetMapping("/by-product/{productId}")
    public ResponseEntity<Item> findById(@PathVariable Long productId) {
        Item item = circuitBreakerFactory.create("items").run(() -> itemService.findByProductId(productId), e -> {
            logger.info("CircuitBreakerFactory, somenthing went wrong in findById {}", productId);
            return null;
        });
        if (item == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(item);
    }

    @CircuitBreaker(name = "items", fallbackMethod = "findByIdCbFallback") // Solo toma en cuenta configuraiones del .ymls
    // Tambien por lo que entiendo, esta anotacion captura cualquier excepcion que se lanze en este metodo y lo manza al fallback
    @GetMapping("/by-product/cb/{productId}")
    public ResponseEntity<Item> findByIdCb(@PathVariable Long productId) {
        return ResponseEntity.ok(itemService.findByProductId(productId));
    }

    public ResponseEntity<Item> findByIdCbFallback(Throwable throwable) {
        return ResponseEntity.notFound().build();
    }

    // Cuando hay ambos no sabe a cual fallbackMethod ir, entonces
    // es necesario tener logs para saber que excepcion esta lanzando, si es un timeout o si es uno del circuit breaker.
    @CircuitBreaker(name = "items", fallbackMethod = "findByIdCbFallback")
    @TimeLimiter(name = "items")
    // Lo mejor es que se declare SOLO un fallbackMethod (en el circuit breaker y funciona para ambos)
    @GetMapping("/by-product/tl/{productId}")
    public CompletableFuture<ResponseEntity<Item>> findByIdTl(@PathVariable Long productId) {
        return CompletableFuture.supplyAsync(() -> {
            return ResponseEntity.ok(itemService.findByProductId(productId));
        });
    }

    public ResponseEntity<Item> findByIdTl(Throwable throwable) {
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<ProductDto> saveProduct(@RequestBody ProductDto productDto) {
        logger.info("Request creating product {}", productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.itemService.saveProduct(productDto));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long productId, @RequestBody ProductDto productDto) {
        logger.info("Request updating product {} with id {}",  productDto,  productId);
        return ResponseEntity.ok(this.itemService.updateProduct(productId, productDto));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        logger.info("Request deleting product {}", productId);
        this.itemService.deleteByProductId(productId);
        return ResponseEntity.noContent().build();
    }
}
