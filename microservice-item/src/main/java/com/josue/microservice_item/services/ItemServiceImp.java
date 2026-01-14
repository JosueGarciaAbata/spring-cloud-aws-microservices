package com.josue.microservice_item.services;

import com.josue.microservice_item.clients.ProductFeignClient;
import com.josue.microservice_item.entities.Item;
import com.josue.microservice_item.models.ProductDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ItemServiceImp implements ItemService {

    private final ProductFeignClient productFeignClient;

    public ItemServiceImp(ProductFeignClient productFeignClient) {
        this.productFeignClient = productFeignClient;
    }

    @Override
    public List<Item> findAll() {
        return productFeignClient.findAll().stream().map(product -> {
            Random ran = new Random();
            return new Item(product, ran.nextInt(11));
        }).toList();
    }

    @Override
    public Optional<Item> findById(Long id) {
        ProductDto product = productFeignClient.findById(id);
        if (product == null) {
            return Optional.empty();
        }

        Random ran = new Random();
        return  Optional.of(new Item(product, ran.nextInt(11)));
    }
}
