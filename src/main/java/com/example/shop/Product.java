package com.example.shop;

import java.math.BigDecimal;

public class Product {

    private final BigDecimal price;

    public Product(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
