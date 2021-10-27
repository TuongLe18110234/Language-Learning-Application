package com.example.languages_learning_app.DTO;

import android.text.format.DateFormat;

public class Vocabulary {
    // Required parameters
    private String id;
    private String word;
    private String meaning;

    // Optional parameters
    private String pronunciation;
    private String imageUrl;
    private String lessonId;
    private String createDate;
    private boolean isActive;

    public String getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLessonId() {
        return lessonId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public boolean isActive() {
        return isActive;
    }

    // Private constructors
    private Vocabulary(){}

    public Vocabulary(VocabularyBuilder builder) {
        this.id = builder.id;
        this.word = builder.word;
        this.meaning = builder.meaning;
        this.pronunciation = builder.pronunciation;
        this.imageUrl = builder.imageUrl;
        this.lessonId = builder.lessonId;
        this.isActive = false;
        this.createDate = DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()).toString();
    }

    // Builder class
    public static class VocabularyBuilder {
        // Required parameters
        private String id;
        private String word;
        private String meaning;

        // Optional parameters
        private String pronunciation;
        private String imageUrl;
        private String lessonId;

        public VocabularyBuilder(String id, String word, String meaning) {
            this.id = id;
            this.word = word;
            this.meaning = meaning;
        }

        public VocabularyBuilder setPronunciation(String pronunciation) {
            this.pronunciation = pronunciation;
            return this;
        }

        public VocabularyBuilder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public VocabularyBuilder setLessonId(String lessonId) {
            this.lessonId = lessonId;
            return this;
        }

        public Vocabulary build() {
            return new Vocabulary(this);
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
