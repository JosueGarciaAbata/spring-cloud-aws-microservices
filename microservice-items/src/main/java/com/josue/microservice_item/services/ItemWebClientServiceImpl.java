package com.josue.microservice_item.services;

import com.josue.microservice_item.entities.Item;
import com.josue.microservice_item.models.ProductDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class ItemWebClientServiceImpl implements ItemService {

    private final WebClient webClient;

    public ItemWebClientServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    // Realmente la llamada seria en la bd de este microservicio.
    @Override
    public List<Item> findAll() {
        return this.webClient
                .get()
                .uri("/")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(ProductDto.class)
                .map(product -> {
                    Random ran = new Random();
                    return new Item(product.getId(), ran.nextInt(11));
                })
                .collectList()
                .block();
    }

    @Override
    public Item findByProductId(Long productId) {
        // Esto es por aprendizaje, lo logico seria traer el item de la bd de este microservicio.
        return this.webClient
                .get()
                .uri("/{id}", productId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .map(product -> new Item(productId, new Random().nextInt(11)))
                .block();
    }

    // Motivos de aprendizaje, se puede usar directamente el endpoint de save product de 'ProductController'
    @Override
    public ProductDto saveProduct(ProductDto product) {
        return this.webClient
                .post()
                .uri("/")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(product)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .block();
    }


    // Motivos de aprendizaje, se puede usar directamente el endpoint de save product de 'ProductController'.
    @Override
    public ProductDto updateProduct(Long productId, ProductDto product) {
        return this.webClient
                .put()
                .uri("/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(product)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .block();
    }


    // Motivos de aprendizaje, se puede usar directamente el endpoint de save product de 'ProductController'
    @Override
    public void deleteByProductId(Long productId) {
        this.webClient
                .delete()
                .uri("/{id}", productId)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
