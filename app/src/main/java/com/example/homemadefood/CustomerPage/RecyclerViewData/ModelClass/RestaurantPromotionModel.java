package com.example.homemadefood.CustomerPage.RecyclerViewData.ModelClass;

import android.os.Parcel;
import android.os.Parcelable;

public class RestaurantPromotionModel implements Parcelable {
    private final int restaurantImage;
    private final String restaurantName;
    private final float rating;
    private final int totalRating;
    private String distance;
    private String deliveryTime;
    private String priceRange;

    public RestaurantPromotionModel(int restaurantImage, String restaurantName, float rating, int totalRating, String distance, String deliveryTime, String priceRange) {
        this.restaurantImage = restaurantImage;
        this.restaurantName = restaurantName;
        this.rating = rating;
        this.totalRating = totalRating;
        this.distance = distance;
        this.deliveryTime = deliveryTime;
        this.priceRange = priceRange;
    }

    protected RestaurantPromotionModel(Parcel in) {
        restaurantImage = in.readInt();
        restaurantName = in.readString();
        rating = in.readFloat();
        totalRating = in.readInt();
        distance = in.readString();
        deliveryTime = in.readString();
        priceRange = in.readString();
    }

    public static final Creator<RestaurantPromotionModel> CREATOR = new Creator<RestaurantPromotionModel>() {
        @Override
        public RestaurantPromotionModel createFromParcel(Parcel in) {
            return new RestaurantPromotionModel(in);
        }

        @Override
        public RestaurantPromotionModel[] newArray(int size) {
            return new RestaurantPromotionModel[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(restaurantImage);
        dest.writeString(restaurantName);
        dest.writeFloat(rating);
        dest.writeInt(totalRating);
        dest.writeString(distance);
        dest.writeString(deliveryTime);
        dest.writeString(priceRange);
    }
}
