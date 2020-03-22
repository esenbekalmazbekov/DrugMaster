package com.example.drugmaster.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User implements Parcelable {

    private String orgname;
    private String email;
    private String phone;
    private String address;
    private String status;
    private String id;
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String id,String orgname, String email, String phone, String address,String status) {
        this.id = id;
        this.orgname = orgname;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.status = status;
    }


    private User(Parcel in) {
        orgname = in.readString();
        email = in.readString();
        phone = in.readString();
        address = in.readString();
        status = in.readString();
        id = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getStatus() {
        return status;
    }

    public String getOrgname() {
        return orgname;
    }

    public String getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orgname);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(address);
        dest.writeString(status);
        dest.writeString(id);
    }
}