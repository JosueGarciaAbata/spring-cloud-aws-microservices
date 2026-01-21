package com.josue.microservice_product.services;

import com.josue.microservice_product.entities.Product;
import com.josue.microservice_product.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly=true)
    @Override
    public List<Product> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly=true)
    @Override
    public Product findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Product not found."));
    }

    @Transactional
    @Override
    public Product save(Product product) {
        return this.repository.save(product);
    }

    @Override
    public Product update(Long id, Product product) {
        Product productFound = repository.findById(id).orElseThrow(() -> new RuntimeException("Product not found."));

        productFound.setName(product.getName());
        productFound.setPrice(product.getPrice());

        this.repository.save(productFound);

        return productFound;
    }

    @Override
    public void deleteById(Long id) {
        this.repository.findById(id).orElseThrow(() -> new RuntimeException("Product not found."));
        this.repository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
