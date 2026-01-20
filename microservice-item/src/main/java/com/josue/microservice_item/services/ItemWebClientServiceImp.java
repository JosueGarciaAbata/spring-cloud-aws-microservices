package com.josue.microservice_item.services;

import com.josue.microservice_item.entities.Item;
import com.josue.microservice_item.models.ProductDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class ItemWebClientServiceImp implements ItemService {

    @Value("${microservice.products.url}")
    private String url;

    private final WebClient.Builder builder;

    public  ItemWebClientServiceImp(WebClient.Builder builder) {
        this.builder = builder;
    }

    // Realmente la llamada seria en la bd de este microservicio.
    @Override
    public List<Item> findAll() {
        return this.builder.build()
                .get()
                .uri(url + "/")
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
        return this.builder.build()
                .get()
                .uri(url + "/{id}", productId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .map(product -> new Item(productId, new Random().nextInt(11)))
                .block();
    }

    // Motivos de aprendizaje, se puede usar directamente el endpoint de save product de 'ProductController'
    @Override
    public ProductDto saveProduct(ProductDto product) {
        return this.builder.build()
                .post()
                .uri(url + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(product)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .block();
    }


    // Motivos de aprendizaje, se puede usar directamente el endpoint de save product de 'ProductController'.
    @Override
    public ProductDto updateProduct(Long productId, ProductDto product) {
        return this.builder.build()
                .put()
                .uri(url + "/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(product)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .block();
    }


    // Motivos de aprendizaje, se puede usar directamente el endpoint de save product de 'ProductController'
    @Override
    public void deleteByProductId(Long productId) {

        this.builder.build()
                .delete()
                .uri(url + "/{id}", productId)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
