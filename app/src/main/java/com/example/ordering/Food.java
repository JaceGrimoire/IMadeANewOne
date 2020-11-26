package com.example.ordering;

import android.util.Log;

public class Food {
    private String Name;
    private double Price;

    public Food() {

    }

    public Food(String name, double price) {
        this.Name = name;
        this.Price = price;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        this.Price = price;
    }
}
