package com.josue.microservice_item.clients;

import com.josue.microservice_item.models.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name ="${microservice.products.name}")
public interface ProductFeignClient {

    @GetMapping("/api/v1/products/")
    List<ProductDto> findAll();

    @GetMapping("/api/v1/products/{id}")
    ProductDto findById(@PathVariable Long id);

}
