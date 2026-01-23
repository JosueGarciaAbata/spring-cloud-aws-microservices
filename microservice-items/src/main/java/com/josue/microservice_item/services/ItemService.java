package com.josue.microservice_item.services;

import com.josue.microservice_item.entities.Item;
import com.josue.microservice_item.models.ProductDto;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    List<Item> findAll();
    Item findByProductId(Long id);
    ProductDto saveProduct(ProductDto product);
    ProductDto updateProduct(Long productID, ProductDto product);
    void deleteByProductId(Long id);

}
