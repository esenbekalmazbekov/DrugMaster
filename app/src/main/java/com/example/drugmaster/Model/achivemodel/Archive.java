package com.example.drugmaster.Model.achivemodel;

import java.util.ArrayList;

public class Archive {
    private String ID;
    private String clOrgName;
    private String clientEmail;
    private String clientID;
    private String managerName;
    private String managerEmail;
    private String price;

    private ArrayList<ShortDrug> list;
    private String date;

    Archive(String ID,String clOrgName, String clientEmail, String clientID, String managerName, String managerEmail, ArrayList<ShortDrug> list, String date,String price) {
        this.ID = ID;
        this.clOrgName = clOrgName;
        this.clientEmail = clientEmail;
        this.clientID = clientID;
        this.managerName = managerName;
        this.managerEmail = managerEmail;
        this.list = list;
        this.date = date;
        this.price = price;
    }
    Archive(){}

    public String getPrice() {
        return price;
    }

    public String getID() {
        return ID;
    }

    public String getClOrgName() {
        return clOrgName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public String getClientID() {
        return clientID;
    }

    public String getManagerName() {
        return managerName;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public ArrayList<ShortDrug> getList() {
        return list;
    }

    public String getDate() {
        return date;
    }
}
