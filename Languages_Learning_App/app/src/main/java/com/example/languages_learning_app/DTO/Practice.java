package com.example.languages_learning_app.DTO;

import android.text.format.DateFormat;

public class Practice {
    private String id;
    private String lessonId;
    private String sentence;
    private String correctAnswer;
    private int type;
    private Boolean status;
    private String createDate;

    public Practice(){
        // For firebase
    }

    public Practice(String lessonId, String sentence, String correctAnswer, int type) {
        this.lessonId = lessonId;
        this.sentence = sentence;
        this.correctAnswer = correctAnswer;
        this.type = type;
        status = false;
        createDate = DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()).toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
