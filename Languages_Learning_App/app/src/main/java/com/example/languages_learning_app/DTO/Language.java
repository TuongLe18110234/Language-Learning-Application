package com.example.languages_learning_app.DTO;

import android.text.format.DateFormat;

public class Language {
    private String id;
    private int image;
    private String name, displayName, briefName;
    private boolean status;
    private String createDate;

    public Language(){}

    public Language(String name, String displayName, String briefName, int image) {
        this.name = name;
        this.displayName = displayName;
        this.briefName = briefName;
        this.status = false;
        this.image = image;
        createDate = DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()).toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBriefName() {
        return briefName;
    }

    public void setBriefName(String briefName) {
        this.briefName = briefName;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}

