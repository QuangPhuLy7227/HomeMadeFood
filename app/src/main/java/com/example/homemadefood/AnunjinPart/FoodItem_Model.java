package com.example.homemadefood.AnunjinPart;

public class FoodItem_Model {
    private String name;
    private double rating;
    private double price;
    private String itemImage;
    private String orderId;

    public FoodItem_Model(String name, double rating, double price, String itemImage, String orderId) {
        this.name = name;
        this.rating = rating;
        this.price = price;
        this.itemImage = itemImage;
        this.orderId = orderId;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getItemImage() {

        return itemImage;
    }

    public void setItemImage(String name) {

        this.itemImage = itemImage;
    }
    public String getOrderId(){
        return orderId;
    }
    public void setOrderId(){
        this.orderId = orderId;
    }
}