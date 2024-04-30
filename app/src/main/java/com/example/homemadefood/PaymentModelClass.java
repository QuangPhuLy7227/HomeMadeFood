package com.example.homemadefood;

public class PaymentModelClass {

    private String cardNumber;
    private String expiredDate;
    private String userCardName;

    public PaymentModelClass(String cardNumber, String expiredDate, String userCardName) {
        this.cardNumber = cardNumber;
        this.expiredDate = expiredDate;
        this.userCardName = userCardName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getUserCardName() {
        return userCardName;
    }

    public void setUserCardName(String userCardName) {
        this.userCardName = userCardName;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }
}
