package com.example.shop;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ShoppingCartTest {
    
    @Test
    void addProductShouldAddProductToCart(){
        ShoppingCart cart = new ShoppingCart();
        Product product = new Product();
        Set<Product> expectedSet = Set.of(product);

        cart.addProduct(product);

        assertThat(cart.getProducts()).isEqualTo(expectedSet);
    }
}