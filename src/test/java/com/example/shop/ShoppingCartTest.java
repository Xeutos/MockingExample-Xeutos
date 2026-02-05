package com.example.shop;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ShoppingCartTest {
    
    @Test
    void addProductShouldAddProductToCart(){
        ShoppingCart cart = new ShoppingCart();
        Product product = new Product(BigDecimal.ONE);
        Set<Product> expectedSet = Set.of(product);

        cart.addProduct(product);

        assertThat(cart.getProducts()).isEqualTo(expectedSet);
    }

    @Test
    void addProductShouldThrowExceptionWhenProductIsNull(){
        ShoppingCart cart = new ShoppingCart();

        assertThatThrownBy(() -> cart.addProduct(null));
    }

    @Test
    void removeProductShouldRemoveProductFromCart(){
        ShoppingCart cart = new ShoppingCart();
        Product product = new Product(BigDecimal.ONE);

        cart.addProduct(product);
        cart.removeProduct(product);

        assertThat(cart.getProducts().isEmpty()).isTrue();
    }

    @Test
    void removeProductShouldThrowExceptionIfProductIsNull(){
        ShoppingCart cart = new ShoppingCart();

        assertThatThrownBy(() -> cart.removeProduct(null));
    }
    
    @Test
    void calculateTotalPriceShouldReturnCorrectPrice(){
        ShoppingCart cart = new ShoppingCart();
        Product product1 = new Product(BigDecimal.valueOf(200));
        Product product2 = new Product(BigDecimal.valueOf(100));

        cart.addProduct(product1);
        cart.addProduct(product2);

        assertThat(cart.calculateTotalPrice()).isEqualTo(BigDecimal.valueOf(300));
    }
}