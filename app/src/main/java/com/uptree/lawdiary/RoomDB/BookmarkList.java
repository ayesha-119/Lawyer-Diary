package com.uptree.lawdiary.RoomDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="bookmarkList")
public class BookmarkList {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "judge")
    private String judge;
    @ColumnInfo(name = "culprit")
    private String culprit;
    @ColumnInfo(name = "category")
    private String category;
    @ColumnInfo(name = "startingdate")
    private String startingdate;
    @ColumnInfo(name = "complaint")
    private String complaint;
    @ColumnInfo(name = "services")
    private String services;
    @ColumnInfo(name = "detail")
    private String detail;
    @ColumnInfo(name = "caseId")
    private String caseId;

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJudge() {
        return judge;
    }

    public void setJudge(String judge) {
        this.judge = judge;
    }

    public String getCulprit() {
        return culprit;
    }

    public void setCulprit(String culprit) {
        this.culprit = culprit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStartingdate() {
        return startingdate;
    }

    public void setStartingdate(String startingdate) {
        this.startingdate = startingdate;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
