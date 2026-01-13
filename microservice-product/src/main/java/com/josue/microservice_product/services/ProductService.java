package com.josue.microservice_product.services;

import com.josue.microservice_product.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> findAll();
    Optional<Product> findById(Long id);

}
