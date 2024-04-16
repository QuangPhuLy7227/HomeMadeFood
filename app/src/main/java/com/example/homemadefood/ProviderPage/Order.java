package com.example.homemadefood.ProviderPage;

public class Order{

        private String orderId;
        private String orderDetails;
        private String username;
        private String email;

        // Constructors
        public Order(String orderId, String orderDetails, String username, String email) {
            this.email = email;
            this.username = username;
            this.orderId = orderId;
            this.orderDetails= orderDetails;
        }
        //Getters for orderID
        public String getOrderId() {
            return orderId;
        }
        //Getter for orderdetails
        public String getOrderDetails() {
            return orderDetails;
        }

        public String getUsername(){
            return username;
        }

        public String getEmail(){
            return email;
        }

      //  public void setOrderId(String order)
}
