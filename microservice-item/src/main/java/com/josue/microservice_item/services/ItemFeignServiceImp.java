package com.josue.microservice_item.services;

import com.josue.microservice_item.clients.ProductFeignClient;
import com.josue.microservice_item.entities.Item;
import com.josue.microservice_item.models.ProductDto;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ItemFeignServiceImp implements ItemService {

    private final ProductFeignClient productFeignClient;

    public ItemFeignServiceImp(ProductFeignClient productFeignClient) {
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
        try {
            ProductDto product = productFeignClient.findById(id); // Si no existe el producto, Spring lanza una excepcion FeignClient.
            Random ran = new Random();
            return  Optional.of(new Item(product, ran.nextInt(11)));
        } catch (FeignException.FeignClientException ex) {
            throw new RuntimeException("Product not found");
        }
    }
}
