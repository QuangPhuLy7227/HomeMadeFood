package com.example.homemadefood.CustomerPage.RecyclerViewData;

public class RestaurantPromotion {
    private final int restaurantImage;
    private final String restaurantName;
    private final float rating;
    private final int totalRating;
    private String distance;
    private String deliveryTime;
    private String priceRange;

    public RestaurantPromotion(int restaurantImage, String restaurantName, float rating, int totalRating, String distance, String deliveryTime, String priceRange) {
        this.restaurantImage = restaurantImage;
        this.restaurantName = restaurantName;
        this.rating = rating;
        this.totalRating = totalRating;
        this.distance = distance;
        this.deliveryTime = deliveryTime;
        this.priceRange = priceRange;
    }

    public int getRestaurantImage() {
        return restaurantImage;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public float getRating() {
        return rating;
    }

    public int getTotalRating() {
        return totalRating;
    }

    public String getDistance() {
        return distance;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }


    public String getPriceRange() {
        return priceRange;
    }

}
