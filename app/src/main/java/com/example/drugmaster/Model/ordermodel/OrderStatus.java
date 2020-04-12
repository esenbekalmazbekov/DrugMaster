package com.example.drugmaster.Model.ordermodel;

public class OrderStatus {
    private String clientID;
    private String status;

    public OrderStatus(String clientID, String status) {
        this.clientID = clientID;
        this.status = status;
    }
    public OrderStatus(){}

    public String getClientID() {
        return clientID;
    }

    public String getStatus() {
        return status;
    }
}
