package com.example.homemadefood.CustomerPage.RecyclerViewData;

import android.os.Parcel;
import android.os.Parcelable;

public class RestaurantData implements Parcelable {

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

    protected RestaurantData(Parcel in) {
        restaurantImageUri = in.readString();
        name = in.readString();
        category = in.readString();
        deliveryFee = in.readFloat();
        rating = in.readFloat();
        totalRating = in.readInt();
        deliveryTime = in.readInt();
    }

    public static final Creator<RestaurantData> CREATOR = new Creator<RestaurantData>() {
        @Override
        public RestaurantData createFromParcel(Parcel in) {
            return new RestaurantData(in);
        }

        @Override
        public RestaurantData[] newArray(int size) {
            return new RestaurantData[size];
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
    }
}
