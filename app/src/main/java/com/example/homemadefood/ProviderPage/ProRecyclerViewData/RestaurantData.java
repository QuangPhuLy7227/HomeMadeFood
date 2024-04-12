package com.example.homemadefood.ProviderPage.ProRecyclerViewData;

public class RestaurantData {
    private String name;
    private String address;
    // Add more fields as needed

    public RestaurantData() {
        // Default constructor required for Firestore
    }

    public RestaurantData(String name, String address) {
        this.name = name;
        this.address = address;
    }

    // Getters and setters for the fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}