package com.example.homemadefood.CustomerPage.RecyclerViewData.ModelClass;

import android.os.Parcel;
import android.os.Parcelable;

public class RestaurantDataModel implements Parcelable {

    private String restaurantImageUri;
    private String name;
    private String category;
    private float deliveryFee;
    private float rating;
    private int totalRating;
    private int deliveryTime;
    private String providerUsername; // New field to store provider's username

    public RestaurantDataModel() {
        // Default constructor required for Firebase deserialization
    }

    public RestaurantDataModel(String restaurantImageUri, String name, String category, float deliveryFee, float rating, int totalRating, String providerUsername) {
        this.restaurantImageUri = restaurantImageUri;
        this.name = name;
        this.category = category;
        this.deliveryFee = deliveryFee;
        this.rating = rating;
        this.totalRating = totalRating;
        this.deliveryTime = 0;
        this.providerUsername = providerUsername;
    }

    protected RestaurantDataModel(Parcel in) {
        restaurantImageUri = in.readString();
        name = in.readString();
        category = in.readString();
        deliveryFee = in.readFloat();
        rating = in.readFloat();
        totalRating = in.readInt();
        deliveryTime = in.readInt();
        providerUsername = in.readString(); // Read provider's username from Parcel
    }

    public static final Creator<RestaurantDataModel> CREATOR = new Creator<RestaurantDataModel>() {
        @Override
        public RestaurantDataModel createFromParcel(Parcel in) {
            return new RestaurantDataModel(in);
        }

        @Override
        public RestaurantDataModel[] newArray(int size) {
            return new RestaurantDataModel[size];
        }
    };

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

    public String getProviderUsername() {
        return providerUsername;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(restaurantImageUri);
        dest.writeString(name);
        dest.writeString(category);
        dest.writeFloat(deliveryFee);
        dest.writeFloat(rating);
        dest.writeInt(totalRating);
        dest.writeInt(deliveryTime);
        dest.writeString(providerUsername); // Write provider's username to Parcel
    }
}
