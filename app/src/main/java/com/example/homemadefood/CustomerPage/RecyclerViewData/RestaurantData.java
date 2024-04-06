package com.example.homemadefood.CustomerPage.RecyclerViewData;

public class RestaurantData {

    private String restaurantImageUri;
    private String name;
    private String category;
    private float deliveryFee;
    private float rating;
    private int totalRating;
    private int deliveryTime;

    public RestaurantData() {
        // Default constructor required for Firebase deserialization
    }

    public RestaurantData(String restaurantImageUri, String name, String category, float deliveryFee, float rating, int totalRating) {
        this.restaurantImageUri = restaurantImageUri;
        this.name = name;
        this.category = category;
        this.deliveryFee = deliveryFee;
        this.rating = rating;
        this.totalRating = totalRating;
        this.deliveryTime = 0;
    }

    public void setRestaurantImageUri(String restaurantImageUri) {
        this.restaurantImageUri = restaurantImageUri;
    }

    public String getRestaurantImageUri() {
        return restaurantImageUri;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public float getDeliveryFee() {
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