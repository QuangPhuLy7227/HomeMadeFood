package com.example.homemadefood.ProviderPage.data;

import android.os.Parcel;
import android.os.Parcelable;

public class RestaurantData implements Parcelable {
    private String addedBy;
    private String address;
    private String category;
    private String closeHours;
    private String date;
    private String info;
    private double latitude;
    private double longitude;
    private String name;
    private String openHours;
    private String phoneNumber;
    private String restaurantImageUri;
    private String zipCode;
    private String providerUsername; // New field

    // Default constructor (required by Firestore)
    public RestaurantData() {
        // Default constructor required for calls to DataSnapshot.getValue(RestaurantData.class)
    }

    // Parameterized constructor
    public RestaurantData(String addedBy, String address, String category, String closeHours, String date,
                          String info, double latitude, double longitude, String name, String openHours,
                          String phoneNumber, String restaurantImageUri, String zipCode, String providerUsername) {
        this.addedBy = addedBy;
        this.address = address;
        this.category = category;
        this.closeHours = closeHours;
        this.date = date;
        this.info = info;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.openHours = openHours;
        this.phoneNumber = phoneNumber;
        this.restaurantImageUri = restaurantImageUri;
        this.zipCode = zipCode;
        this.providerUsername = providerUsername;
    }

    // Constructor for parcelable
    public RestaurantData(Parcel in) {
        addedBy = in.readString();
        address = in.readString();
        category = in.readString();
        closeHours = in.readString();
        date = in.readString();
        info = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        name = in.readString();
        openHours = in.readString();
        phoneNumber = in.readString();
        restaurantImageUri = in.readString();
        zipCode = in.readString();
        providerUsername = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(addedBy);
        dest.writeString(address);
        dest.writeString(category);
        dest.writeString(closeHours);
        dest.writeString(date);
        dest.writeString(info);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(name);
        dest.writeString(openHours);
        dest.writeString(phoneNumber);
        dest.writeString(restaurantImageUri);
        dest.writeString(zipCode);
        dest.writeString(providerUsername);
    }

    public static final Parcelable.Creator<RestaurantData> CREATOR = new Parcelable.Creator<RestaurantData>() {
        public RestaurantData createFromParcel(Parcel in) {
            return new RestaurantData(in);
        }

        public RestaurantData[] newArray(int size) {
            return new RestaurantData[size];
        }
    };

    // Getters and setters for all fields

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCloseHours() {
        return closeHours;
    }

    public void setCloseHours(String closeHours) {
        this.closeHours = closeHours;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRestaurantImageUri() {
        return restaurantImageUri;
    }

    public void setRestaurantImageUri(String restaurantImageUri) {
        this.restaurantImageUri = restaurantImageUri;
    }

    public String getZipCode() {
        return zipCode;
    }
}