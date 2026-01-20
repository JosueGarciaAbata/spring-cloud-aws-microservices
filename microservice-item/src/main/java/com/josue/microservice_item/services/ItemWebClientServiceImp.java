package com.josue.microservice_item.services;

import com.josue.microservice_item.entities.Item;
import com.josue.microservice_item.models.ProductDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.*;

@Service
public class ItemWebClientServiceImp implements ItemService {

    @Value("${microservice.products.url}")
    private String url;

    private final WebClient.Builder builder;

    public  ItemWebClientServiceImp(WebClient.Builder builder) {
        this.builder = builder;
    }

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
                    return new Item(product, ran.nextInt(11));
                })
                .collectList()
                .block();
    }

    @Override
    public Optional<Item> findById(Long id) {
        Map<String,Long> params = new HashMap<>();
        params.put("id",id);

        try {
            return Optional.ofNullable(
                    this.builder.build()
                            .get()
                            .uri(url + "/{id}", params)
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToMono(ProductDto.class)
                            .map(product -> {
                                Random ran = new Random();
                                return new Item(product, ran.nextInt(11));
                            })
                            .block()
            );
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Product not found: " + e.getMessage());
        }
    }

    @Override
    public ProductDto save(ProductDto product) {
        return null;
    }

    @Override
    public ProductDto update(Long id, ProductDto product) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
