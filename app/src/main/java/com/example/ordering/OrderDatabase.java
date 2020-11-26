package com.example.ordering;

import java.util.ArrayList;

public class OrderDatabase {
    private static final OrderDatabase instance = new OrderDatabase();

    private ArrayList<Orders> ordersArrayList;
    private ArrayList<String> foodName;
    private ArrayList<String> foodPrice;
    private ArrayList<String> foodQty;
    private ArrayList<String> foodRestaurant;

    private OrderDatabase(){

    }

    public static OrderDatabase getInstance() {
        return instance;
    }


    public ArrayList<Orders> getOrdersArrayList() {
        return ordersArrayList;
    }

    public void setOrdersArrayList(ArrayList<Orders> ordersArrayList) {
        this.ordersArrayList = ordersArrayList;
    }
}
