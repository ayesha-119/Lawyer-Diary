package com.uptree.lawdiary.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegistrationPojo {
    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("email")
    @Expose
    private String email;


    @SerializedName("password")
    @Expose
    private String password;


    @SerializedName("phone")
    @Expose
    private String phone;


    @SerializedName("chamber_no")
    @Expose
    private String chamber_no;


    @SerializedName("chamber_address")
    @Expose
    private String chamber_address;

    @SerializedName("image")
    @Expose
    private String image;

    private String result;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getChamber_no() {
        return chamber_no;
    }

    public void setChamber_no(String chamber_no) {
        this.chamber_no = chamber_no;
    }

    public String getChamber_address() {
        return chamber_address;
    }

    public void setChamber_address(String chamber_address) {
        this.chamber_address = chamber_address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
