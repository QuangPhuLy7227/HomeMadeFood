package com.example.homemadefood.CustomerPage.RecyclerViewData;

public class RestaurantMenuDrinkModel {
    private String drinkImage;
    private String drinkName;
    private float drinkPrice;

    public RestaurantMenuDrinkModel(String drinkImageUri, String drinkName, float drinkPrice) {
        this.drinkImage = drinkImageUri;
        this.drinkName = drinkName;
        this.drinkPrice = drinkPrice;
    }

    public String getDrinkImage() {
        return drinkImage;
    }

    public void setDrinkImage(String drinkImage) {
        this.drinkImage = drinkImage;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public float getDrinkPrice() {
        return drinkPrice;
    }

    public void setDrinkPrice(float drinkPrice) {
        this.drinkPrice = drinkPrice;
    }


}
