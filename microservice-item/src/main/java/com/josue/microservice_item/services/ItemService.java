package com.josue.microservice_item.services;

import com.josue.microservice_item.entities.Item;
import com.josue.microservice_item.models.ProductDto;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    List<Item> findAll();
    Optional<Item> findById(Long id);
    ProductDto save(ProductDto product);
    ProductDto update(Long id, ProductDto product);
    void deleteById(Long id);

}
