package com.example.homemadefood.ProviderPage.data;

public class MenuData {
    private String itemName;
    private String category;
    private String description;
    private double price;
    private String image; // Add this field

    // Required default constructor for Firestore deserialization
    public MenuData() {
        // Empty constructor needed for Firestore
    }
    public MenuData(String itemName, String category, String description, double price, String image) {
        this.itemName = itemName;
        this.category = category;
        this.description = description;
        this.price = price;
        this.image = image; // Initialize the image field
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}