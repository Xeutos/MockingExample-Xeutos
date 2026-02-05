package com.example.shop;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

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

    public void removeProduct(Product product) {
        if (product == null)
            throw new IllegalArgumentException("Product cannot be null");
        products.remove(product);
    }

    public BigDecimal calculateTotalPrice() {
        Function<Product, BigDecimal> totalMapper = Product::getPrice;
        return products.stream().map(totalMapper).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateDiscountedTotalPrice(BigDecimal discountPercentage) {
        BigDecimal discount = discountPercentage.divide(BigDecimal.valueOf(100),1, RoundingMode.HALF_EVEN);
        BigDecimal price = calculateTotalPrice();
        return price.multiply(discount);
    }
}
