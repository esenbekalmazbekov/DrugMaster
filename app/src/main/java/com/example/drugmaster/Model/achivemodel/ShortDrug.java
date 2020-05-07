package com.example.drugmaster.Model.achivemodel;

public class ShortDrug {
    private String drugName;
    private int count;

    public ShortDrug(String drugName, int count) {
        this.drugName = drugName;
        this.count = count;
    }

    public String getDrugName() {
        return drugName;
    }

    public int getCount() {
        return count;
    }
}
