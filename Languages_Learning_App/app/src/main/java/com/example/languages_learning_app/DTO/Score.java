package com.example.languages_learning_app.DTO;

import android.speech.SpeechRecognizer;
import android.text.format.DateFormat;

import java.io.Serializable;

public class Score implements Serializable {
    // Required parameters
    private String id;
    private String traineeId;
    private String lessonId;

    // Optional parameters
    private String traineeName;
    private String lessonName;
    private int practiceEasyScore;
    private int practiceEasyPercentile;
    private int practiceHardScore;
    private int practiceHardPercentile;
    private int writingScore;
    private int writingPercentile;
    private int selectionScore;
    private int selectionPercentile;
    private int audioScore;
    private int audioPercentile;
    private int totalScore;
    private String createDate;

    public Score(){}

    public Score(String traineeId, String lessonId) {
        this.traineeId = traineeId;
        this.lessonId = lessonId;
        this.createDate = DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()).toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(String traineeId) {
        this.traineeId = traineeId;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public String getTraineeName() {
        return traineeName;
    }

    public void setTraineeName(String traineeName) {
        this.traineeName = traineeName;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public int getPracticeEasyScore() {
        return practiceEasyScore;
    }

    public void setPracticeEasyScore(int practiceEasyScore) {
        this.practiceEasyScore = practiceEasyScore;
    }

    public int getPracticeEasyPercentile() {
        return practiceEasyPercentile;
    }

    public void setPracticeEasyPercentile(int practiceEasyPercentile) {
        this.practiceEasyPercentile = practiceEasyPercentile;
    }

    public int getPracticeHardScore() {
        return practiceHardScore;
    }

    public void setPracticeHardScore(int practiceHardScore) {
        this.practiceHardScore = practiceHardScore;
    }

    public int getPracticeHardPercentile() {
        return practiceHardPercentile;
    }

    public void setPracticeHardPercentile(int practiceHardPercentile) {
        this.practiceHardPercentile = practiceHardPercentile;
    }

    public int getWritingScore() {
        return writingScore;
    }

    public void setWritingScore(int writingScore) {
        this.writingScore = writingScore;
    }

    public int getWritingPercentile() {
        return writingPercentile;
    }

    public void setWritingPercentile(int writingPercentile) {
        this.writingPercentile = writingPercentile;
    }

    public int getSelectionScore() {
        return selectionScore;
    }

    public void setSelectionScore(int selectionScore) {
        this.selectionScore = selectionScore;
    }

    public int getSelectionPercentile() {
        return selectionPercentile;
    }

    public void setSelectionPercentile(int selectionPercentile) {
        this.selectionPercentile = selectionPercentile;
    }

    public int getAudioScore() {
        return audioScore;
    }

    public void setAudioScore(int audioScore) {
        this.audioScore = audioScore;
    }

    public int getAudioPercentile() {
        return audioPercentile;
    }

    public void setAudioPercentile(int audioPercentile) {
        this.audioPercentile = audioPercentile;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
