package com.example.homemadefood.CustomerPage.RecyclerViewData.ModelClass;

import android.os.Parcel;
import android.os.Parcelable;

public class RestaurantMenuFoodModel implements Parcelable {
    private String foodImage;
    private String foodName;
    private String foodDescription;
    private float foodPrice;

    public RestaurantMenuFoodModel() {
        // Default constructor required for Firestore
    }


    public RestaurantMenuFoodModel(String foodImage, String foodName, String foodDescription, float foodPrice) {
        this.foodImage = foodImage;
        this.foodName = foodName;
        this.foodDescription = foodDescription;
        this.foodPrice = foodPrice;
    }

    protected RestaurantMenuFoodModel(Parcel in) {
        foodImage = in.readString();
        foodName = in.readString();
        foodDescription = in.readString();
        foodPrice = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(foodImage);
        dest.writeString(foodName);
        dest.writeString(foodDescription);
        dest.writeFloat(foodPrice);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RestaurantMenuFoodModel> CREATOR = new Creator<RestaurantMenuFoodModel>() {
        @Override
        public RestaurantMenuFoodModel createFromParcel(Parcel in) {
            return new RestaurantMenuFoodModel(in);
        }

        @Override
        public RestaurantMenuFoodModel[] newArray(int size) {
            return new RestaurantMenuFoodModel[size];
        }
    };

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public float getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(float foodPrice) {
        this.foodPrice = foodPrice;
    }

}
