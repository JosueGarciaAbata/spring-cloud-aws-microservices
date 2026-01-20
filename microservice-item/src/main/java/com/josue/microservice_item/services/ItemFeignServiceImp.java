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
        return this.productFeignClient.findAll().stream().map(product -> {
            Random ran = new Random();
            return new Item(product.getId(), ran.nextInt(11));
        }).toList();
    }

    @Override
    public Item findByProductId(Long id) {
        ProductDto product = this.productFeignClient.findById(id); // Si no existe el producto, Spring lanza una excepcion FeignClient.
        Random ran = new Random();
        return new Item(product.getId(), ran.nextInt(11));
    }

    @Override
    public ProductDto saveProduct(ProductDto product) {
        return this.productFeignClient.save(product);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto product) {
        return this.productFeignClient.update(id, product);
    }

    @Override
    public void deleteByProductId(Long id) {
        this.productFeignClient.deleteById(id);
    }
}
