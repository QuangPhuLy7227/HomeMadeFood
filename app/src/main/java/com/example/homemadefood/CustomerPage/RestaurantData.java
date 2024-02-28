package com.example.homemadefood.CustomerPage;

public class RestaurantData {

    private final int restaurantImage;
    private final String name;
    private final String deliveryFee;


    private final float rating;
    private final int totalRating;
    private final int deliveryTime;

    public RestaurantData(int restaurantImage, String name, String deliveryFee, float rating, int totalRating, int deliveryTime) {
        this.restaurantImage = restaurantImage;
        this.name = name;
        this.deliveryFee = deliveryFee;
        this.rating = rating;
        this.totalRating = totalRating;
        this.deliveryTime = deliveryTime;
    }

    public int getRestaurantImage() {
        return restaurantImage;
    }

    public String getName() {
        return name;
    }

    public String getDeliveryFee() {
        return deliveryFee;
    }

    public float getRating() {
        return rating;
    }

    public int getTotalRating() {
        return totalRating;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }


}
