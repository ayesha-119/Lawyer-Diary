package com.uptree.lawdiary.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginPojo {
    @SerializedName("id")
    @Expose
    private String id;


    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("email")
    @Expose
    private String email;


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

    @SerializedName("password")
    @Expose
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public LoginPojo(String id, String username, String email, String phone, String chamber_no, String chamber_address, String image) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.chamber_no = chamber_no;
        this.chamber_address = chamber_address;
        this.image = image;
    }
}
