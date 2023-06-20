package com.uptree.lawdiary.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HearingModel {
    @SerializedName("user_id")
    @Expose
    private String user_id;

    @SerializedName("case_id")
    @Expose
    private String case_id;

    @SerializedName("new_date")
    @Expose
    private String new_date;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("result")
    @Expose
    private String result;




    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    public String getNew_date() {
        return new_date;
    }

    public void setNew_date(String new_date) {
        this.new_date = new_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
