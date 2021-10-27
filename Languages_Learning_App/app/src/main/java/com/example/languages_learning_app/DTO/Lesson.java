package com.example.languages_learning_app.DTO;

import android.text.format.DateFormat;

import java.io.Serializable;

public class Lesson implements Serializable {
    private String id;
    private String languageId;
    private int image;
    private String name, description;
    private int wordCount;
    private int activeWordCount;
    private int easyPracticeCount;
    private int hardPracticeCount;
    private boolean status;
    private String createDate;

    public Lesson() {
        // For firebase
    }

    public Lesson(String languageId, String name, String description) {
        this.languageId = languageId;
        this.name = name;
        this.description = description;
        this.wordCount = 0;
        this.activeWordCount = 0;
        this.easyPracticeCount = 0;
        this.hardPracticeCount = 0;
        status = false;
        createDate = DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()).toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public int getActiveWordCount() {
        return activeWordCount;
    }

    public void setActiveWordCount(int activeWordCount) {
        this.activeWordCount = activeWordCount;
    }

    public int getEasyPracticeCount() {
        return easyPracticeCount;
    }

    public void setEasyPracticeCount(int easyPracticeCount) {
        this.easyPracticeCount = easyPracticeCount;
    }

    public int getHardPracticeCount() {
        return hardPracticeCount;
    }

    public void setHardPracticeCount(int hardPracticeCount) {
        this.hardPracticeCount = hardPracticeCount;
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