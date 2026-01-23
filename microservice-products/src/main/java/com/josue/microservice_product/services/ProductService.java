package com.josue.microservice_product.services;

import com.josue.microservice_product.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> findAll();
    Product findById(Long id);
    Product save(Product product);
    Product update(Long id,Product product);
    void deleteById(Long id);
    boolean existsById(Long id);
}
