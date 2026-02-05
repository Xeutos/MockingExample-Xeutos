package com.example.shop;

import java.util.HashSet;
import java.util.Set;

public class ShoppingCart {

    private final HashSet<Product> products;

    public ShoppingCart() {
        this.products = new HashSet<>();
    }

    public void addProduct(Product product) {
        if (product == null)
            throw new IllegalArgumentException("Product cannot be null");
        products.add(product);
    }

    public Set<Product> getProducts() {
        return products;
    }
}
