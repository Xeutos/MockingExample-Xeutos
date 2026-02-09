package com.example.shop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ShoppingCartTest {
    ShoppingCart cart;
    Inventory inventory;

    @BeforeEach
    void setup(){
        inventory = new Inventory(2);
        cart = new ShoppingCart(inventory);
    }
    
    @Test
    void addProductShouldAddProductToCart(){
        Product product = new Product(BigDecimal.ONE);
        Set<Product> expectedSet = Set.of(product);

        cart.addProduct(product);

        assertThat(cart.getProducts()).isEqualTo(expectedSet);
    }

    @Test
    void addProductShouldThrowExceptionWhenProductIsNull(){
        assertThatThrownBy(() -> cart.addProduct(null));
    }

    @Test
    void removeProductShouldRemoveProductFromCart(){
        Product product = new Product(BigDecimal.ONE);

        cart.addProduct(product);
        cart.removeProduct(product);

        assertThat(cart.getProducts().isEmpty()).isTrue();
    }

    @Test
    void removeProductShouldThrowExceptionIfProductIsNull(){
        assertThatThrownBy(() -> cart.removeProduct(null));
    }
    
    @Test
    void calculateTotalPriceShouldReturnCorrectPrice(){
        Product product1 = new Product(BigDecimal.valueOf(200));
        Product product2 = new Product(BigDecimal.valueOf(100));

        cart.addProduct(product1);
        cart.addProduct(product2);

        assertThat(cart.calculateTotalPrice()).isEqualTo(BigDecimal.valueOf(300));
    }

    @Test
    void calculateTotalPriceWhenDiscountIsAppliedShouldReturnCorrectPrice(){
        Product product1 = new Product(BigDecimal.valueOf(200));
        Product product2 = new Product(BigDecimal.valueOf(100));

        cart.addProduct(product1);
        cart.addProduct(product2);

        assertThat(cart.calculateDiscountedTotalPrice(BigDecimal.valueOf(80))).isEqualTo(BigDecimal.valueOf(240.0));
    }

    @Test
    void inventoryStockShouldReduceWhenAddingProductToCart(){
        Product product = new Product(BigDecimal.ONE);

        cart.addProduct(product);

        assertThat(inventory.getStock()).isEqualTo(1);
    }

    @Test
    void inventoryStockShouldIncreaseWhenRemovingProductToCart(){
        Product product = new Product(BigDecimal.ONE);

        cart.addProduct(product);
        cart.removeProduct(product);

        assertThat(inventory.getStock()).isEqualTo(2);
    }
}