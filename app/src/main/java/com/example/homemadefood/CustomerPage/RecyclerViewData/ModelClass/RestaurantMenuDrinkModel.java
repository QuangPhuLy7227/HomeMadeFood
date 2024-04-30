package com.example.homemadefood.CustomerPage.RecyclerViewData.ModelClass;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class RestaurantMenuDrinkModel implements Parcelable {
    private String drinkImage;
    private String drinkName;
    private float drinkPrice;

    public RestaurantMenuDrinkModel(String drinkImageUri, String drinkName, float drinkPrice) {
        this.drinkImage = drinkImageUri;
        this.drinkName = drinkName;
        this.drinkPrice = drinkPrice;
    }

    protected RestaurantMenuDrinkModel(Parcel in) {
        drinkImage = in.readString();
        drinkName = in.readString();
        drinkPrice = in.readFloat();
    }

    public static final Creator<RestaurantMenuDrinkModel> CREATOR = new Creator<RestaurantMenuDrinkModel>() {
        @Override
        public RestaurantMenuDrinkModel createFromParcel(Parcel in) {
            return new RestaurantMenuDrinkModel(in);
        }

        @Override
        public RestaurantMenuDrinkModel[] newArray(int size) {
            return new RestaurantMenuDrinkModel[size];
        }
    };

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(drinkImage);
        dest.writeString(drinkName);
        dest.writeFloat(drinkPrice);
    }
}
