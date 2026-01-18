package com.josue.microservice_item.controllers;

import com.josue.microservice_item.entities.Item;
import com.josue.microservice_item.services.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/api/v1/items")
public class ItemController {

    private final ItemService itemService;
    private final Logger logger = LoggerFactory.getLogger(ItemController.class);

    public  ItemController(@Qualifier("itemFeignServiceImp") ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/")
    public List<Item> findAll(@RequestParam("name") String nameParam, @RequestHeader("token-request") String tokenRequest) {

        logger.info("ItemController, parametro 'name' = " + nameParam);
        logger.info("ItemController, parametro 'token' = " + tokenRequest);

        return itemService.findAll();
    }

    @GetMapping("/by-product/{productId}")
    public ResponseEntity<Item> findById(@PathVariable Long productId) {
        Optional<Item> item = itemService.findById(productId);
        if (item.isEmpty()) {
            ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(item.get());
    }

}
