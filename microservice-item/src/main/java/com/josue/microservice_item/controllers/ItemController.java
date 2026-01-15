package com.josue.microservice_item.controllers;

import com.josue.microservice_item.entities.Item;
import com.josue.microservice_item.services.ItemService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/api/v1/items")
public class ItemController {

    private final ItemService itemService;


    public  ItemController(@Qualifier("itemWebClientServiceImp") ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/")
    public List<Item> findAll() {
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
