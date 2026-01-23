package com.josue.microservice_item.entities;

import com.josue.microservice_item.models.ProductDto;

public class Item {

    private Long productId; // En un proyecto ya real, en realidad solo se tiene el productId y en base a eso se trabja.
    private int quantity;

    public Item() {
    }

    public  Item(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
