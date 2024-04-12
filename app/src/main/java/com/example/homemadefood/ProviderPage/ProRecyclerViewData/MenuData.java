package com.example.homemadefood.ProviderPage.ProRecyclerViewData;

public class MenuData {
    private String itemName;
    private double price;
    // Add more fields as needed

    public MenuData() {
        // Default constructor required for Firestore
    }

    public MenuData(String itemName, double price) {
        this.itemName = itemName;
        this.price = price;
    }

    // Getters and setters for the fields
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}