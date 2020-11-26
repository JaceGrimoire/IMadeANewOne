package com.example.ordering;

public class Orders {
    private String foodName;
    private double price;
    private int qty;
    private String restaurant;

    public Orders() {

    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public Orders(String foodName, double price, int qty, String restaurant) {
        this.foodName = foodName;
        this.price = price;
        this.qty = qty;
        this.restaurant = restaurant;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
