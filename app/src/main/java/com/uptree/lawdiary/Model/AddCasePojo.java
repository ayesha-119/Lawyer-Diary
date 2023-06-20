package com.uptree.lawdiary.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddCasePojo {
    @SerializedName("case_title")
    @Expose
    private String case_title;

    @SerializedName("name_of_judge")
    @Expose
    private String name_of_judge;

    @SerializedName("name_of_culprit")
    @Expose
    private String name_of_culprit;

    @SerializedName("case_category")
    @Expose
    private String case_category;

    @SerializedName("case_details")
    @Expose
    private String case_details;

    @SerializedName("case_starting_date")
    @Expose
    private String case_starting_date;

    @SerializedName("current_date")
    @Expose
    private String current_date;

    @SerializedName("user_id")
    @Expose
    private String user_id;

    @SerializedName("name_of_complain")
    @Expose
    private String name_of_complain;

    @SerializedName("our_services_for")
    @Expose
    private String our_services_for;


    @SerializedName("search_text")
    @Expose
    private String search_text;


    @SerializedName("win")
    @Expose
    private String win;

    @SerializedName("judgement")
    @Expose
    private String judgement;

    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getWin() {
        return win;
    }

    public void setWin(String win) {
        this.win = win;
    }

    public String getJudgement() {
        return judgement;
    }

    public void setJudgement(String judgement) {
        this.judgement = judgement;
    }

    public String getSearch_text() {
        return search_text;
    }

    public void setSearch_text(String search_text) {
        this.search_text = search_text;
    }

    public String getName_of_complain() {
        return name_of_complain;
    }

    public void setName_of_complain(String name_of_complain) {
        this.name_of_complain = name_of_complain;
    }

    public String getOur_services_for() {
        return our_services_for;
    }

    public void setOur_services_for(String our_services_for) {
        this.our_services_for = our_services_for;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("new_date")
    @Expose
    private String new_date;

    public String getNew_date() {
        return new_date;
    }

    public void setNew_date(String new_date) {
        this.new_date = new_date;
    }

    public String getCase_title() {
        return case_title;
    }

    public void setCase_title(String case_title) {
        this.case_title = case_title;
    }

    public String getName_of_judge() {
        return name_of_judge;
    }

    public void setName_of_judge(String name_of_judge) {
        this.name_of_judge = name_of_judge;
    }

    public String getName_of_culprit() {
        return name_of_culprit;
    }

    public void setName_of_culprit(String name_of_culprit) {
        this.name_of_culprit = name_of_culprit;
    }

    public String getCase_category() {
        return case_category;
    }

    public void setCase_category(String case_category) {
        this.case_category = case_category;
    }

    public String getCase_details() {
        return case_details;
    }

    public void setCase_details(String case_details) {
        this.case_details = case_details;
    }

    public String getCase_starting_date() {
        return case_starting_date;
    }

    public void setCase_starting_date(String case_starting_date) {
        this.case_starting_date = case_starting_date;
    }

    public String getCurrent_date() {
        return current_date;
    }

    public void setCurrent_date(String current_date) {
        this.current_date = current_date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public AddCasePojo(String case_title, String name_of_judge, String name_of_culprit, String case_category, String case_details, String case_starting_date, String current_date, String user_id) {
        this.case_title = case_title;
        this.name_of_judge = name_of_judge;
        this.name_of_culprit = name_of_culprit;
        this.case_category = case_category;
        this.case_details = case_details;
        this.case_starting_date = case_starting_date;
        this.current_date = current_date;
        this.user_id = user_id;
    }

    //for fvrt item
    private String key_id;
    private String favStatus;

    public String getKey_id() {
        return key_id;
    }

    public void setKey_id(String key_id) {
        this.key_id = key_id;
    }

    public String getFavStatus() {
        return favStatus;
    }

    public void setFavStatus(String favStatus) {
        this.favStatus = favStatus;
    }
}
