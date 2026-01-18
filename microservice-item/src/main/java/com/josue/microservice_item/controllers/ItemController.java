package com.josue.microservice_item.controllers;

import com.josue.microservice_item.entities.Item;
import com.josue.microservice_item.services.ItemService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/api/v1/items")
public class ItemController {

    private final ItemService itemService;
    private final CircuitBreakerFactory circuitBreakerFactory;
    private final Logger logger = LoggerFactory.getLogger(ItemController.class);

    public  ItemController(@Qualifier("itemFeignServiceImp") ItemService itemService, CircuitBreakerFactory circuitBreakerFactory) {
        this.itemService = itemService;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @GetMapping("/")
    public List<Item> findAll(@RequestParam("name") String nameParam, @RequestHeader("token-request") String tokenRequest) {

        logger.info("ItemController, parametro 'name' = " + nameParam);
        logger.info("ItemController, parametro 'token' = " + tokenRequest);

        return itemService.findAll();
    }

    @GetMapping("/by-product/{productId}")
    public ResponseEntity<Item> findById(@PathVariable Long productId) {
        Optional<Item> item = circuitBreakerFactory.create("items").run(() -> itemService.findById(productId), e -> {
            logger.info("CircuitBreakerFactory, somenthing went wrong in findById' = " + productId);
            return Optional.empty();
        });
        if (item.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(item.get());
    }

    @CircuitBreaker(name = "items", fallbackMethod = "findByIdCbFallback") // Solo toma en cuenta configuraiones del .ymls
    // Tambien por lo que entiendo, esta anotacion captura cualquier excepcion que se lanze en este metodo y lo manza al fallback
    @GetMapping("/by-product/cb/{productId}")
    public ResponseEntity<Item> findByIdCb(@PathVariable Long productId) {
        Optional<Item> item = itemService.findById(productId);
        if (item.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(item.get());
    }

    public ResponseEntity<Item> findByIdCbFallback(Throwable throwable) {
        return ResponseEntity.notFound().build();
    }

}
