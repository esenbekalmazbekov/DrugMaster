package com.example.drugmaster.Model.achivemodel;

import android.os.Parcel;
import android.os.Parcelable;

public class ShortDrug implements Parcelable {
    private String drugName;
    private int count;

    ShortDrug(String drugName, int count) {
        this.drugName = drugName;
        this.count = count;
    }
    ShortDrug(){}

    private ShortDrug(Parcel in) {
        drugName = in.readString();
        count = in.readInt();
    }

    public static final Creator<ShortDrug> CREATOR = new Creator<ShortDrug>() {
        @Override
        public ShortDrug createFromParcel(Parcel in) {
            return new ShortDrug(in);
        }

        @Override
        public ShortDrug[] newArray(int size) {
            return new ShortDrug[size];
        }
    };

    public String getDrugName() {
        return drugName;
    }

    public int getCount() {
        return count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(drugName);
        dest.writeInt(count);
    }
}
