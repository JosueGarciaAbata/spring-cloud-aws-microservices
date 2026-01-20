package com.josue.microservice_item.clients;

import com.josue.microservice_item.models.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name ="${microservice.products.name}")
public interface ProductFeignClient {

    @GetMapping("/api/v1/products/")
    List<ProductDto> findAll();

    @GetMapping("/api/v1/products/{id}")
    ProductDto findById(@PathVariable Long id);

    @PostMapping("/api/v1/products/")
    ProductDto save(@RequestBody ProductDto productDto);

    @PutMapping("/api/v1/products/{productId}")
    ProductDto update(@PathVariable Long productId, @RequestBody ProductDto productDto);

    @DeleteMapping("/api/v1/products/{productId}")
    void deleteById(@PathVariable Long productId);

}
