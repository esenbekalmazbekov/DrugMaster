package com.example.drugmaster.Model;

public class Drug {
    private String id;
    private String name;
    private String unit;
    private String maker;
    private String price;

    public Drug() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Drug(String id,String name, String unit, String maker, String price) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.maker = maker;
        this.price = price;
    }
    public String getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public String getMaker() {
        return maker;
    }

    public String getPrice() {
        return price;
    }
}
