package com.example.drugmaster.Model.ordermodel;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

@IgnoreExtraProperties
public class Order {
    private String managerID;
    private HashMap<String,Integer> drugs;
    private String status = "СинийC";

    public Order(String managerID){
        this.managerID = managerID;
        this.drugs = new HashMap<>();
    }

    public Order(){}

    public HashMap<String, Integer> getDrugs() {
        return drugs;
    }

    public String getManagerID() {
        return managerID;
    }

    public String getStatus() {
        return status;
    }
}
