package com.example.homemadefood;

public class RestaurantDataModel {
    private int id;
    private String resName;
    private String resLocation;
    private String resContact;
    private double resRating;
    private String provId;
    private boolean dineIn;
    private boolean open;

    public RestaurantDataModel(String resName, String resLocation, String resContact, double resRating, String provId, boolean dineIn, boolean open) {
        this.resName = resName;
        this.resLocation = resLocation;
        this.resContact = resContact;
        this.resRating = resRating;
        this.provId = provId;
        this.dineIn = dineIn;
        this.open = open;
    }
    public RestaurantDataModel() {
    }

    @Override
    public String toString() {
        return "RestaurantDataModel{" +
                ", resName='" + resName + '\'' +
                ", resLocation='" + resLocation + '\'' +
                ", resContact='" + resContact + '\'' +
                ", resRating=" + resRating +
                ", provId='" + provId + '\'' +
                ", dineIn=" + dineIn +
                ", open=" + open +
                '}';
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getResLocation() {
        return resLocation;
    }

    public void setResLocation(String resLocation) {
        this.resLocation = resLocation;
    }

    public String getResContact() {
        return resContact;
    }

    public void setResContact(String resContact) {
        this.resContact = resContact;
    }

    public double getResRating() {
        return resRating;
    }

    public void setResRating(double resRating) {
        this.resRating = resRating;
    }

    public boolean isDineIn() {
        return dineIn;
    }

    public void setDineIn(boolean dineIn) {
        this.dineIn = dineIn;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getProvId() {
        return provId;
    }

    public void setProvId(String provId) {
        this.provId = provId;
    }
}

