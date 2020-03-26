package com.example.drugmaster.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Drug implements Parcelable,Cloneable {
    private String id;
    private String name;
    private String unit;
    private String maker;
    private String price;

    public Drug() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Drug(String id, String name, String unit, String maker, String price) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.maker = maker;
        this.price = price;
    }

    private Drug(Parcel in) {
        id = in.readString();
        name = in.readString();
        unit = in.readString();
        maker = in.readString();
        price = in.readString();
    }

    public static final Creator<Drug> CREATOR = new Creator<Drug>() {
        @Override
        public Drug createFromParcel(Parcel in) {
            return new Drug(in);
        }

        @Override
        public Drug[] newArray(int size) {
            return new Drug[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(unit);
        dest.writeString(maker);
        dest.writeString(price);
    }

    @NonNull
    @Override
    public Drug clone() throws CloneNotSupportedException {
        return (Drug) super.clone();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
