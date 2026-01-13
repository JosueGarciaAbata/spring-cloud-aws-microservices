package com.josue.microservice_product.services;

import com.josue.microservice_product.entities.Product;
import com.josue.microservice_product.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImp implements ProductService{

    private final ProductRepository repository;

    public ProductServiceImp(ProductRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly=true)
    @Override
    public List<Product> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly=true)
    @Override
    public Optional<Product> findById(Long id) {
        return repository.findById(id);
    }
}
