package com.example.drugmaster.Model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    private String orgname;
    private String email;
    private String phone;
    private String address;
    private String status;
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String orgname, String email, String phone, String address,String status) {
        this.orgname = orgname;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.status = status;
    }


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
}