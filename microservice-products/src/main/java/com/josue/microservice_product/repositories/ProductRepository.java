package com.josue.microservice_product.repositories;

import com.josue.microservice_product.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
