package com.example.shop;

public class Inventory {

    private int stock;

    public Inventory(int stock){
        this.stock = stock;
    }

    public int getStock() {
        return stock;
    }

    public void reduceStock() {
        stock -= 1;
    }

    public void increaseStock() {
        stock += 1;
    }
}
