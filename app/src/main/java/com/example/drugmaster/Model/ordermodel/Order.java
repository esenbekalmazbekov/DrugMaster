package com.example.drugmaster.Model.ordermodel;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

@IgnoreExtraProperties
public class Order{
    private String managerID;
    private HashMap<String,Integer> drugs;
    private String status = "Заказ Создан";
    private Double cost;

    public Order(String managerID){
        this.managerID = managerID;
        this.drugs = new HashMap<>();
        this.cost = 0.0;
    }

    public Order(){}

    public Double getCost(){return cost;}

    public void setCost(Double cost){this.cost = cost;}

    public HashMap<String, Integer> getDrugs() {
        return drugs;
    }

    public String getManagerID() {
        return managerID;
    }

    public void setStatus(String status){this.status = status;}

    public String getStatus() {
        return status;
    }
}
